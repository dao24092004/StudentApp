-- Thêm dữ liệu cho tbl_semester
INSERT INTO tbl_semester (start_date, end_date) VALUES
('2023-09-01', '2024-01-15'),
('2024-02-01', '2024-06-15');

-- Nhập dữ liệu mẫu cho tbl_class_group
INSERT INTO tbl_class_group (id, group_code, group_name, major_id, shift, semester_id) VALUES
(1, 'CNPM1', 'Công nghệ Phần mềm 1', 1, 'Morning', 1),
(2, 'CNPM2', 'Công nghệ Phần mềm 2', 1, 'Morning', 1),
(3, 'CNPM3', 'Công nghệ Phần mềm 3', 1, 'Morning', 1),
(4, 'TMĐT1', 'Thương mại Điện tử 1', 2, 'Afternoon', 1),
(5, 'TMĐT2', 'Thương mại Điện tử 2', 2, 'Afternoon', 1),
(6, 'ANM1', 'An ninh Mạng 1', 3, 'Afternoon', 1);

-- Nhập dữ liệu mẫu cho tbl_student (Bổ sung sinh viên cho tất cả nhóm lớp)
INSERT INTO tbl_student (id, user_id, student_code, student_name, date_of_birth, gender, address, phone_number, major_id, class_group_id) VALUES
(1, 4, 'SV001', 'Nguyen Van A', '2002-05-15', 'Male', 'Hà Nội', '0912345678', 1, 1), -- CNPM1
(2, 5, 'SV002', 'Tran Thi B', '2002-07-20', 'Female', 'TP.HCM', '0912345679', 1, 1), -- CNPM1
(3, 6, 'SV003', 'Le Van C', '2002-08-10', 'Male', 'Hà Nội', '0912345680', 1, 2), -- CNPM2
(4, 7, 'SV004', 'Pham Thi D', '2002-09-15', 'Female', 'TP.HCM', '0912345681', 1, 3), -- CNPM3
(5, 8, 'SV005', 'Nguyen Van E', '2002-10-20', 'Male', 'Hà Nội', '0912345682', 2, 4), -- TMĐT1
(6, 9, 'SV006', 'Tran Thi F', '2002-11-25', 'Female', 'TP.HCM', '0912345683', 2, 5), -- TMĐT2
(7, 10, 'SV007', 'Le Van G', '2002-12-30', 'Male', 'Hà Nội', '0912345684', 3, 6); -- ANM1

-- Nhập dữ liệu mẫu cho tbl_teacher
INSERT INTO tbl_teacher (id, user_id, teacher_code, teacher_name, date_of_birth, gender, address, phone_number, email, dept_id) VALUES
(1, 2, 'GV001', 'Nguyen Van B', '1980-03-10', 'Male', 'Hà Nội', '0912345685', 'teacher1@university.edu.vn', 1), -- CNTT
(2, 3, 'GV002', 'Tran Thi C', '1985-06-25', 'Female', 'TP.HCM', '0912345686', 'teacher2@university.edu.vn', 2); -- Chính trị

