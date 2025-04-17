

-- Khởi tạo dữ liệu cho bảng tbl_user (admin1 với mật khẩu "admin" đã mã hóa bằng BCrypt)
INSERT INTO tbl_user (id, username, password, email, role_id) VALUES ('admin1', '$2a$10$V3LlC56TIt8IBJNZSPwGZOXQRNGR41XJ/khDB6oam8oLnWcF0Q/cW', 'admin1@example.com', 1);

-- Khởi tạo dữ liệu cho bảng tbl_teacher
INSERT INTO tbl_teacher (id, teacher_code, teacher_name, email) VALUES ('GV001', 'Nguyen Van A', 'nguyenvana@university.edu.vn');