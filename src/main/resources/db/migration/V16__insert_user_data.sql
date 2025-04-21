-- Nhập dữ liệu mẫu cho tbl_user (nếu chưa có)
INSERT INTO tbl_user (id, username, password, email, role_id) VALUES
(1, 'admin1', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'admin1@university.edu.vn', 1),
(2, 'teacher1', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'teacher1@university.edu.vn', 2),
(3, 'teacher2', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'teacher2@university.edu.vn', 2),
(4, 'student1', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student1@university.edu.vn', 3),
(5, 'student2', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student2@university.edu.vn', 3),
(6, 'student3', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student3@university.edu.vn', 3),
(7, 'student4', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student4@university.edu.vn', 3),
(8, 'student5', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student5@university.edu.vn', 3),
(9, 'student6', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student6@university.edu.vn', 3),
(10, 'student7', '$2a$10$5eL4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student7@university.edu.vn', 3)
ON CONFLICT (id) DO NOTHING;