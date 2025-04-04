-- Thêm dữ liệu cho tbl_user
INSERT INTO tbl_user (username, password, email, role_id, avatar_url, created_at, updated_at) VALUES
('admin1', '$2a$10$XURP2.9g2xY44A3eK1fG5e4eL4b4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'admin1@example.com', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher1', '$2a$10$XURP2.9g2xY44A3eK1fG5e4eL4b4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'teacher1@example.com', 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher2', '$2a$10$XURP2.9g2xY44A3eK1fG5e4eL4b4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'teacher2@example.com', 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('student1', '$2a$10$XURP2.9g2xY44A3eK1fG5e4eL4b4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student1@example.com', 3, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('student2', '$2a$10$XURP2.9g2xY44A3eK1fG5e4eL4b4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e', 'student2@example.com', 3, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);