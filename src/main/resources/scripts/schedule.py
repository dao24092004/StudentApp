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

# Debug: Print Python environment info
print(f"Python interpreter: {sys.executable}")
print(f"Python version: {sys.version}")
print(f"System PATH: {sys.path}")

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Parse command line arguments
parser = argparse.ArgumentParser()
parser.add_argument("--semesterId", type=int, default=1)
args = parser.parse_args()
SEMESTER_ID = args.semesterId

# Constants
MAX_SLOTS_PER_DAY = 14  # Tăng lên 14 slot để bao phủ từ 07:00 đến 21:00
DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
DAY_INDEX = {day: i for i, day in enumerate(DAYS)}
SLOT_DURATION_MINUTES = 50  # Mỗi tiết học 50 phút
SLOT_START_HOUR = 7  # Bắt đầu từ 07:00 sáng
# Tính toán SLOT_TIMES để bao phủ từ 07:00 đến 21:00 (14 slot x 50 phút = 700 phút ~ 11.67 giờ)
SLOT_TIMES = {i: (SLOT_START_HOUR + ((i-1) * (SLOT_DURATION_MINUTES / 60)), SLOT_START_HOUR + (i * (SLOT_DURATION_MINUTES / 60))) for i in range(1, MAX_SLOTS_PER_DAY + 1)}
room_cache = {}
schedule_cache = {}

# Database connection
def get_db_connection():
    try:
        conn = psycopg2.connect(
            dbname="student_db",
            user="studentApp",
            password="1234$",
            host="localhost",
            port="5432"
        )
        logger.info("Successfully connected to PostgreSQL database")
        return conn
    except psycopg2.Error as e:
        logger.error(f"Database connection error: {e}")
        raise

# Calculate number of weeks between start_date and end_date
def calculate_weeks(start_date, end_date):
    start = datetime.strptime(str(start_date), '%Y-%m-%d')
    end = datetime.strptime(str(end_date), '%Y-%m-%d')
    weeks = (end - start).days // 7 + 1
    return max(1, weeks)

# Calculate start_time and end_time based on slot and week (50-minute slots)
def calculate_times(week, day_of_week, slot, start_date):
    start = datetime.strptime(str(start_date), '%Y-%m-%d')
    day_index = DAY_INDEX[day_of_week]
    base_time = start + timedelta(weeks=week-1, days=day_index)
    start_hour = SLOT_START_HOUR + ((slot-1) * (SLOT_DURATION_MINUTES / 60))
    start_time = base_time.replace(hour=int(start_hour), minute=int((start_hour % 1) * 60), second=0)
    end_time = start_time + timedelta(minutes=SLOT_DURATION_MINUTES)
    return start_time.strftime('%Y-%m-%d %H:%M:%S'), end_time.strftime('%Y-%m-%d %H:%M:%S')

# Fetch data from database
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
        logger.info(f"Fetched {len(classes_df)} classes, {len(rooms_df)} rooms, {len(preferences_df)} preferences")
        logger.debug(f"Classes data: {classes_df[['class_id', 'subject_id', 'room_type', 'max_students', 'dept_id']].to_dict('records')}")
        logger.debug(f"Rooms data: {rooms_df[['room_id', 'room_type', 'capacity', 'dept_id']].to_dict('records')}")
    except Exception as e:
        logger.error(f"Error fetching data: {e}")
        raise
    finally:
        conn.close()
    return classes_df, rooms_df, preferences_df

# Preprocess rooms data
def preprocess_rooms(rooms_df):
    room_lookup = defaultdict(list)
    for _, row in rooms_df.iterrows():
        room_type = row['room_type']
        capacity = row['capacity']
        dept_id = row['dept_id'] if pd.notna(row['dept_id']) else None
        room_id = row['room_id']
        for cap in range(50, capacity + 1):
            room_lookup[(room_type, cap, dept_id)].append(room_id)
            if dept_id is not None:
                room_lookup[(room_type, cap, None)].append(room_id)
        if room_type == 'lab':
            for cap in range(50, capacity + 1):
                room_lookup[('practical', cap, dept_id)].append(room_id)
                if dept_id is not None:
                    room_lookup[('practical', cap, None)].append(room_id)
    return room_lookup

