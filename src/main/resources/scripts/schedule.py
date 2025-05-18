import psycopg2
import pandas as pd
from heapq import heappush, heappop
from datetime import datetime, timedelta
import logging
import platform
import numpy as np
import sys
import argparse
from collections import defaultdict
from multiprocessing import Pool, Manager, Lock

# Debug: In thông tin môi trường Python
print(f"Python interpreter: {sys.executable}")
print(f"Python version: {sys.version}")
print(f"System PATH: {sys.path}")

# Cấu hình logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Parse tham số dòng lệnh
parser = argparse.ArgumentParser()
parser.add_argument("--semesterId", type=int, default=1)
args = parser.parse_args()
SEMESTER_ID = args.semesterId

# Hằng số
MAX_SLOTS_PER_DAY = 10
DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
DAY_INDEX = {day: i for i, day in enumerate(DAYS)}
room_cache = {}
schedule_cache = {}

# Kết nối CSDL PostgreSQL
def get_db_connection():
    try:
        conn = psycopg2.connect(
            dbname="student_db",
            user="studentApp",
            password="1234$",
            host="localhost",
            port="5432"
        )
        logger.info("Kết nối thành công đến cơ sở dữ liệu PostgreSQL")
        return conn
    except psycopg2.Error as e:
        logger.error(f"Lỗi kết nối cơ sở dữ liệu: {e}")
        raise

# Tính số tuần giữa start_date và end_date
def calculate_weeks(start_date, end_date):
    start = datetime.strptime(str(start_date), '%Y-%m-%d')
    end = datetime.strptime(str(end_date), '%Y-%m-%d')
    weeks = (end - start).days // 7 + 1
    return max(1, weeks)

# Lấy dữ liệu từ cơ sở dữ liệu
def fetch_data():
    conn = get_db_connection()
    query_classes = """
        SELECT c.id AS class_id, c.class_code, c.class_name, c.class_group_id, c.subject_id, c.teacher_id, 
               c.semester_id, c.room_type, c.max_students, c.start_date, c.end_date, c.priority, 
               c.period_length, c.theory_periods, c.practical_periods, c.class_duration,
               d.dept_code AS dept_id
        FROM tbl_class c
        JOIN tbl_department d ON d.id = (SELECT dept_id FROM tbl_subject WHERE id = c.subject_id)
        WHERE c.semester_id = %s
    """
    query_rooms = "SELECT id AS room_id, room_code AS room_name, room_type, capacity, dept_id FROM tbl_room"
    query_preferences = """
        SELECT teacher_id, week, day_of_week, slot 
        FROM tbl_teacher_preference 
        WHERE teacher_id IN (SELECT teacher_id FROM tbl_class WHERE semester_id = %s)
    """

    try:
        classes_df = pd.read_sql(query_classes, conn, params=(SEMESTER_ID,))
        rooms_df = pd.read_sql(query_rooms, conn)
        preferences_df = pd.read_sql(query_preferences, conn, params=(SEMESTER_ID,))
        logger.info(f"Lấy được {len(classes_df)} lớp, {len(rooms_df)} phòng, {len(preferences_df)} ưu tiên")
        logger.debug(f"Dữ liệu lớp: {classes_df[['class_id', 'subject_id', 'room_type', 'max_students', 'dept_id']].to_dict('records')}")
        logger.debug(f"Dữ liệu phòng: {rooms_df[['room_id', 'room_type', 'capacity', 'dept_id']].to_dict('records')}")
    except Exception as e:
        logger.error(f"Lỗi khi lấy dữ liệu: {e}")
        raise
    finally:
        conn.close()
    return classes_df, rooms_df, preferences_df

# Tiền xử lý dữ liệu phòng
def preprocess_rooms(rooms_df):
    room_lookup = defaultdict(list)
    for _, row in rooms_df.iterrows():
        room_type = row['room_type']
        capacity = row['capacity']
        dept_id = row['dept_id'] if pd.notna(row['dept_id']) else None
        room_id = row['room_id']
        # Lưu tất cả các phòng có capacity >= total_students
        for cap in range(50, capacity + 1):  # Bắt đầu từ 50 vì max_students = 50
            room_lookup[(room_type, cap, dept_id)].append(room_id)
            if dept_id is not None:
                room_lookup[(room_type, cap, None)].append(room_id)
        # Ánh xạ lab sang practical
        if room_type == 'lab':
            for cap in range(50, capacity + 1):
                room_lookup[('practical', cap, dept_id)].append(room_id)
                if dept_id is not None:
                    room_lookup[('practical', cap, None)].append(room_id)
    return room_lookup

