-- Thêm dữ liệu cho tbl_registration
INSERT INTO tbl_registration (class_id, student_id, registration_date, status) VALUES
(1, 1, '2023-08-20', 'Registered'),
(1, 2, '2023-08-20', 'Registered'),
(2, 1, '2023-08-20', 'Registered');

-- Thêm dữ liệu cho tbl_grade
INSERT INTO tbl_grade (class_id, student_id, attendance_score, exam_score, final_score, note) VALUES
(1, 1, 8.5, 7.0, 7.5, 'Tốt'),
(1, 2, 9.0, 8.0, 8.5, 'Xuất sắc'),
(2, 1, 7.0, 6.5, 6.8, 'Khá');