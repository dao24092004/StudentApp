from heapq import heappush, heappop
import psycopg2
import sys
from collections import defaultdict
from datetime import datetime, date, timedelta

# Định nghĩa lớp Lecture để lưu thông tin lớp học
class Lecture:
    def __init__(self, id, lecturer_id, room, priority, duration_in_periods, time_windows, shift=None, subject_id=None):
        self.id = id
        self.lecturer_id = lecturer_id
        self.room = room
        self.priority = priority
        self.duration_in_periods = duration_in_periods
        self.time_windows = time_windows
        self.shift = shift
        self.subject_id = subject_id

# Định nghĩa lớp State để lưu trạng thái trong thuật toán A*
class State:
    def __init__(self, P, t, schedule):
        self.P = set(P)
        self.t = t
        self.schedule = schedule

    def __lt__(self, other):
        return len(self.P) < len(other.P)

# Kết nối với cơ sở dữ liệu PostgreSQL
def connect_db():
    return psycopg2.connect(dbname="student_db", user="studentApp", password="1234$", host="localhost", port="5432")

# Lấy danh sách lớp học từ cơ sở dữ liệu
def fetch_lectures(conn, semester_id):
    with conn.cursor() as cur:
        cur.execute("""
            SELECT c.id, t.teacher_code, c.classroom, c.priority, 
                   ((c.end_date - c.start_date) / 7 * 2) AS duration_in_periods,
                   array_agg(json_build_object('day', tw.day_of_week, 'slot', tw.slot)) AS time_windows,
                   c.shift, sub.id AS subject_id
            FROM tbl_class c
            JOIN tbl_teacher t ON c.teacher_id = t.id
            JOIN tbl_subject sub ON c.subject_id = sub.id
            LEFT JOIN tbl_time_window tw ON c.id = tw.id_class
            WHERE sub.semester_id = %s AND c.teacher_id IS NOT NULL
            GROUP BY c.id, t.teacher_code, c.classroom, c.priority, c.start_date, c.end_date, c.shift, sub.id
        """, (semester_id,))
        lectures = []
        for row in cur.fetchall():
            time_windows = [(tw['day'], tw['slot']) for tw in row[5] if tw]
            if row[6] == 'Morning':
                time_windows = [(day, slot) for day, slot in time_windows if slot == 1]
            elif row[6] == 'Afternoon':
                time_windows = [(day, slot) for day, slot in time_windows if slot == 2]
            if time_windows:
                lectures.append(Lecture(
                    id=row[0], lecturer_id=row[1], room=row[2], priority=row[3] or 10,
                    duration_in_periods=row[4], time_windows=time_windows,
                    shift=row[6], subject_id=row[7]
                ))
        return lectures

# Lấy thông tin tài nguyên (giáo viên, phòng học)
def fetch_resources(conn):
    resources = {'lecturers': {}, 'rooms': {}}
    with conn.cursor() as cur:
        cur.execute("SELECT teacher_code FROM tbl_teacher")
        resources['lecturers'] = {row[0]: {'schedule': {}} for row in cur.fetchall()}
        cur.execute("SELECT DISTINCT classroom FROM tbl_class WHERE classroom IS NOT NULL")
        resources['rooms'] = {row[0]: 50 for row in cur.fetchall()}
    return resources

# Lấy thông tin kỳ học (ngày bắt đầu, kết thúc)
def get_semester_info(conn, semester_id):
    with conn.cursor() as cur:
        cur.execute("""
            SELECT start_date, end_date
            FROM tbl_semester
            WHERE id = %s
        """, (semester_id,))
        return cur.fetchone()

