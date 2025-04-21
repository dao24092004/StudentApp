-- Dữ liệu cho tbl_department
INSERT INTO tbl_department (id, dept_code, dept_name, description, created_at, updated_at)
VALUES 
(1, 'CNTT', 'Công nghệ Thông tin', 'Khoa Công nghệ Thông tin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'CT', 'Chính trị', 'Khoa Chính trị', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'KTDT', 'Kỹ thuật Điện tử', 'Khoa Kỹ thuật Điện tử', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Dữ liệu cho tbl_curriculum
INSERT INTO tbl_curriculum (id, curriculum_code, curriculum_name, description, created_at, updated_at) 
VALUES 
(1, 'CT001', 'Chương trình Công nghệ Thông tin 2023', 'Chương trình đào tạo ngành CNTT 2023', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'CT002', 'Chương trình Kỹ thuật Điện tử 2023', 'Chương trình đào tạo ngành Kỹ thuật Điện tử 2023', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Dữ liệu cho tbl_major
INSERT INTO tbl_major (id, major_code, major_name, dept_id, curriculum_id, created_at, updated_at) 
VALUES 
(1, 'CNTT-CNPM', 'Công nghệ Phần mềm', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Dùng CT001
(2, 'CNTT-TMĐT', 'Thương mại Điện tử', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Dùng CT001
(3, 'CNTT-ANM', 'An ninh Mạng', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Dùng CT001
(4, 'KTDT-DT', 'Kỹ thuật Điện tử', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) -- Dùng CT002
ON CONFLICT (id) DO NOTHING;