


INSERT INTO tbl_class (id, class_code, class_name, teacher_id, subject_id, class_group_id, semester_id, max_students, start_date, end_date, room_type, shift, priority, period_length, theory_periods, practical_periods, class_duration) VALUES
(1, 'THDC_01', 'Lớp Tin học đại cương 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV001'), 1, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(2, 'THDC_02', 'Lớp Tin học đại cương 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV009'), 1, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM2'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(3, 'THDC_03', 'Lớp Tin học đại cương 3', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV010'), 1, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM3'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(4, 'OOP_01', 'Lớp Lập trình hướng đối tượng 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV003'), 2, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM1'), 1, 50, '2025-09-01', '2025-12-15', 'lab', 'Any', 10, 1, 25, 15, 15),
(5, 'OOP_02', 'Lớp Lập trình hướng đối tượng 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV003'), 2, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM2'), 1, 50, '2025-09-01', '2025-12-15', 'lab', 'Any', 10, 1, 25, 15, 15),
(6, 'NLP_01', 'Lớp Ngôn ngữ lập trình 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV004'), 3, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(7, 'NLP_02', 'Lớp Ngôn ngữ lập trình 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV004'), 3, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM2'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(8, 'PLDC_01', 'Lớp Pháp luật đại cương 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV002'), 4, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 15, 5, 15),
(9, 'PLDC_02', 'Lớp Pháp luật đại cương 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV002'), 4, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM2'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 15, 5, 15),
(10, 'TC_01', 'Lớp Toán cao cấp 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV007'), 5, (SELECT id FROM tbl_class_group WHERE group_code = 'KTDT1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 25, 15, 15),
(11, 'TC_02', 'Lớp Toán cao cấp 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV007'), 5, (SELECT id FROM tbl_class_group WHERE group_code = 'KTDT1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 25, 15, 15),
(12, 'THML_01', 'Lớp Triết học Mác-Lênin 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV011'), 6, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(13, 'THML_02', 'Lớp Triết học Mác-Lênin 2', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV012'), 6, (SELECT id FROM tbl_class_group WHERE group_code = 'CNPM2'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(14, 'VLDC_01', 'Lớp Vật lý đại cương 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV008'), 7, (SELECT id FROM tbl_class_group WHERE group_code = 'KTDT1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 20, 10, 15),
(15, 'LSD_01', 'Lớp Lịch sử Đảng 1', (SELECT id FROM tbl_teacher WHERE teacher_code = 'GV005'), 8, (SELECT id FROM tbl_class_group WHERE group_code = 'KTDT1'), 1, 50, '2025-09-01', '2025-12-15', 'theory', 'Any', 10, 1, 15, 5, 15)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tbl_room (room_code, room_name, room_type, capacity, dept_id) VALUES
('A101', 'Phòng máy A101', 'lab', 50, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('A102', 'Phòng máy A102', 'lab', 50, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('A103', 'Phòng máy A103', 'lab', 40, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('B201', 'Phòng lý thuyết B201', 'theory', 60, NULL),
('B202', 'Phòng lý thuyết B202', 'theory', 60, NULL),
('B203', 'Phòng lý thuyết B203', 'theory', 70, NULL),
('C301', 'Phòng thực hành C301', 'lab', 40, (SELECT id FROM tbl_department WHERE dept_code = 'KTDT')),
('C302', 'Phòng thực hành C302', 'lab', 40, (SELECT id FROM tbl_department WHERE dept_code = 'KTDT')),
('C303', 'Phòng thực hành C303', 'lab', 50, (SELECT id FROM tbl_department WHERE dept_code = 'CK')),
('D401', 'Sân thể dục D401', 'lab', 30, (SELECT id FROM tbl_department WHERE dept_code = 'TD')),
('D402', 'Sân thể dục D402', 'lab', 30, (SELECT id FROM tbl_department WHERE dept_code = 'TD'));

INSERT INTO tbl_room (room_code, room_name, room_type, capacity, dept_id) VALUES
('B204', 'Phòng lý thuyết B204', 'theory', 60, NULL),
('B205', 'Phòng lý thuyết B205', 'theory', 70, NULL),
('B206', 'Phòng lý thuyết B206', 'theory', 80, NULL),
('B207', 'Phòng lý thuyết B207', 'theory', 60, NULL),
('B208', 'Phòng lý thuyết B208', 'theory', 70, NULL),
('B209', 'Phòng lý thuyết B209', 'theory', 80, NULL),
('B210', 'Phòng lý thuyết B210', 'theory', 100, NULL),
('A104', 'Phòng máy A104', 'lab', 40, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('A105', 'Phòng máy A105', 'lab', 50, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('A106', 'Phòng máy A106', 'lab', 60, (SELECT id FROM tbl_department WHERE dept_code = 'CNTT')),
('C304', 'Phòng thực hành C304', 'lab', 40, (SELECT id FROM tbl_department WHERE dept_code = 'KTDT')),
('C305', 'Phòng thực hành C305', 'lab', 50, (SELECT id FROM tbl_department WHERE dept_code = 'CK')),
('C306', 'Phòng thực hành C306', 'lab', 40, NULL),
('E101', 'Phòng thực hành E101', 'lab', 50, NULL),
('E102', 'Phòng thực hành E102', 'lab', 60, NULL);


INSERT INTO tbl_teacher_preference (teacher_id, week, day_of_week, slot) VALUES
(1, 1, 'Mon', 1), (1, 1, 'Mon', 2), (1, 1, 'Mon', 3), (1, 1, 'Mon', 4),
(1, 1, 'Wed', 1), (1, 1, 'Wed', 2), (1, 1, 'Wed', 3), (1, 1, 'Wed', 4),
(1, 1, 'Sat', 1), (1, 1, 'Sat', 2), (1, 1, 'Sun', 1), (1, 1, 'Sun', 2),
(2, 1, 'Tue', 1), (2, 1, 'Tue', 2), (2, 1, 'Tue', 3), (2, 1, 'Tue', 4),
(2, 1, 'Thu', 1), (2, 1, 'Thu', 2), (2, 1, 'Thu', 3), (2, 1, 'Thu', 4),
(2, 1, 'Sat', 1), (2, 1, 'Sat', 2), (2, 1, 'Sun', 1), (2, 1, 'Sun', 2),
(3, 1, 'Mon', 5), (3, 1, 'Mon', 6), (3, 1, 'Mon', 7), (3, 1, 'Mon', 8),
(3, 1, 'Fri', 5), (3, 1, 'Fri', 6), (3, 1, 'Fri', 7), (3, 1, 'Fri', 8),
(3, 1, 'Sat', 3), (3, 1, 'Sat', 4), (3, 1, 'Sun', 3), (3, 1, 'Sun', 4),
(4, 1, 'Wed', 1), (4, 1, 'Wed', 2), (4, 1, 'Wed', 3), (4, 1, 'Wed', 4),
(4, 1, 'Fri', 1), (4, 1, 'Fri', 2), (4, 1, 'Fri', 3), (4, 1, 'Fri', 4),
(4, 1, 'Sat', 1), (4, 1, 'Sat', 2), (4, 1, 'Sun', 1), (4, 1, 'Sun', 2),
(5, 1, 'Tue', 5), (5, 1, 'Tue', 6), (5, 1, 'Tue', 7), (5, 1, 'Tue', 8),
(5, 1, 'Thu', 5), (5, 1, 'Thu', 6), (5, 1, 'Thu', 7), (5, 1, 'Thu', 8),
(5, 1, 'Sat', 3), (5, 1, 'Sat', 4), (5, 1, 'Sun', 3), (5, 1, 'Sun', 4),
(6, 1, 'Mon', 5), (6, 1, 'Mon', 6), (6, 1, 'Mon', 7), (6, 1, 'Mon', 8),
(6, 1, 'Wed', 5), (6, 1, 'Wed', 6), (6, 1, 'Wed', 7), (6, 1, 'Wed', 8),
(6, 1, 'Sat', 5), (6, 1, 'Sat', 6), (6, 1, 'Sun', 5), (6, 1, 'Sun', 6),
(7, 1, 'Tue', 1), (7, 1, 'Tue', 2), (7, 1, 'Tue', 3), (7, 1, 'Tue', 4),
(7, 1, 'Thu', 1), (7, 1, 'Thu', 2), (7, 1, 'Thu', 3), (7, 1, 'Thu', 4),
(7, 1, 'Sat', 1), (7, 1, 'Sat', 2), (7, 1, 'Sun', 1), (7, 1, 'Sun', 2),
(8, 1, 'Mon', 1), (8, 1, 'Mon', 2), (8, 1, 'Mon', 3), (8, 1, 'Mon', 4),
(8, 1, 'Fri', 1), (8, 1, 'Fri', 2), (8, 1, 'Fri', 3), (8, 1, 'Fri', 4),
(8, 1, 'Sat', 1), (8, 1, 'Sat', 2), (8, 1, 'Sun', 1), (8, 1, 'Sun', 2),
(9, 1, 'Wed', 1), (9, 1, 'Wed', 2), (9, 1, 'Wed', 3), (9, 1, 'Wed', 4),
(9, 1, 'Fri', 1), (9, 1, 'Fri', 2), (9, 1, 'Fri', 3), (9, 1, 'Fri', 4),
(9, 1, 'Sat', 1), (9, 1, 'Sat', 2), (9, 1, 'Sun', 1), (9, 1, 'Sun', 2),
(10, 1, 'Mon', 5), (10, 1, 'Mon', 6), (10, 1, 'Mon', 7), (10, 1, 'Mon', 8),
(10, 1, 'Thu', 5), (10, 1, 'Thu', 6), (10, 1, 'Thu', 7), (10, 1, 'Thu', 8),
(10, 1, 'Sat', 3), (10, 1, 'Sat', 4), (10, 1, 'Sun', 3), (10, 1, 'Sun', 4),
(11, 1, 'Mon', 5), (11, 1, 'Mon', 6), (11, 1, 'Mon', 7), (11, 1, 'Mon', 8),
(11, 1, 'Wed', 5), (11, 1, 'Wed', 6), (11, 1, 'Wed', 7), (11, 1, 'Wed', 8),
(11, 1, 'Sat', 5), (11, 1, 'Sat', 6), (11, 1, 'Sun', 5), (11, 1, 'Sun', 6),
(12, 1, 'Tue', 1), (12, 1, 'Tue', 2), (12, 1, 'Tue', 3), (12, 1, 'Tue', 4),
(12, 1, 'Thu', 1), (12, 1, 'Thu', 2), (12, 1, 'Thu', 3), (12, 1, 'Thu', 4),
(12, 1, 'Sat', 1), (12, 1, 'Sat', 2), (12, 1, 'Sun', 1), (12, 1, 'Sun', 2),
(13, 1, 'Mon', 1), (13, 1, 'Mon', 2), (13, 1, 'Mon', 3), (13, 1, 'Mon', 4),
(13, 1, 'Fri', 1), (13, 1, 'Fri', 2), (13, 1, 'Fri', 3), (13, 1, 'Fri', 4),
(13, 1, 'Sat', 1), (13, 1, 'Sat', 2), (13, 1, 'Sun', 1), (13, 1, 'Sun', 2),
(14, 1, 'Wed', 5), (14, 1, 'Wed', 6), (14, 1, 'Wed', 7), (14, 1, 'Wed', 8),
(14, 1, 'Fri', 5), (14, 1, 'Fri', 6), (14, 1, 'Fri', 7), (14, 1, 'Fri', 8),
(14, 1, 'Sat', 3), (14, 1, 'Sat', 4), (14, 1, 'Sun', 3), (14, 1, 'Sun', 4),
(15, 1, 'Tue', 5), (15, 1, 'Tue', 6), (15, 1, 'Tue', 7), (15, 1, 'Tue', 8),
(15, 1, 'Thu', 5), (15, 1, 'Thu', 6), (15, 1, 'Thu', 7), (15, 1, 'Thu', 8),
(15, 1, 'Sat', 3), (15, 1, 'Sat', 4), (15, 1, 'Sun', 3), (15, 1, 'Sun', 4)
ON CONFLICT (teacher_id, week, day_of_week, slot) DO NOTHING;


CREATE TABLE tbl_global_time_slots (
    id SERIAL PRIMARY KEY,
    day_of_week VARCHAR(10) NOT NULL,
    slot INT NOT NULL,
    CONSTRAINT unique_slot UNIQUE (day_of_week, slot)
);

INSERT INTO tbl_global_time_slots (day_of_week, slot)
SELECT day_of_week, slot
FROM (
    SELECT day_of_week, slot
    FROM unnest(ARRAY['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']) AS day_of_week
    CROSS JOIN generate_series(1, 7) AS slot
) t
ON CONFLICT (day_of_week, slot) DO NOTHING;

INSERT INTO tbl_time_window (id_class, week, day_of_week, period) VALUES
(1, 1, 'Mon', 1), (1, 1, 'Mon', 2), (1, 1, 'Mon', 3), (1, 1, 'Mon', 4), (1, 1, 'Mon', 5), (1, 1, 'Mon', 6), (1, 1, 'Mon', 7),
(1, 1, 'Tue', 1), (1, 1, 'Tue', 2), (1, 1, 'Tue', 3), (1, 1, 'Tue', 4), (1, 1, 'Tue', 5), (1, 1, 'Tue', 6), (1, 1, 'Tue', 7),
(1, 1, 'Wed', 1), (1, 1, 'Wed', 2), (1, 1, 'Wed', 3), (1, 1, 'Wed', 4), (1, 1, 'Wed', 5), (1, 1, 'Wed', 6), (1, 1, 'Wed', 7),
(1, 1, 'Thu', 1), (1, 1, 'Thu', 2), (1, 1, 'Thu', 3), (1, 1, 'Thu', 4), (1, 1, 'Thu', 5), (1, 1, 'Thu', 6), (1, 1, 'Thu', 7),
(1, 1, 'Fri', 1), (1, 1, 'Fri', 2), (1, 1, 'Fri', 3), (1, 1, 'Fri', 4), (1, 1, 'Fri', 5), (1, 1, 'Fri', 6), (1, 1, 'Fri', 7),
(1, 1, 'Sat', 1), (1, 1, 'Sat', 2), (1, 1, 'Sat', 3),
(1, 1, 'Sun', 1), (1, 1, 'Sun', 2), (1, 1, 'Sun', 3),

(2, 1, 'Mon', 1), (2, 1, 'Mon', 2), (2, 1, 'Mon', 3), (2, 1, 'Mon', 4), (2, 1, 'Mon', 5), (2, 1, 'Mon', 6), (2, 1, 'Mon', 7),
(2, 1, 'Tue', 1), (2, 1, 'Tue', 2), (2, 1, 'Tue', 3), (2, 1, 'Tue', 4), (2, 1, 'Tue', 5), (2, 1, 'Tue', 6), (2, 1, 'Tue', 7),
(2, 1, 'Wed', 1), (2, 1, 'Wed', 2), (2, 1, 'Wed', 3), (2, 1, 'Wed', 4), (2, 1, 'Wed', 5), (2, 1, 'Wed', 6), (2, 1, 'Wed', 7),
(2, 1, 'Thu', 1), (2, 1, 'Thu', 2), (2, 1, 'Thu', 3), (2, 1, 'Thu', 4), (2, 1, 'Thu', 5), (2, 1, 'Thu', 6), (2, 1, 'Thu', 7),
(2, 1, 'Fri', 1), (2, 1, 'Fri', 2), (2, 1, 'Fri', 3), (2, 1, 'Fri', 4), (2, 1, 'Fri', 5), (2, 1, 'Fri', 6), (2, 1, 'Fri', 7),
(2, 1, 'Sat', 1), (2, 1, 'Sat', 2), (2, 1, 'Sat', 3),
(2, 1, 'Sun', 1), (2, 1, 'Sun', 2), (2, 1, 'Sun', 3),

(3, 1, 'Mon', 1), (3, 1, 'Mon', 2), (3, 1, 'Mon', 3), (3, 1, 'Mon', 4), (3, 1, 'Mon', 5), (3, 1, 'Mon', 6), (3, 1, 'Mon', 7),
(3, 1, 'Tue', 1), (3, 1, 'Tue', 2), (3, 1, 'Tue', 3), (3, 1, 'Tue', 4), (3, 1, 'Tue', 5), (3, 1, 'Tue', 6), (3, 1, 'Tue', 7),
(3, 1, 'Wed', 1), (3, 1, 'Wed', 2), (3, 1, 'Wed', 3), (3, 1, 'Wed', 4), (3, 1, 'Wed', 5), (3, 1, 'Wed', 6), (3, 1, 'Wed', 7),
(3, 1, 'Thu', 1), (3, 1, 'Thu', 2), (3, 1, 'Thu', 3), (3, 1, 'Thu', 4), (3, 1, 'Thu', 5), (3, 1, 'Thu', 6), (3, 1, 'Thu', 7),
(3, 1, 'Fri', 1), (3, 1, 'Fri', 2), (3, 1, 'Fri', 3), (3, 1, 'Fri', 4), (3, 1, 'Fri', 5), (3, 1, 'Fri', 6), (3, 1, 'Fri', 7),
(3, 1, 'Sat', 1), (3, 1, 'Sat', 2), (3, 1, 'Sat', 3),
(3, 1, 'Sun', 1), (3, 1, 'Sun', 2), (3, 1, 'Sun', 3),

(4, 1, 'Mon', 1), (4, 1, 'Mon', 2), (4, 1, 'Mon', 3), (4, 1, 'Mon', 4), (4, 1, 'Mon', 5), (4, 1, 'Mon', 6), (4, 1, 'Mon', 7),
(4, 1, 'Tue', 1), (4, 1, 'Tue', 2), (4, 1, 'Tue', 3), (4, 1, 'Tue', 4), (4, 1, 'Tue', 5), (4, 1, 'Tue', 6), (4, 1, 'Tue', 7),
(4, 1, 'Wed', 1), (4, 1, 'Wed', 2), (4, 1, 'Wed', 3), (4, 1, 'Wed', 4), (4, 1, 'Wed', 5), (4, 1, 'Wed', 6), (4, 1, 'Wed', 7),
(4, 1, 'Thu', 1), (4, 1, 'Thu', 2), (4, 1, 'Thu', 3), (4, 1, 'Thu', 4), (4, 1, 'Thu', 5), (4, 1, 'Thu', 6), (4, 1, 'Thu', 7),
(4, 1, 'Fri', 1), (4, 1, 'Fri', 2), (4, 1, 'Fri', 3), (4, 1, 'Fri', 4), (4, 1, 'Fri', 5), (4, 1, 'Fri', 6), (4, 1, 'Fri', 7),
(4, 1, 'Sat', 1), (4, 1, 'Sat', 2), (4, 1, 'Sat', 3),
(4, 1, 'Sun', 1), (4, 1, 'Sun', 2), (4, 1, 'Sun', 3),

(5, 1, 'Mon', 1), (5, 1, 'Mon', 2), (5, 1, 'Mon', 3), (5, 1, 'Mon', 4), (5, 1, 'Mon', 5), (5, 1, 'Mon', 6), (5, 1, 'Mon', 7),
(5, 1, 'Tue', 1), (5, 1, 'Tue', 2), (5, 1, 'Tue', 3), (5, 1, 'Tue', 4), (5, 1, 'Tue', 5), (5, 1, 'Tue', 6), (5, 1, 'Tue', 7),
(5, 1, 'Wed', 1), (5, 1, 'Wed', 2), (5, 1, 'Wed', 3), (5, 1, 'Wed', 4), (5, 1, 'Wed', 5), (5, 1, 'Wed', 6), (5, 1, 'Wed', 7),
(5, 1, 'Thu', 1), (5, 1, 'Thu', 2), (5, 1, 'Thu', 3), (5, 1, 'Thu', 4), (5, 1, 'Thu', 5), (5, 1, 'Thu', 6), (5, 1, 'Thu', 7),
(5, 1, 'Fri', 1), (5, 1, 'Fri', 2), (5, 1, 'Fri', 3), (5, 1, 'Fri', 4), (5, 1, 'Fri', 5), (5, 1, 'Fri', 6), (5, 1, 'Fri', 7),
(5, 1, 'Sat', 1), (5, 1, 'Sat', 2), (5, 1, 'Sat', 3),
(5, 1, 'Sun', 1), (5, 1, 'Sun', 2), (5, 1, 'Sun', 3),

(6, 1, 'Mon', 1), (6, 1, 'Mon', 2), (6, 1, 'Mon', 3), (6, 1, 'Mon', 4), (6, 1, 'Mon', 5), (6, 1, 'Mon', 6), (6, 1, 'Mon', 7),
(6, 1, 'Tue', 1), (6, 1, 'Tue', 2), (6, 1, 'Tue', 3), (6, 1, 'Tue', 4), (6, 1, 'Tue', 5), (6, 1, 'Tue', 6), (6, 1, 'Tue', 7),
(6, 1, 'Wed', 1), (6, 1, 'Wed', 2), (6, 1, 'Wed', 3), (6, 1, 'Wed', 4), (6, 1, 'Wed', 5), (6, 1, 'Wed', 6), (6, 1, 'Wed', 7),
(6, 1, 'Thu', 1), (6, 1, 'Thu', 2), (6, 1, 'Thu', 3), (6, 1, 'Thu', 4), (6, 1, 'Thu', 5), (6, 1, 'Thu', 6), (6, 1, 'Thu', 7),
(6, 1, 'Fri', 1), (6, 1, 'Fri', 2), (6, 1, 'Fri', 3), (6, 1, 'Fri', 4), (6, 1, 'Fri', 5), (6, 1, 'Fri', 6), (6, 1, 'Fri', 7),
(6, 1, 'Sat', 1), (6, 1, 'Sat', 2), (6, 1, 'Sat', 3),
(6, 1, 'Sun', 1), (6, 1, 'Sun', 2), (6, 1, 'Sun', 3),

(7, 1, 'Mon', 1), (7, 1, 'Mon', 2), (7, 1, 'Mon', 3), (7, 1, 'Mon', 4), (7, 1, 'Mon', 5), (7, 1, 'Mon', 6), (7, 1, 'Mon', 7),
(7, 1, 'Tue', 1), (7, 1, 'Tue', 2), (7, 1, 'Tue', 3), (7, 1, 'Tue', 4), (7, 1, 'Tue', 5), (7, 1, 'Tue', 6), (7, 1, 'Tue', 7),
(7, 1, 'Wed', 1), (7, 1, 'Wed', 2), (7, 1, 'Wed', 3), (7, 1, 'Wed', 4), (7, 1, 'Wed', 5), (7, 1, 'Wed', 6), (7, 1, 'Wed', 7),
(7, 1, 'Thu', 1), (7, 1, 'Thu', 2), (7, 1, 'Thu', 3), (7, 1, 'Thu', 4), (7, 1, 'Thu', 5), (7, 1, 'Thu', 6), (7, 1, 'Thu', 7),
(7, 1, 'Fri', 1), (7, 1, 'Fri', 2), (7, 1, 'Fri', 3), (7, 1, 'Fri', 4), (7, 1, 'Fri', 5), (7, 1, 'Fri', 6), (7, 1, 'Fri', 7),
(7, 1, 'Sat', 1), (7, 1, 'Sat', 2), (7, 1, 'Sat', 3),
(7, 1, 'Sun', 1), (7, 1, 'Sun', 2), (7, 1, 'Sun', 3),

(8, 1, 'Mon', 1), (8, 1, 'Mon', 2), (8, 1, 'Mon', 3), (8, 1, 'Mon', 4), (8, 1, 'Mon', 5), (8, 1, 'Mon', 6), (8, 1, 'Mon', 7),
(8, 1, 'Tue', 1), (8, 1, 'Tue', 2), (8, 1, 'Tue', 3), (8, 1, 'Tue', 4), (8, 1, 'Tue', 5), (8, 1, 'Tue', 6), (8, 1, 'Tue', 7),
(8, 1, 'Wed', 1), (8, 1, 'Wed', 2), (8, 1, 'Wed', 3), (8, 1, 'Wed', 4), (8, 1, 'Wed', 5), (8, 1, 'Wed', 6), (8, 1, 'Wed', 7),
(8, 1, 'Sat', 1), (8, 1, 'Sat', 2), (8, 1, 'Sat', 3),

(9, 1, 'Thu', 1), (9, 1, 'Thu', 2), (9, 1, 'Thu', 3), (9, 1, 'Thu', 4), (9, 1, 'Thu', 5), (9, 1, 'Thu', 6), (9, 1, 'Thu', 7),
(9, 1, 'Fri', 1), (9, 1, 'Fri', 2), (9, 1, 'Fri', 3), (9, 1, 'Fri', 4), (9, 1, 'Fri', 5), (9, 1, 'Fri', 6), (9, 1, 'Fri', 7),
(9, 1, 'Sun', 1), (9, 1, 'Sun', 2), (9, 1, 'Sun', 3),
(9, 1, 'Sat', 1), (9, 1, 'Sat', 2), (9, 1, 'Sat', 3),

(10, 1, 'Mon', 1), (10, 1, 'Mon', 2), (10, 1, 'Mon', 3), (10, 1, 'Mon', 4), (10, 1, 'Mon', 5), (10, 1, 'Mon', 6), (10, 1, 'Mon', 7),
(10, 1, 'Tue', 1), (10, 1, 'Tue', 2), (10, 1, 'Tue', 3), (10, 1, 'Tue', 4), (10, 1, 'Tue', 5), (10, 1, 'Tue', 6), (10, 1, 'Tue', 7),
(10, 1, 'Wed', 1), (10, 1, 'Wed', 2), (10, 1, 'Wed', 3), (10, 1, 'Wed', 4), (10, 1, 'Wed', 5), (10, 1, 'Wed', 6), (10, 1, 'Wed', 7),
(10, 1, 'Thu', 1), (10, 1, 'Thu', 2), (10, 1, 'Thu', 3), (10, 1, 'Thu', 4), (10, 1, 'Thu', 5), (10, 1, 'Thu', 6), (10, 1, 'Thu', 7),
(10, 1, 'Fri', 1), (10, 1, 'Fri', 2), (10, 1, 'Fri', 3), (10, 1, 'Fri', 4), (10, 1, 'Fri', 5), (10, 1, 'Fri', 6), (10, 1, 'Fri', 7),
(10, 1, 'Sat', 1), (10, 1, 'Sat', 2), (10, 1, 'Sat', 3),
(10, 1, 'Sun', 1), (10, 1, 'Sun', 2), (10, 1, 'Sun', 3),

(11, 1, 'Mon', 1), (11, 1, 'Mon', 2), (11, 1, 'Mon', 3), (11, 1, 'Mon', 4), (11, 1, 'Mon', 5), (11, 1, 'Mon', 6), (11, 1, 'Mon', 7),
(11, 1, 'Tue', 1), (11, 1, 'Tue', 2), (11, 1, 'Tue', 3), (11, 1, 'Tue', 4), (11, 1, 'Tue', 5), (11, 1, 'Tue', 6), (11, 1, 'Tue', 7),
(11, 1, 'Wed', 1), (11, 1, 'Wed', 2), (11, 1, 'Wed', 3), (11, 1, 'Wed', 4), (11, 1, 'Wed', 5), (11, 1, 'Wed', 6), (11, 1, 'Wed', 7),
(11, 1, 'Thu', 1), (11, 1, 'Thu', 2), (11, 1, 'Thu', 3), (11, 1, 'Thu', 4), (11, 1, 'Thu', 5), (11, 1, 'Thu', 6), (11, 1, 'Thu', 7),
(11, 1, 'Fri', 1), (11, 1, 'Fri', 2), (11, 1, 'Fri', 3), (11, 1, 'Fri', 4), (11, 1, 'Fri', 5), (11, 1, 'Fri', 6), (11, 1, 'Fri', 7),
(11, 1, 'Sat', 1), (11, 1, 'Sat', 2), (11, 1, 'Sat', 3),
(11, 1, 'Sun', 1), (11, 1, 'Sun', 2), (11, 1, 'Sun', 3),

(12, 1, 'Mon', 1), (12, 1, 'Mon', 2), (12, 1, 'Mon', 3), (12, 1, 'Mon', 4), (12, 1, 'Mon', 5), (12, 1, 'Mon', 6), (12, 1, 'Mon', 7),
(12, 1, 'Tue', 1), (12, 1, 'Tue', 2), (12, 1, 'Tue', 3), (12, 1, 'Tue', 4), (12, 1, 'Tue', 5), (12, 1, 'Tue', 6), (12, 1, 'Tue', 7),
(12, 1, 'Wed', 1), (12, 1, 'Wed', 2), (12, 1, 'Wed', 3), (12, 1, 'Wed', 4), (12, 1, 'Wed', 5), (12, 1, 'Wed', 6), (12, 1, 'Wed', 7),
(12, 1, 'Thu', 1), (12, 1, 'Thu', 2), (12, 1, 'Thu', 3), (12, 1, 'Thu', 4), (12, 1, 'Thu', 5), (12, 1, 'Thu', 6), (12, 1, 'Thu', 7),
(12, 1, 'Fri', 1), (12, 1, 'Fri', 2), (12, 1, 'Fri', 3), (12, 1, 'Fri', 4), (12, 1, 'Fri', 5), (12, 1, 'Fri', 6), (12, 1, 'Fri', 7),
(12, 1, 'Sat', 1), (12, 1, 'Sat', 2), (12, 1, 'Sat', 3),
(12, 1, 'Sun', 1), (12, 1, 'Sun', 2), (12, 1, 'Sun', 3),

(13, 1, 'Mon', 1), (13, 1, 'Mon', 2), (13, 1, 'Mon', 3), (13, 1, 'Mon', 4), (13, 1, 'Mon', 5), (13, 1, 'Mon', 6), (13, 1, 'Mon', 7),
(13, 1, 'Tue', 1), (13, 1, 'Tue', 2), (13, 1, 'Tue', 3), (13, 1, 'Tue', 4), (13, 1, 'Tue', 5), (13, 1, 'Tue', 6), (13, 1, 'Tue', 7),
(13, 1, 'Wed', 1), (13, 1, 'Wed', 2), (13, 1, 'Wed', 3), (13, 1, 'Wed', 4), (13, 1, 'Wed', 5), (13, 1, 'Wed', 6), (13, 1, 'Wed', 7),
(13, 1, 'Thu', 1), (13, 1, 'Thu', 2), (13, 1, 'Thu', 3), (13, 1, 'Thu', 4), (13, 1, 'Thu', 5), (13, 1, 'Thu', 6), (13, 1, 'Thu', 7),
(13, 1, 'Fri', 1), (13, 1, 'Fri', 2), (13, 1, 'Fri', 3), (13, 1, 'Fri', 4), (13, 1, 'Fri', 5), (13, 1, 'Fri', 6), (13, 1, 'Fri', 7),
(13, 1, 'Sat', 1), (13, 1, 'Sat', 2), (13, 1, 'Sat', 3),
(13, 1, 'Sun', 1), (13, 1, 'Sun', 2), (13, 1, 'Sun', 3),

(14, 1, 'Mon', 1), (14, 1, 'Mon', 2), (14, 1, 'Mon', 3), (14, 1, 'Mon', 4), (14, 1, 'Mon', 5), (14, 1, 'Mon', 6), (14, 1, 'Mon', 7),
(14, 1, 'Tue', 1), (14, 1, 'Tue', 2), (14, 1, 'Tue', 3), (14, 1, 'Tue', 4), (14, 1, 'Tue', 5), (14, 1, 'Tue', 6), (14, 1, 'Tue', 7),
(14, 1, 'Wed', 1), (14, 1, 'Wed', 2), (14, 1, 'Wed', 3), (14, 1, 'Wed', 4), (14, 1, 'Wed', 5), (14, 1, 'Wed', 6), (14, 1, 'Wed', 7),
(14, 1, 'Thu', 1), (14, 1, 'Thu', 2), (14, 1, 'Thu', 3), (14, 1, 'Thu', 4), (14, 1, 'Thu', 5), (14, 1, 'Thu', 6), (14, 1, 'Thu', 7),
(14, 1, 'Fri', 1), (14, 1, 'Fri', 2), (14, 1, 'Fri', 3), (14, 1, 'Fri', 4), (14, 1, 'Fri', 5), (14, 1, 'Fri', 6), (14, 1, 'Fri', 7),
(14, 1, 'Sat', 1), (14, 1, 'Sat', 2), (14, 1, 'Sat', 3),
(14, 1, 'Sun', 1), (14, 1, 'Sun', 2), (14, 1, 'Sun', 3),

(15, 1, 'Mon', 1), (15, 1, 'Mon', 2), (15, 1, 'Mon', 3), (15, 1, 'Mon', 4), (15, 1, 'Mon', 5), (15, 1, 'Mon', 6), (15, 1, 'Mon', 7),
(15, 1, 'Tue', 1), (15, 1, 'Tue', 2), (15, 1, 'Tue', 3), (15, 1, 'Tue', 4), (15, 1, 'Tue', 5), (15, 1, 'Tue', 6), (15, 1, 'Tue', 7),
(15, 1, 'Wed', 1), (15, 1, 'Wed', 2), (15, 1, 'Wed', 3), (15, 1, 'Wed', 4), (15, 1, 'Wed', 5), (15, 1, 'Wed', 6), (15, 1, 'Wed', 7),
(15, 1, 'Thu', 1), (15, 1, 'Thu', 2), (15, 1, 'Thu', 3), (15, 1, 'Thu', 4), (15, 1, 'Thu', 5), (15, 1, 'Thu', 6), (15, 1, 'Thu', 7),
(15, 1, 'Fri', 1), (15, 1, 'Fri', 2), (15, 1, 'Fri', 3), (15, 1, 'Fri', 4), (15, 1, 'Fri', 5), (15, 1, 'Fri', 6), (15, 1, 'Fri', 7),
(15, 1, 'Sat', 1), (15, 1, 'Sat', 2), (15, 1, 'Sat', 3), (15, 1, 'Sat', 4), (15, 1, 'Sat', 5), (15, 1, 'Sat', 6), (15, 1, 'Sat', 7),
(15, 1, 'Sun', 1), (15, 1, 'Sun', 2), (15, 1, 'Sun', 3), (15, 1, 'Sun', 4), (15, 1, 'Sun', 5), (15, 1, 'Sun', 6), (15, 1, 'Sun', 7);