# Get suitable rooms
def get_suitable_rooms(subject_id, room_type, dept_id, total_students, rooms_df, room_lookup):
    cache_key = (subject_id, room_type, dept_id, total_students)
    if cache_key in room_cache:
        return room_cache[cache_key]
    room_type_mapped = 'practical' if room_type == 'practical' else room_type
    suitable = room_lookup.get((room_type_mapped, total_students, dept_id), [])
    if not suitable:
        suitable = room_lookup.get((room_type_mapped, total_students, None), [])
        if not suitable and room_type_mapped == 'practical':
            suitable = room_lookup.get(('lab', total_students, dept_id), [])
            if not suitable:
                suitable = room_lookup.get(('lab', total_students, None), [])
        if not suitable:
            logger.warning(f"No suitable room for subject_id={subject_id}, room_type={room_type}, total_students={total_students}, dept_id={dept_id}")
    room_cache[cache_key] = suitable
    return suitable

# Greedy scheduling algorithm with 5-slot limit per day per class
def greedy_schedule(class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock):
    class_id = class_info['class_id']
    subject_id = class_info['subject_id']
    teacher_id = class_info['teacher_id']
    total_students = class_info['max_students']
    start_date = class_info['start_date']
    period_length = class_info['period_length']
    
    theory_periods = class_info['theory_periods']
    practical_periods = class_info['practical_periods']
    total_periods = theory_periods + practical_periods

    num_weeks = calculate_weeks(class_info['start_date'], class_info['end_date'])
    periods_per_week = max(1, total_periods // num_weeks)
    remaining_periods = total_periods % num_weeks

    schedule = []
    teacher_slots = [(w, d, s) for w in range(1, num_weeks + 1) for d, s in global_slots if (d, s) in teacher_preferences.get(teacher_id, set())]
    daily_slot_count = defaultdict(int)  # Theo dõi số slot mỗi ngày cho class_id

    # Schedule theory periods
    theory_scheduled = 0
    for week in range(1, num_weeks + 1):
        periods_this_week = periods_per_week + (1 if week <= remaining_periods else 0)
        periods_to_schedule = min(periods_this_week, theory_periods - theory_scheduled)
        if periods_to_schedule <= 0:
            break

        week_slots = [(w, d, s) for w, d, s in teacher_slots if w == week]
        suitable_rooms = get_suitable_rooms(subject_id, 'theory', class_info['dept_id'], total_students, rooms_df, room_lookup)

        if not suitable_rooms:
            logger.warning(f"No theory rooms for class {class_id}, skipping week {week}")
            continue

        for _ in range(periods_to_schedule):
            scheduled = False
            for slot in sorted(week_slots, key=lambda x: (DAY_INDEX[x[1]], x[2])):
                day_key = (slot[0], slot[1])  # (week, day)
                with lock:
                    if slot in slot_usage or daily_slot_count[day_key] >= 5:
                        continue
                    if slot not in room_usage:
                        room_usage[slot] = set()
                    if slot not in teacher_usage:
                        teacher_usage[slot] = set()
                    if slot not in class_usage:
                        class_usage[slot] = set()

                    room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                    if not room:
                        continue
                    if teacher_id in teacher_usage[slot] or class_id in class_usage[slot]:
                        continue

                    start_time, end_time = calculate_times(week, slot[1], slot[2], start_date)
                    schedule.append({
                        'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': 'theory',
                        'class_id': class_id, 'subject_id': subject_id, 'teacher_id': teacher_id,
                        'period': period_length, 'start_time': start_time, 'end_time': end_time, 'status': 'ACTIVE'
                    })
                    slot_usage[slot] = True
                    room_usage[slot].add(room)
                    teacher_usage[slot].add(teacher_id)
                    class_usage[slot].add(class_id)
                    daily_slot_count[day_key] += 1
                    theory_scheduled += 1
                    scheduled = True
                    break
            if not scheduled:
                logger.warning(f"Could not schedule theory period for class {class_id} in week {week}")

    # Schedule practical periods
    practical_scheduled = 0
    daily_slot_count.clear()  # Reset counter for practical periods
    for week in range(1, num_weeks + 1):
        periods_this_week = periods_per_week + (1 if week <= remaining_periods else 0)
        periods_to_schedule = min(periods_this_week, practical_periods - practical_scheduled)
        if periods_to_schedule <= 0:
            break

        week_slots = [(w, d, s) for w, d, s in teacher_slots if w == week]
        suitable_rooms = get_suitable_rooms(subject_id, 'practical', class_info['dept_id'], total_students, rooms_df, room_lookup)

        if not suitable_rooms:
            logger.warning(f"No practical rooms for class {class_id}, skipping week {week}")
            continue

        for _ in range(periods_to_schedule):
            scheduled = False
            for slot in sorted(week_slots, key=lambda x: (DAY_INDEX[x[1]], x[2])):
                day_key = (slot[0], slot[1])  # (week, day)
                with lock:
                    if slot in slot_usage or daily_slot_count[day_key] >= 5:
                        continue
                    if slot not in room_usage:
                        room_usage[slot] = set()
                    if slot not in teacher_usage:
                        teacher_usage[slot] = set()
                    if slot not in class_usage:
                        class_usage[slot] = set()

                    room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                    if not room:
                        continue
                    if teacher_id in teacher_usage[slot] or class_id in class_usage[slot]:
                        continue

                    start_time, end_time = calculate_times(week, slot[1], slot[2], start_date)
                    schedule.append({
                        'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': 'practical',
                        'class_id': class_id, 'subject_id': subject_id, 'teacher_id': teacher_id,
                        'period': period_length, 'start_time': start_time, 'end_time': end_time, 'status': 'ACTIVE'
                    })
                    slot_usage[slot] = True
                    room_usage[slot].add(room)
                    teacher_usage[slot].add(teacher_id)
                    class_usage[slot].add(class_id)
                    daily_slot_count[day_key] += 1
                    practical_scheduled += 1
                    scheduled = True
                    break
            if not scheduled:
                logger.warning(f"Could not schedule practical period for class {class_id} in week {week}")

    logger.info(f"Scheduled class {class_id}: {theory_scheduled}/{theory_periods} theory, {practical_scheduled}/{practical_periods} practical")
    return schedule

# A* optimization node
class ScheduleNode:
    def __init__(self, class_id, assigned_slots, cost, heuristic):
        self.class_id = class_id
        self.assigned_slots = assigned_slots
        self.cost = cost
        self.heuristic = heuristic
        self.total_cost = cost + heuristic

    def __lt__(self, other):
        return self.total_cost < other.total_cost

# A* optimization with 5-slot limit per day per class
def optimize_with_a_star(class_info, initial_schedule, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock, max_iterations=100):
    class_id = class_info['class_id']
    subject_id = class_info['subject_id']
    teacher_id = class_info['teacher_id']
    total_students = class_info['max_students']
    start_date = class_info['start_date']
    period_length = class_info['period_length']
    theory_periods = class_info['theory_periods']
    practical_periods = class_info['practical_periods']
    total_periods = theory_periods + practical_periods

    if len(initial_schedule) == total_periods:
        return initial_schedule

    logger.warning(f"Initial schedule for class {class_id} has insufficient periods, using A* to reschedule")
    initial_slots = initial_schedule

    with lock:
        for s in initial_slots:
            slot = (s['week'], s['day'], s['slot'])
            if slot not in room_usage:
                room_usage[slot] = set()
            if slot not in teacher_usage:
                teacher_usage[slot] = set()
            if slot not in class_usage:
                class_usage[slot] = set()
            slot_usage[slot] = True
            room_usage[slot].add(s['room_id'])
            teacher_usage[slot].add(s['teacher_id'])
            class_usage[slot].add(s['class_id'])

    open_set = []
    closed_set = set()
    heappush(open_set, ScheduleNode(class_id, initial_slots, 0, total_periods - len(initial_slots)))
    best_schedule = initial_slots
    min_cost = float('inf')
    iterations = 0

    num_weeks = calculate_weeks(start_date, class_info['end_date'])
    teacher_slots = [(w, d, s) for w in range(1, num_weeks + 1) for d, s in global_slots if (d, s) in teacher_preferences.get(teacher_id, set())]
    daily_slot_count = defaultdict(int)  # Theo dõi số slot mỗi ngày cho class_id

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
            logger.warning(f"No {slot_type} rooms for class {class_id} in A*")
            break

        for slot in teacher_slots:
            day_key = (slot[0], slot[1])
            current_day_slots = [s for s in current_node.assigned_slots if (s['week'], s['day']) == day_key]
            with lock:
                if slot in slot_usage or len(current_day_slots) >= 5:
                    continue
                day_key = (slot[0], slot[1])
                if day_key in daily_slot_count and daily_slot_count[day_key] >= 5:
                    continue

                if slot not in room_usage:
                    room_usage[slot] = set()
                if slot not in teacher_usage:
                    teacher_usage[slot] = set()
                if slot not in class_usage:
                    class_usage[slot] = set()

                room = next((r for r in suitable_rooms if r not in room_usage[slot]), None)
                if not room:
                    continue
                if teacher_id in teacher_usage[slot] or class_id in class_usage[slot]:
                    continue

                start_time, end_time = calculate_times(slot[0], slot[1], slot[2], start_date)
                new_slots = current_node.assigned_slots.copy()
                new_slots.append({
                    'week': slot[0], 'day': slot[1], 'slot': slot[2], 'room_id': room, 'type': slot_type,
                    'class_id': class_id, 'subject_id': subject_id, 'teacher_id': teacher_id,
                    'period': period_length, 'start_time': start_time, 'end_time': end_time, 'status': 'ACTIVE'
                })
                slot_usage[slot] = True
                room_usage[slot].add(room)
                teacher_usage[slot].add(teacher_id)
                class_usage[slot].add(class_id)
                daily_slot_count[day_key] += 1

                new_cost = current_node.cost + (100 - class_info['priority']) + periods_left * 0.1
                new_heuristic = periods_left
                heappush(open_set, ScheduleNode(class_id, new_slots, new_cost, new_heuristic))

                del slot_usage[slot]
                if room in room_usage[slot]:
                    room_usage[slot].remove(room)
                if teacher_id in teacher_usage[slot]:
                    teacher_usage[slot].remove(teacher_id)
                if class_id in class_usage[slot]:
                    class_usage[slot].remove(class_id)
                daily_slot_count[day_key] -= 1
                iterations += 1

    return best_schedule

# Process each class in multiprocessing
def schedule_class(args):
    class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock = args
    class_id = class_info['class_id']
    initial_schedule = greedy_schedule(class_info, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock)
    optimized_schedule = optimize_with_a_star(class_info, initial_schedule, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock)
    return class_id, optimized_schedule

# Main scheduling function
def main():
    try:
        classes_df, rooms_df, preferences_df = fetch_data()
        teacher_preferences = preferences_df.groupby('teacher_id').apply(
            lambda x: set(zip(x['day_of_week'], x['slot'])), include_groups=False
        ).to_dict()
        global_slots = set((row['day_of_week'], row['slot']) for _, row in preferences_df.iterrows())

        room_lookup = preprocess_rooms(rooms_df)

        manager = Manager()
        slot_usage = manager.dict()
        room_usage = manager.dict()
        teacher_usage = manager.dict()
        class_usage = manager.dict()
        lock = manager.Lock()

        schedule = {}
        with Pool(processes=min(4, len(classes_df))) as pool:
            args_list = [(row, global_slots, teacher_preferences, rooms_df, room_lookup, slot_usage, room_usage, teacher_usage, class_usage, lock) for _, row in classes_df.iterrows()]
            results = pool.map(schedule_class, args_list)
            for class_id, class_schedule in results:
                schedule[class_id] = class_schedule

        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM tbl_schedule WHERE semester_id = %s", (SEMESTER_ID,))
        unique_schedule = {}
        for class_id, slots in schedule.items():
            for s in slots:
                key = (s['room_id'], SEMESTER_ID, s['week'], s['day'], s['slot'])
                if key not in unique_schedule or s['class_id'] not in [x['class_id'] for x in unique_schedule.get(key, [])]:
                    if key not in unique_schedule:
                        unique_schedule[key] = []
                    unique_schedule[key].append(s)

        values = [
            (
                SEMESTER_ID,
                s['class_id'],
                s['subject_id'],
                s['teacher_id'],
                s['week'],
                s['day'],
                s['slot'],
                s['room_id'],
                s['period'],
                s['start_time'],
                s['end_time'],
                s['status']
            )
            for slots in unique_schedule.values()
            for s in slots
        ]
        logger.info(f"Scheduled and saved {len(values)} periods after deduplication.")
        if values:
            cursor.executemany(
                """
                INSERT INTO tbl_schedule (semester_id, class_id, subject_id, teacher_id, week, day_of_week, slot, room_id, period, start_time, end_time, status)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT ON CONSTRAINT unique_room_slot DO NOTHING
                """,
                values
            )
            conn.commit()
        else:
            logger.warning("No schedule to save into tbl_schedule.")
        cursor.close()
        conn.close()
    except Exception as e:
        logger.error(f"Error in main: {e}")
        raise

if platform.system() == "Emscripten":
    import asyncio
    asyncio.ensure_future(main())
else:
    if __name__ == "__main__":
        main()