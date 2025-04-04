-- Thêm dữ liệu cho tbl_curriculum
INSERT INTO tbl_curriculum (curriculum_code, curriculum_name, description, created_at, updated_at) VALUES
('CT001', 'Chương trình Công nghệ Thông tin 2023', 'Chương trình đào tạo ngành CNTT 2023', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CT002', 'Chương trình Kỹ thuật Điện tử 2023', 'Chương trình đào tạo ngành Kỹ thuật Điện tử 2023', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho tbl_department
INSERT INTO tbl_department (dept_code, dept_name, description, created_at, updated_at) VALUES
('KHOA_CNTT', 'Khoa Công nghệ Thông tin', 'Khoa đào tạo ngành CNTT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KHOA_DT', 'Khoa Kỹ thuật Điện tử', 'Khoa đào tạo ngành Kỹ thuật Điện tử', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho tbl_major
INSERT INTO tbl_major (major_code, major_name, dept_id, curriculum_id, description, created_at, updated_at) VALUES
('CNTT01', 'Công nghệ Thông tin', 1, 1, 'Ngành Công nghệ Thông tin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DT01', 'Kỹ thuật Điện tử', 2, 2, 'Ngành Kỹ thuật Điện tử', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);