# Lấy phòng phù hợp (sử dụng lookup table)
def get_suitable_rooms(subject_id, room_type, dept_id, total_students, rooms_df, room_lookup):
    cache_key = (subject_id, room_type, dept_id, total_students)
    if cache_key in room_cache:
        return room_cache[cache_key]
    room_type_mapped = 'practical' if room_type == 'practical' else room_type
    # Tìm phòng với dept_id cụ thể
    suitable = room_lookup.get((room_type_mapped, total_students, dept_id), [])
    if not suitable:
        # Tìm phòng chung (dept_id=None)
        suitable = room_lookup.get((room_type_mapped, total_students, None), [])
        if not suitable and room_type_mapped == 'practical':
            # Thử tìm lab nếu không có practical
            suitable = room_lookup.get(('lab', total_students, dept_id), [])
            if not suitable:
                suitable = room_lookup.get(('lab', total_students, None), [])
        if not suitable:
            logger.warning(f"Không tìm thấy phòng phù hợp cho subject_id={subject_id}, room_type={room_type}, total_students={total_students}, dept_id={dept_id}")
    room_cache[cache_key] = suitable
    return suitable

# Thuật toán Greedy để xếp lịch ban đầu
def greedy_schedule(class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock):
    class_id = class_info['class_id']
    class_group_id = class_info['class_group_id']
    subject_id = class_info['subject_id']
    teacher_id = class_info['teacher_id']
    total_students = class_info['max_students']
    start_date = class_info['start_date']
    end_date = class_info['end_date']

    theory_periods = class_info['theory_periods']
    practical_periods = class_info['practical_periods']
    total_periods = theory_periods + practical_periods

    num_weeks = calculate_weeks(start_date, end_date)
    periods_per_week = max(1, total_periods // num_weeks)
    remaining_periods = total_periods % num_weeks

    schedule = []
    teacher_slots = [(w, d, s) for w in range(1, num_weeks + 1) for d, s in global_slots if (d, s) in teacher_preferences.get(teacher_id, set())]

    # Phân bổ tiết lý thuyết
    theory_scheduled = 0
    for week in range(1, num_weeks + 1):
        periods_this_week = periods_per_week + (1 if week <= remaining_periods else 0)
        periods_to_schedule = min(periods_this_week, theory_periods - theory_scheduled)
        if periods_to_schedule <= 0:
            break

        week_slots = [(w, d, s) for w, d, s in teacher_slots if w == week]
        suitable_rooms = get_suitable_rooms(subject_id, 'theory', class_info['dept_id'], total_students, rooms_df, room_lookup)

        if not suitable_rooms:
            logger.warning(f"Không có phòng lý thuyết cho lớp {class_id}, bỏ qua tuần {week}")
            continue

        for _ in range(periods_to_schedule):
            scheduled = False
            for slot in sorted(week_slots, key=lambda x: (DAY_INDEX[x[1]], x[2])):
                with lock:
                    if slot in slot_usage:
                        continue
                    # Khởi tạo slot nếu chưa tồn tại
                    if slot not in room_usage:
                        room_usage[slot] = set()
                    if slot not in teacher_usage:
                        teacher_usage[slot] = set()
                    if slot not in group_usage:
                        group_usage[slot] = set()

                    room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                    if not room:
                        continue
                    if teacher_id in teacher_usage[slot] or class_group_id in group_usage[slot]:
                        continue

                    schedule.append({
                        'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': 'theory',
                        'class_group_id': class_group_id, 'subject_id': subject_id, 'teacher_id': teacher_id
                    })
                    slot_usage[slot] = True
                    room_usage[slot].add(room)
                    teacher_usage[slot].add(teacher_id)
                    group_usage[slot].add(class_group_id)
                    theory_scheduled += 1
                    scheduled = True
                    break
            if not scheduled:
                logger.warning(f"Không thể xếp tiết lý thuyết cho lớp {class_id} trong tuần {week}")

    # Phân bổ tiết thực hành
    practical_scheduled = 0
    for week in range(1, num_weeks + 1):
        periods_this_week = periods_per_week + (1 if week <= remaining_periods else 0)
        periods_to_schedule = min(periods_this_week, practical_periods - practical_scheduled)
        if periods_to_schedule <= 0:
            break

        week_slots = [(w, d, s) for w, d, s in teacher_slots if w == week]
        suitable_rooms = get_suitable_rooms(subject_id, 'practical', class_info['dept_id'], total_students, rooms_df, room_lookup)

        if not suitable_rooms:
            logger.warning(f"Không có phòng thực hành cho lớp {class_id}, bỏ qua tuần {week}")
            continue

        for _ in range(periods_to_schedule):
            scheduled = False
            for slot in sorted(week_slots, key=lambda x: (DAY_INDEX[x[1]], x[2])):
                with lock:
                    if slot in slot_usage:
                        continue
                    # Khởi tạo slot nếu chưa tồn tại
                    if slot not in room_usage:
                        room_usage[slot] = set()
                    if slot not in teacher_usage:
                        teacher_usage[slot] = set()
                    if slot not in group_usage:
                        group_usage[slot] = set()

                    room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                    if not room:
                        continue
                    if teacher_id in teacher_usage[slot] or class_group_id in group_usage[slot]:
                        continue

                    schedule.append({
                        'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': 'practical',
                        'class_group_id': class_group_id, 'subject_id': subject_id, 'teacher_id': teacher_id
                    })
                    slot_usage[slot] = True
                    room_usage[slot].add(room)
                    teacher_usage[slot].add(teacher_id)
                    group_usage[slot].add(class_group_id)
                    practical_scheduled += 1
                    scheduled = True
                    break
            if not scheduled:
                logger.warning(f"Không thể xếp tiết thực hành cho lớp {class_id} trong tuần {week}")

    logger.info(f"Xếp lịch cho lớp {class_id}: {theory_scheduled}/{theory_periods} tiết lý thuyết, {practical_scheduled}/{practical_periods} tiết thực hành")
    return schedule

# Nút cho A* (State)
class ScheduleNode:
    def __init__(self, class_id, assigned_slots, cost, heuristic):
        self.class_id = class_id
        self.assigned_slots = assigned_slots
        self.cost = cost
        self.heuristic = heuristic
        self.total_cost = cost + heuristic

    def __lt__(self, other):
        return self.total_cost < other.total_cost

# A* tối ưu hóa với giới hạn độ sâu
def optimize_with_a_star(class_info, initial_schedule, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock, max_iterations=100):
    class_id = class_info['class_id']
    class_group_id = class_info['class_group_id']
    subject_id = class_info['subject_id']
    teacher_id = class_info['teacher_id']
    total_students = class_info['max_students']
    start_date = class_info['start_date']
    end_date = class_info['end_date']
    theory_periods = class_info['theory_periods']
    practical_periods = class_info['practical_periods']
    total_periods = theory_periods + practical_periods

    if len(initial_schedule) == total_periods:
        return initial_schedule

    logger.warning(f"Lịch ban đầu cho lớp {class_id} không đủ tiết, sử dụng A* để xếp lại")
    initial_slots = initial_schedule

    with lock:
        for s in initial_slots:
            slot = (s['week'], s['day'], s['slot'])
            if slot not in room_usage:
                room_usage[slot] = set()
            if slot not in teacher_usage:
                teacher_usage[slot] = set()
            if slot not in group_usage:
                group_usage[slot] = set()
            slot_usage[slot] = True
            room_usage[slot].add(s['room_id'])
            teacher_usage[slot].add(s['teacher_id'])
            group_usage[slot].add(s['class_group_id'])

    open_set = []
    closed_set = set()
    heappush(open_set, ScheduleNode(class_id, initial_slots, 0, total_periods - len(initial_slots)))
    best_schedule = initial_slots
    min_cost = float('inf')
    iterations = 0

    num_weeks = calculate_weeks(start_date, end_date)
    teacher_slots = [(w, d, s) for w in range(1, num_weeks + 1) for d, s in global_slots if (d, s) in teacher_preferences.get(teacher_id, set())]

    while open_set and iterations < max_iterations:
        current_node = heappop(open_set)
        if tuple(sorted([s['week'] * 100 + DAY_INDEX[s['day']] * 10 + s['slot'] for s in current_node.assigned_slots])) in closed_set:
            continue
        closed_set.add(tuple(sorted([s['week'] * 100 + DAY_INDEX[s['day']] * 10 + s['slot'] for s in current_node.assigned_slots])))

        current_theory = sum(1 for s in current_node.assigned_slots if s['type'] == 'theory')
        current_practical = sum(1 for s in current_node.assigned_slots if s['type'] == 'practical')
        periods_left = (theory_periods - current_theory) + (practical_periods - current_practical)

        if periods_left <= 0:
            with lock:
                conflict_count = sum(1 for slot in [(s['week'], s['day'], s['slot']) for s in current_node.assigned_slots] if slot in slot_usage and slot_usage[slot])
                if current_node.cost + conflict_count < min_cost:
                    min_cost = current_node.cost + conflict_count
                    best_schedule = current_node.assigned_slots
            continue

        slot_type = 'theory' if current_theory < theory_periods else 'practical'
        suitable_rooms = get_suitable_rooms(subject_id, slot_type, class_info['dept_id'], total_students, rooms_df, room_lookup)

        if not suitable_rooms:
            logger.warning(f"Không có phòng {slot_type} cho lớp {class_id} trong A*")
            break

        for slot in teacher_slots:
            with lock:
                if slot in slot_usage:
                    continue
                day_key = (slot[0], slot[1])
                assigned_days = {(s['week'], s['day']) for s in current_node.assigned_slots}
                if day_key in assigned_days and len([s for s in current_node.assigned_slots if s['week'] == slot[0] and s['day'] == slot[1]]) >= 2:
                    continue

                if slot not in room_usage:
                    room_usage[slot] = set()
                if slot not in teacher_usage:
                    teacher_usage[slot] = set()
                if slot not in group_usage:
                    group_usage[slot] = set()

                room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                if not room:
                    continue
                if teacher_id in teacher_usage[slot] or class_group_id in group_usage[slot]:
                    continue

                new_slots = current_node.assigned_slots.copy()
                new_slots.append({
                    'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': slot_type,
                    'class_group_id': class_group_id, 'subject_id': subject_id, 'teacher_id': teacher_id
                })
                slot_usage[slot] = True
                room_usage[slot].add(room)
                teacher_usage[slot].add(teacher_id)
                group_usage[slot].add(class_group_id)

                new_cost = current_node.cost + (100 - class_info['priority']) + periods_left * 0.1
                new_heuristic = periods_left
                heappush(open_set, ScheduleNode(class_id, new_slots, new_cost, new_heuristic))

                # Xóa trạng thái, nhưng kiểm tra trước khi remove
                del slot_usage[slot]
                if room in room_usage[slot]:
                    room_usage[slot].remove(room)
                if teacher_id in teacher_usage[slot]:
                    teacher_usage[slot].remove(teacher_id)
                if class_group_id in group_usage[slot]:
                    group_usage[slot].remove(class_group_id)
                iterations += 1

    return best_schedule

# Hàm xử lý từng lớp trong multiprocessing
def schedule_class(args):
    class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock = args
    class_id = class_info['class_id']
    initial_schedule = greedy_schedule(class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock)
    optimized_schedule = optimize_with_a_star(class_info, initial_schedule, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock)
    return class_id, optimized_schedule

# Hàm chính để xếp lịch
def main():
    try:
        classes_df, rooms_df, preferences_df = fetch_data()
        teacher_preferences = preferences_df.groupby('teacher_id').apply(
            lambda x: set(zip(x['day_of_week'], x['slot'])), include_groups=False
        ).to_dict()
        global_slots = set((row['day_of_week'], row['slot']) for _, row in preferences_df.iterrows())

        # Tiền xử lý dữ liệu phòng
        room_lookup = preprocess_rooms(rooms_df)

        # Sử dụng Manager để chia sẻ trạng thái giữa các tiến trình
        manager = Manager()
        slot_usage = manager.dict()
        room_usage = manager.dict()
        teacher_usage = manager.dict()
        group_usage = manager.dict()
        lock = manager.Lock()  # Thêm lock để đồng bộ hóa

        schedule = {}
        with Pool(processes=min(4, len(classes_df))) as pool:
            args_list = [(row, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, group_usage, lock) for _, row in classes_df.iterrows()]
            results = pool.map(schedule_class, args_list)
            for class_id, class_schedule in results:
                schedule[class_id] = class_schedule

        # Lưu lịch vào CSDL, loại bỏ trùng lặp
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM tbl_schedule WHERE semester_id = %s", (SEMESTER_ID,))
        # Tạo tập hợp duy nhất dựa trên (room_id, semester_id, week, day_of_week, slot)
        unique_schedule = {}
        for class_id, slots in schedule.items():
            for s in slots:
                key = (s['room_id'], SEMESTER_ID, s['week'], s['day'], s['slot'])
                if key not in unique_schedule or s['class_group_id'] not in [x['class_group_id'] for x in unique_schedule.get(key, [])]:
                    if key not in unique_schedule:
                        unique_schedule[key] = []
                    unique_schedule[key].append(s)

        values = [
            (
                SEMESTER_ID,
                s['class_group_id'],
                s['subject_id'],
                s['teacher_id'],
                s['week'],
                s['day'],
                s['slot'],
                s['room_id']
            )
            for slots in unique_schedule.values()
            for s in slots
        ]
        logger.info(f"Đã xếp và lưu {len(values)} tiết học sau khi loại bỏ trùng lặp.")
        if values:
            cursor.executemany(
                """
                INSERT INTO tbl_schedule (semester_id, class_group_id, subject_id, teacher_id, week, day_of_week, slot, room_id)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT ON CONSTRAINT unique_room_slot DO NOTHING
                """,
                values
            )
            conn.commit()
        else:
            logger.warning("Không có lịch nào để lưu vào tbl_schedule.")
        cursor.close()
        conn.close()
    except Exception as e:
        logger.error(f"Lỗi trong main: {e}")
        raise

if platform.system() == "Emscripten":
    import asyncio
    asyncio.ensure_future(main())
else:
    if __name__ == "__main__":
        main()