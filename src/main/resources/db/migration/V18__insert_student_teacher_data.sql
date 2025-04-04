-- Thêm dữ liệu cho tbl_student
INSERT INTO tbl_student (user_id, student_code, student_name, date_of_birth, gender, address, phone_number, major_id) VALUES
(4, 'SV001', 'Nguyễn Văn A', '2002-05-15', 'Male', 'Hà Nội', '0912345678', 1),
(5, 'SV002', 'Trần Thị B', '2002-07-20', 'Female', 'TP.HCM', '0912345679', 1);

-- Thêm dữ liệu cho tbl_teacher
INSERT INTO tbl_teacher (user_id, teacher_code, teacher_name, date_of_birth, gender, address, phone_number, email) VALUES
(2, 'GV001', 'Lê Văn C', '1980-03-10', 'Male', 'Hà Nội', '0912345680', 'teacher1@university.edu.vn'),
(3, 'GV002', 'Phạm Thị D', '1985-06-25', 'Female', 'TP.HCM', '0912345681', 'teacher2@university.edu.vn');