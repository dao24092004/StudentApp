
-- Nhập dữ liệu mẫu cho tbl_subject (Thêm dept_id)
INSERT INTO tbl_subject (id, subject_code, subject_name, credits, description, semester_id, dept_id) VALUES
(1, 'THDC', 'Tin học đại cương', 3, 'Môn học cơ bản về tin học', 1, 1), -- CNTT
(2, 'OOP', 'Lập trình hướng đối tượng', 4, 'Môn học về lập trình OOP', 1, 1), -- CNTT
(3, 'NLP001', 'Ngôn ngữ lập trình', 3, 'Môn học về lập trình', 1, 1), -- CNTT
(4, 'PLDC001', 'Pháp luật đại cương', 2, 'Môn học về pháp luật', 1, 2), -- Chính trị
(5, 'TC001', 'Toán cao cấp 1', 4, 'Môn học về toán', 1, 1), -- CNTT
(6, 'THML001', 'Triết học Mác-Lênin', 3, 'Môn học về triết học', 1, 2), -- Chính trị
(7, 'VLDC001', 'Vật lý đại cương', 3, 'Môn học về vật lý', 1, 1), -- CNTT
(8, 'LSD001', 'Lịch sử Đảng', 2, 'Môn học về lịch sử', 1, 2); -- Chính trị
-- Nhập dữ liệu mẫu cho tbl_class (Thêm class_group_id và shift)
INSERT INTO tbl_class (id, class_code, class_name, subject_id, teacher_id, class_group_id, start_date, end_date, classroom, shift, priority) VALUES
(1, 'THDC_01', 'Tin học đại cương - Lớp 01', 1, 1, 1, '2025-01-01', '2025-05-31', 'P101', 'Morning', 10), -- CNPM1
(2, 'OOP_01', 'Lập trình OOP - Lớp 01', 2, 1, 1, '2025-01-01', '2025-05-31', 'P102', 'Morning', 10), -- CNPM1
(3, 'NLP001', 'Ngôn ngữ lập trình', 3, 1, 2, '2025-01-01', '2025-05-31', 'P103', 'Morning', 11), -- CNPM2
(4, 'PLDC001', 'Pháp luật đại cương', 4, 2, 3, '2025-01-01', '2025-05-31', 'P104', 'Morning', 11), -- CNPM3
(5, 'TC001', 'Toán cao cấp 1', 5, 1, 4, '2025-01-01', '2025-05-31', 'P105', 'Afternoon', 14), -- TMĐT1
(6, 'THML001', 'Triết học Mác-Lênin', 6, 2, 5, '2025-01-01', '2025-05-31', 'P106', 'Afternoon', 14), -- TMĐT2
(7, 'VLDC001', 'Vật lý đại cương', 7, 1, 6, '2025-01-01', '2025-05-31', 'P107', 'Afternoon', 14), -- ANM1
(8, 'LSD001', 'Lịch sử Đảng', 8, 2, 6, '2025-01-01', '2025-05-31', 'P108', 'Afternoon', 11); -- ANM1


-- Nhập dữ liệu mẫu cho tbl_time_window
INSERT INTO tbl_time_window (id_class, day_of_week, slot, created_date) VALUES
(1, 'Mon', 1, '2025-04-20'), -- CNPM1: Thứ 2, ca sáng
(2, 'Mon', 1, '2025-04-20'), -- CNPM1: Thứ 2, ca sáng
(3, 'Tue', 1, '2025-04-20'), -- CNPM2: Thứ 3, ca sáng
(4, 'Wed', 1, '2025-04-20'), -- CNPM3: Thứ 4, ca sáng
(5, 'Mon', 2, '2025-04-20'), -- TMĐT1: Thứ 2, ca chiều
(6, 'Thu', 2, '2025-04-20'), -- TMĐT2: Thứ 5, ca chiều
(7, 'Fri', 2, '2025-04-20'), -- ANM1: Thứ 6, ca chiều
(8, 'Fri', 2, '2025-04-20'); -- ANM1: Thứ 6, ca chiều