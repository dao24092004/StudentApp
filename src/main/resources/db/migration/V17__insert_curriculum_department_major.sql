-- Nhập dữ liệu mẫu cho tbl_department
INSERT INTO tbl_department (id, dept_code, dept_name, description, created_at, updated_at)
VALUES 
(1, 'CNTT', 'Công nghệ Thông tin', 'Khoa Công nghệ Thông tin, chuyên về CNTT và kỹ thuật số', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(2, 'CT', 'Chính trị', 'Khoa Chính trị, đào tạo môn khoa học xã hội', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(3, 'KTDT', 'Kỹ thuật Điện tử', 'Khoa Kỹ thuật Điện tử, tập trung vào công nghệ điện tử', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(4, 'KT', 'Kinh tế', 'Khoa Kinh tế, đào tạo về quản lý và kinh doanh', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(5, 'CK', 'Cơ khí', 'Khoa Cơ khí, chuyên về kỹ thuật cơ khí và chế tạo', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(6, 'TD', 'Thể dục', 'Khoa Thể dục, đào tạo môn thể chất', '2025-01-01 00:00:00', '2025-05-06 00:00:00')
ON CONFLICT (id) DO NOTHING;

-- Thêm dữ liệu mới
INSERT INTO tbl_semester (id, semester_name, start_date, end_date) VALUES
(1, 'Học kỳ 1 - 2025-2026', '2025-09-01', '2025-12-15'),  -- 15 tuần
(2, 'Học kỳ 2 - 2025-2026', '2026-02-01', '2026-05-15'),  -- 15 tuần
(3, 'Học kỳ hè - 2025-2026', '2026-07-01', '2026-08-15')  -- 6 tuần
ON CONFLICT DO NOTHING;



-- Nhập dữ liệu mẫu cho tbl_curriculum
INSERT INTO tbl_curriculum (curriculum_code, curriculum_name, description, created_at, updated_at) VALUES
('CT001-2025', 'Chương trình Công nghệ Thông tin 2025', 'Chương trình đào tạo ngành CNTT cập nhật 2025', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
('CT002-2025', 'Chương trình Kỹ thuật Điện tử 2025', 'Chương trình đào tạo ngành Kỹ thuật Điện tử cập nhật 2025', '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
('CT003-2025', 'Chương trình Kinh tế 2025', 'Chương trình đào tạo ngành Kinh tế cập nhật 2025', '2025-01-01 00:00:00', '2025-05-06 00:00:00')
ON CONFLICT (curriculum_code) DO NOTHING;

-- Thêm dữ liệu mới với số tiết giảm bớt cho khả thi
INSERT INTO tbl_subject (subject_code, subject_name, credits, description, dept_id, theory_periods, practical_periods) VALUES
-- Kỳ 1
('THDC', 'Tin học đại cương', 3, 'Môn học cơ bản về tin học', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
('OOP', 'Lập trình hướng đối tượng', 4, 'Môn học về lập trình OOP', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 25, 15), -- 40 tiết
('NLP001', 'Ngôn ngữ lập trình', 3, 'Môn học về lập trình', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
('PLDC001', 'Pháp luật đại cương', 2, 'Môn học về pháp luật', (SELECT id FROM tbl_department WHERE dept_code = 'CT'), 15, 5), -- 20 tiết
('TC001', 'Toán cao cấp 1', 4, 'Môn học về toán', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 25, 15), -- 40 tiết
('THML001', 'Triết học Mác-Lênin', 3, 'Môn học về triết học', (SELECT id FROM tbl_department WHERE dept_code = 'CT'), 20, 10), -- 30 tiết
('VLDC001', 'Vật lý đại cương', 3, 'Môn học về vật lý', (SELECT id FROM tbl_department WHERE dept_code = 'KTDT'), 20, 10), -- 30 tiết
('LSD001', 'Lịch sử Đảng', 2, 'Môn học về lịch sử', (SELECT id FROM tbl_department WHERE dept_code = 'CT'), 15, 5), -- 20 tiết
('CSDL', 'Cơ sở dữ liệu', 3, 'Môn học về cơ sở dữ liệu', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
('GTNT', 'Giải tích nâng cao', 3, 'Môn học về giải tích', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
-- Kỳ 2
('DTDT', 'Điện tử cơ bản', 3, 'Môn học cơ bản về điện tử', (SELECT id FROM tbl_department WHERE dept_code = 'KTDT'), 20, 10), -- 30 tiết
('KTKT', 'Kinh tế vi mô', 3, 'Môn học về kinh tế vi mô', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 20, 10), -- 30 tiết
('CKCS', 'Cơ khí chế tạo', 4, 'Môn học về cơ khí chế tạo', (SELECT id FROM tbl_department WHERE dept_code = 'CK'), 25, 15), -- 40 tiết
('KTDT001', 'Kỹ thuật số', 3, 'Môn học về kỹ thuật số', (SELECT id FROM tbl_department WHERE dept_code = 'KTDT'), 20, 10), -- 30 tiết
('KTQT001', 'Kinh tế quốc tế', 3, 'Môn học về kinh tế quốc tế', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 20, 10), -- 30 tiết
('CKMT001', 'Cơ học máy và thiết bị', 4, 'Môn học về cơ học máy', (SELECT id FROM tbl_department WHERE dept_code = 'CK'), 25, 15), -- 40 tiết
('MHTT001', 'Mạng máy tính', 3, 'Môn học về mạng máy tính', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
('QLKD001', 'Quản lý kinh doanh', 2, 'Môn học về quản lý', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 15, 5), -- 20 tiết
('AI001', 'Trí tuệ nhân tạo', 4, 'Môn học về AI', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 25, 15), -- 40 tiết
('TKCT', 'Thiết kế cơ khí', 3, 'Môn học về thiết kế cơ khí', (SELECT id FROM tbl_department WHERE dept_code = 'CK'), 20, 10), -- 30 tiết
-- Kỳ 3
('TD001', 'Thể dục 1', 1, 'Môn học thể dục cơ bản', (SELECT id FROM tbl_department WHERE dept_code = 'TD'), 10, 5), -- 15 tiết
('CNPM001', 'Công nghệ phần mềm', 4, 'Môn học về công nghệ phần mềm', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 25, 15), -- 40 tiết
('HTTT001', 'Hệ thống thông tin', 3, 'Môn học về hệ thống thông tin', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 20, 10), -- 30 tiết
('KDVT001', 'Kinh doanh và thương mại', 3, 'Môn học về kinh doanh', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 20, 10), -- 30 tiết
('DTNM001', 'Điện tử năng lượng mặt trời', 3, 'Môn học về điện tử năng lượng', (SELECT id FROM tbl_department WHERE dept_code = 'KTDT'), 20, 10), -- 30 tiết
('CKCT001', 'Cơ khí công trình', 4, 'Môn học về cơ khí công trình', (SELECT id FROM tbl_department WHERE dept_code = 'CK'), 25, 15), -- 40 tiết
('GDTC001', 'Giáo dục thể chất', 1, 'Môn học thể dục nâng cao', (SELECT id FROM tbl_department WHERE dept_code = 'TD'), 10, 5), -- 15 tiết
('QTKD001', 'Quản trị kinh doanh', 2, 'Môn học về quản trị', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 15, 5), -- 20 tiết
('ML001', 'Máy học', 4, 'Môn học về máy học', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 25, 15), -- 40 tiết
('QTMK', 'Quản trị marketing', 3, 'Môn học về marketing', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 20, 10) -- 30 tiết
ON CONFLICT (subject_code) DO NOTHING;



-- Nhập dữ liệu mẫu cho tbl_curriculum_detail
-- Nhập dữ liệu mẫu cho tbl_curriculum_detail
INSERT INTO tbl_curriculum_detail (curriculum_id, subject_id, semester_id, is_mandatory) VALUES
-- CT001-2025: Công nghệ Thông tin (Công nghệ phần mềm, An ninh mạng, Thương mại điện tử)
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'THDC'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'OOP'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'NLP001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'CSDL'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'GTNT'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'PLDC001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'MHTT001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'AI001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'CNPM001'), 3, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'HTTT001'), 3, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'ML001'), 3, TRUE),
-- CT002-2025: Kỹ thuật Điện tử (Kỹ thuật điện tử, Cơ khí ô tô)
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'TC001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'VLDC001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'LSD001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'DTDT'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'KTDT001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'CKCS'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'CKMT001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'TKCT'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'DTNM001'), 3, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'CKCT001'), 3, TRUE),
-- CT003-2025: Kinh tế (Kinh tế quốc tế)
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'PLDC001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'THML001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'LSD001'), 1, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'KTKT'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'KTQT001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'QLKD001'), 2, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'KDVT001'), 3, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'QTKD001'), 3, TRUE),
((SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), 
    (SELECT id FROM tbl_subject WHERE subject_code = 'QTMK'), 3, TRUE)
ON CONFLICT (curriculum_id, subject_id, semester_id) DO NOTHING;


-- Dữ liệu cho tbl_major
INSERT INTO tbl_major (id, major_code, major_name, dept_id, curriculum_id, created_at, updated_at) 
VALUES 
(1, 'CNTT-CNPM', 'Công nghệ Phần mềm', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(2, 'CNTT-TMĐT', 'Thương mại Điện tử', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(3, 'CNTT-ANM', 'An ninh Mạng', (SELECT id FROM tbl_department WHERE dept_code = 'CNTT'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT001-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(4, 'KTDT-DT', 'Kỹ thuật Điện tử', (SELECT id FROM tbl_department WHERE dept_code = 'KTDT'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(5, 'KT-KTQT', 'Kinh tế Quốc tế', (SELECT id FROM tbl_department WHERE dept_code = 'KT'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT003-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(6, 'CK-OTO', 'Cơ khí Ô tô', (SELECT id FROM tbl_department WHERE dept_code = 'CK'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT002-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00'),
(7, 'TD-TD', 'Thể dục', (SELECT id FROM tbl_department WHERE dept_code = 'TD'), 
    (SELECT id FROM tbl_curriculum WHERE curriculum_code = 'CT004-2025'), '2025-01-01 00:00:00', '2025-05-06 00:00:00')
ON CONFLICT (id) DO NOTHING;


