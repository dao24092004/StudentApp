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
(10, 8, 7, 1, '2025-01-01', 'Registered'), -- SV007 đăng ký LSD001
(11, 9, 8, 2, '2025-06-01', 'Registered'), -- SV008 đăng ký ĐTDT_01
(12, 10, 9, 2, '2025-06-01', 'Registered'), -- SV009 đăng ký KTKT_01
(13, 11, 10, 2, '2025-06-01', 'Registered'), -- SV010 đăng ký CKCS_01
(14, 12, 11, 3, '2025-06-15', 'Registered'), -- SV011 đăng ký TD001
(15, 13, 12, 2, '2025-06-01', 'Registered');-- SV012 đăng ký KTDT001

-- Nhập dữ liệu mẫu cho tbl_grade
INSERT INTO tbl_grade (id, class_id, student_id, attendance_score, exam_score, final_score, note) VALUES
(1, 1, 1, 8.5, 7.0, 7.5, 'Tốt'), -- SV001, THDC_01
(2, 1, 2, 9.0, 8.0, 8.5, 'Xuất sắc'), -- SV002, THDC_01
(3, 2, 1, 7.0, 6.5, 6.8, 'Khá'), -- SV001, OOP_01
(4, 2, 2, 8.0, 7.5, 7.8, 'Tốt'), -- SV002, OOP_01
(5, 3, 3, 6.5, 6.0, 6.2, 'Trung bình'), -- SV003, NLP001
(6, 4, 4, 7.5, 7.0, 7.2, 'Tốt'), -- SV004, PLDC001
(7, 5, 5, 8.0, 8.5, 8.3, 'Xuất sắc'), -- SV005, TC001
(8, 6, 6, 6.0, 5.5, 5.7, 'Trung bình'), -- SV006, THML001
(9, 7, 7, 7.0, 6.5, 6.7, 'Khá'), -- SV007, VLDC001
(10, 8, 7, 8.5, 8.0, 8.2, 'Tốt'), -- SV007, LSD001
(11, 9, 8, 7.5, 7.0, 7.2, 'Tốt'), -- SV008, ĐTDT_01
(12, 10, 9, 6.5, 6.0, 6.2, 'Trung bình'), -- SV009, KTKT_01
(13, 11, 10, 8.0, 8.5, 8.3, 'Xuất sắc'), -- SV010, CKCS_01
(14, 12, 11, 7.0, 6.5, 6.7, 'Khá'), -- SV011, TD001
(15, 13, 12, 7.5, 7.0, 7.2, 'Tốt'), -- SV012, KTDT001
(16, 14, 13, 6.0, 5.5, 5.7, 'Trung bình'); -- SV013, KTQT001



