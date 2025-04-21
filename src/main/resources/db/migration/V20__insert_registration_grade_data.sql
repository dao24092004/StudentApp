-- Nhập dữ liệu mẫu cho tbl_registration (Thêm semester_id)
INSERT INTO tbl_registration (id, class_id, student_id, semester_id, registration_date, status) VALUES
(1, 1, 1, 1, '2025-01-01', 'Registered'), -- SV001 đăng ký THDC_01
(2, 1, 2, 1, '2025-01-01', 'Registered'), -- SV002 đăng ký THDC_01
(3, 2, 1, 1, '2025-01-01', 'Registered'), -- SV001 đăng ký OOP_01
(4, 2, 2, 1, '2025-01-01', 'Registered'), -- SV002 đăng ký OOP_01
(5, 3, 3, 1, '2025-01-01', 'Registered'), -- SV003 đăng ký NLP001
(6, 4, 4, 1, '2025-01-01', 'Registered'), -- SV004 đăng ký PLDC001
(7, 5, 5, 1, '2025-01-01', 'Registered'), -- SV005 đăng ký TC001
(8, 6, 6, 1, '2025-01-01', 'Registered'), -- SV006 đăng ký THML001
(9, 7, 7, 1, '2025-01-01', 'Registered'), -- SV007 đăng ký VLDC001
(10, 8, 7, 1, '2025-01-01', 'Registered'); -- SV007 đăng ký LSD001
-- Nhập dữ liệu mẫu cho tbl_grade
INSERT INTO tbl_grade (id, class_id, student_id, attendance_score, exam_score, final_score, note) VALUES
(1, 1, 1, 8.5, 7.0, 7.5, 'Tốt'), -- SV001, THDC_01
(2, 1, 2, 9.0, 8.0, 8.5, 'Xuất sắc'), -- SV002, THDC_01
(3, 2, 1, 7.0, 6.5, 6.8, 'Khá'), -- SV001, OOP_01
(4, 2, 2, 8.0, 7.5, 7.8, 'Tốt'); -- SV002, OOP_01