# Thuật toán A* để xếp lịch
def a_star(lectures, resources, days, slots, start_date, end_date, conn):
    lectures_dict = {lec.id: lec for lec in lectures}
    initial_t = {res_type: {res: resources[res_type][res]['schedule'] if res_type == 'lecturers' else {} for res in resources[res_type]} for res_type in resources}
    initial_state = State(set(lectures_dict.keys()), initial_t, [])
    Q = [(0, initial_state, 0)]
    W = set()
    best_solution = []
    best_prize = 0

    weeks = ((end_date - start_date).days // 7) + 1
    day_subject_map = defaultdict(set)

    while Q:
        f, state, g = heappop(Q)
        state_key = frozenset(state.P)
        if state_key in W:
            continue
        W.add(state_key)

        if not state.P:
            if g > best_prize:
                best_prize = g
                best_solution = state.schedule
            continue

        for lec_id in sorted(state.P, key=lambda x: lectures_dict[x].priority, reverse=True):
            lecture = lectures_dict[lec_id]

            for day, slot in lecture.time_windows:
                for week in range(1, weeks + 1):
                    conflict = False
                    for res_type, res_id in [('lecturers', lecture.lecturer_id), ('rooms', lecture.room)]:
                        if state.t[res_type][res_id].get((day, slot, week), False):
                            conflict = True
                            break
                    if conflict:
                        continue

                    day_key = (day, slot, week)
                    if len(day_subject_map[day_key]) >= 2 and lecture.subject_id not in day_subject_map[day_key]:
                        continue

                    lecturer_slots = sum(1 for ts in state.t['lecturers'][lecture.lecturer_id] 
                                       if ts[0] == day and ts[2] == week)
                    if lecturer_slots >= 2:
                        continue

                    P_new = state.P - {lec_id}
                    t_new = {res_type: {res: dict(state.t[res_type][res]) for res in state.t[res_type]} 
                            for res_type in state.t}
                    for res_type, res_id in [('lecturers', lecture.lecturer_id), ('rooms', lecture.room)]:
                        t_new[res_type][res_id][(day, slot, week)] = True
                    schedule_new = state.schedule + [(lecture.id, lecture.subject_id, day, slot, week)]
                    day_subject_map[day_key].add(lecture.subject_id)
                    g_new = g + lecture.priority
                    h_new = sum(lectures_dict[lec].priority for lec in P_new)
                    heappush(Q, (-(g_new + h_new), State(P_new, t_new, schedule_new), g_new))
    return best_solution, day_subject_map

# Lưu lịch học vào cơ sở dữ liệu
def save_schedule(conn, solution, day_subject_map, semester_start_date):
    period_times = {
        1: {1: ('07:00:00', '07:45:00'), 2: ('12:30:00', '13:15:00')},
        2: {1: ('08:00:00', '08:45:00'), 2: ('13:15:00', '14:00:00')},
        3: {1: ('09:00:00', '09:45:00'), 2: ('14:00:00', '14:45:00')},
        4: {1: ('10:00:00', '10:45:00'), 2: ('14:45:00', '15:30:00')},
        5: {1: ('11:00:00', '11:45:00'), 2: ('15:30:00', '16:15:00')}
    }

    with conn.cursor() as cur:
        cur.execute("DELETE FROM tbl_schedule")
        for class_id, subject_id, day, slot, week in solution:
            day_key = (day, slot, week)
            subjects_in_day = list(day_subject_map[day_key])
            if len(subjects_in_day) == 1:
                for period in range(1, 6):
                    start_time, end_time = period_times[period][slot]
                    days_since_start = (week - 1) * 7 + ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].index(day)
                    actual_date = (semester_start_date + timedelta(days=days_since_start)).strftime('%Y-%m-%d')
                    start_datetime = f"{actual_date} {start_time}"
                    end_datetime = f"{actual_date} {end_time}"
                    cur.execute("""
                        INSERT INTO tbl_schedule (class_id, subject_id, day_of_week, slot, period, start_time, end_time)
                        VALUES (%s, %s, %s, %s, %s, %s, %s)
                    """, (class_id, subject_id, day, slot, period, start_datetime, end_datetime))
            else:
                first_subject = subjects_in_day[0]
                second_subject = subjects_in_day[1]
                subject_to_periods = {first_subject: [1, 2, 3], second_subject: [4, 5]}
                for period in range(1, 6):
                    assigned_subject = first_subject if period in subject_to_periods[first_subject] else second_subject
                    if assigned_subject != subject_id:
                        continue
                    start_time, end_time = period_times[period][slot]
                    days_since_start = (week - 1) * 7 + ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].index(day)
                    actual_date = (semester_start_date + timedelta(days=days_since_start)).strftime('%Y-%m-%d')
                    start_datetime = f"{actual_date} {start_time}"
                    end_datetime = f"{actual_date} {end_time}"
                    cur.execute("""
                        INSERT INTO tbl_schedule (class_id, subject_id, day_of_week, slot, period, start_time, end_time)
                        VALUES (%s, %s, %s, %s, %s, %s, %s)
                    """, (class_id, assigned_subject, day, slot, period, start_datetime, end_datetime))
        conn.commit()

# Tạo lịch học
def generate_schedule(semester_id):
    conn = connect_db()
    semester_info = get_semester_info(conn, semester_id)
    if not semester_info:
        raise ValueError("Semester not found")
    start_date, end_date = semester_info

    lectures = fetch_lectures(conn, semester_id)
    resources = fetch_resources(conn)
    days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    slots = [1, 2]
    solution, day_subject_map = a_star(lectures, resources, days, slots, start_date, end_date, conn)
    save_schedule(conn, solution, day_subject_map, start_date)
    conn.close()

# Hàm chính để xử lý các hành động
def main():
    if len(sys.argv) < 2:
        print("Error: Action is required")
        sys.exit(1)

    action = sys.argv[1]
    supported_actions = ['generate']

    if action not in supported_actions:
        print(f"Invalid action. Supported actions: {', '.join(supported_actions)}")
        sys.exit(1)

    if action == 'generate':
        if len(sys.argv) != 3:
            print("Error: semester_id is required for 'generate' action")
            sys.exit(1)
        semester_id = int(sys.argv[2])
        generate_schedule(semester_id)

if __name__ == "__main__":
    main()