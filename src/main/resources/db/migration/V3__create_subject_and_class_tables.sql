-- Tạo bảng TBL_SUBJECT
CREATE TABLE tbl_subject (
    id SERIAL PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    credits INTEGER NOT NULL,
    description VARCHAR(100),
    semester_id INTEGER NOT NULL,
    CONSTRAINT uk_subject_code UNIQUE (subject_code),
    CONSTRAINT fk_subject_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);

-- Tạo bảng TBL_CLASS
CREATE TABLE tbl_class (
    id SERIAL PRIMARY KEY,
    class_code VARCHAR(20) NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    subject_id INTEGER,
    teacher_id INTEGER,
    start_date DATE,
    end_date DATE,
    classroom VARCHAR(50),
    CONSTRAINT uk_class_code UNIQUE (class_code),
    CONSTRAINT check_class_dates CHECK (start_date < end_date),
    CONSTRAINT fk_class_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
    CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id)
);