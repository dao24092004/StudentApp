-- Thêm dữ liệu cho tbl_semester
INSERT INTO tbl_semester (start_date, end_date) VALUES
('2023-09-01', '2024-01-15'),
('2024-02-01', '2024-06-15');

-- Thêm dữ liệu cho tbl_subject
INSERT INTO tbl_subject (subject_code, subject_name, credits, description, semester_id) VALUES
('THDC', 'Tin học đại cương', 3, 'Môn học cơ bản về tin học', 1),
('OOP', 'Lập trình hướng đối tượng', 4, 'Môn học về lập trình OOP', 1);

-- Thêm dữ liệu cho tbl_class
INSERT INTO tbl_class (class_code, class_name, subject_id, teacher_id, start_date, end_date, classroom) VALUES
('THDC_01', 'Tin học đại cương - Lớp 01', 1, 1, '2023-09-01', '2023-12-15', 'P101'),
('OOP_01', 'Lập trình OOP - Lớp 01', 2, 2, '2023-09-01', '2023-12-15', 'P102');