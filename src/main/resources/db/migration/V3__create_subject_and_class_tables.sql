-- Bảng tbl_subject: Lưu thông tin môn học
CREATE TABLE tbl_subject (
    id SERIAL PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL, -- Mã môn học
    subject_name VARCHAR(100) NOT NULL, -- Tên môn học
    credits INTEGER NOT NULL, -- Số tín chỉ
    description VARCHAR(100), -- Mô tả môn học
    semester_id INTEGER NOT NULL, -- ID của kỳ học (liên kết với tbl_semester)
    dept_id INTEGER NOT NULL, -- ID của khoa phụ trách môn học (liên kết với tbl_department, bắt buộc)
    CONSTRAINT uk_subject_code UNIQUE (subject_code), -- Ràng buộc: mã môn học là duy nhất
    CONSTRAINT fk_subject_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id), -- Khóa ngoại tới tbl_semester
    CONSTRAINT fk_subject_dept FOREIGN KEY (dept_id) REFERENCES tbl_department(id) -- Khóa ngoại tới tbl_department
);
-- Bảng tbl_class: Lưu thông tin lớp học
CREATE TABLE tbl_class (
    id SERIAL PRIMARY KEY,
    class_code VARCHAR(20) NOT NULL, -- Mã lớp học
    class_name VARCHAR(100) NOT NULL, -- Tên lớp học
    subject_id INTEGER NOT NULL, -- ID của môn học (liên kết với tbl_subject, bắt buộc)
    teacher_id INTEGER NOT NULL, -- ID của giáo viên (liên kết với tbl_teacher, bắt buộc)
    class_group_id INTEGER NOT NULL, -- ID của nhóm lớp (liên kết với tbl_class_group, bắt buộc)
    start_date DATE, -- Ngày bắt đầu lớp học
    end_date DATE, -- Ngày kết thúc lớp học
    classroom VARCHAR(50), -- Phòng học
    shift VARCHAR(10) CHECK (shift IN ('Morning', 'Afternoon')), -- Ca học: Morning (sáng), Afternoon (chiều)
    priority INTEGER DEFAULT 10 CHECK (priority >= 0), -- Độ ưu tiên khi xếp lịch
    CONSTRAINT uk_class_code UNIQUE (class_code), -- Ràng buộc: mã lớp học là duy nhất
    CONSTRAINT check_class_dates CHECK (start_date < end_date), -- Ràng buộc: ngày bắt đầu nhỏ hơn ngày kết thúc
    CONSTRAINT fk_class_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id), -- Khóa ngoại tới tbl_subject
    CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id), -- Khóa ngoại tới tbl_teacher
    CONSTRAINT fk_class_group FOREIGN KEY (class_group_id) REFERENCES tbl_class_group(id) -- Khóa ngoại tới tbl_class_group
);

