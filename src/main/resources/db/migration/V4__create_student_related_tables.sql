-- Tạo bảng TBL_REGISTRATION
CREATE TABLE tbl_registration (
    id SERIAL PRIMARY KEY,
    class_id INTEGER,
    student_id INTEGER,
    registration_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(10),
    CONSTRAINT uk_registration UNIQUE (class_id, student_id),
    CONSTRAINT check_registration_status CHECK (status IN ('Registered', 'Cancelled')),
    CONSTRAINT fk_registration_class FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES tbl_student(id)
);

-- Tạo bảng TBL_ATTENDANCE
CREATE TABLE tbl_attendance (
    id SERIAL PRIMARY KEY,
    class_id INTEGER,
    student_id INTEGER,
    attendance_date DATE NOT NULL,
    status VARCHAR(20),
    note VARCHAR(100),
    CONSTRAINT uk_attendance UNIQUE (class_id, student_id, attendance_date),
    CONSTRAINT check_attendance_status CHECK (status IN ('Present', 'Absent', 'Late')),
    CONSTRAINT fk_attendance_class FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES tbl_student(id)
);

-- Tạo bảng TBL_GRADE
CREATE TABLE tbl_grade (
    id SERIAL PRIMARY KEY,
    class_id INTEGER,
    student_id INTEGER,
    attendance_score NUMERIC(5,2),
    exam_score NUMERIC(5,2),
    final_score NUMERIC(5,2),
    note VARCHAR(100),
    CONSTRAINT check_scores CHECK (attendance_score BETWEEN 0 AND 10 AND exam_score BETWEEN 0 AND 10 AND final_score BETWEEN 0 AND 10),
    CONSTRAINT fk_grade_class FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    CONSTRAINT fk_grade_student FOREIGN KEY (student_id) REFERENCES tbl_student(id)
);

-- Tạo bảng TBL_GRADE_APPEAL
CREATE TABLE tbl_grade_appeal (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    class_id INTEGER,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    CONSTRAINT check_appeal_status CHECK (status IN ('Pending', 'Approved', 'Rejected')),
    CONSTRAINT fk_appeal_student FOREIGN KEY (student_id) REFERENCES tbl_student(id),
    CONSTRAINT fk_appeal_class FOREIGN KEY (class_id) REFERENCES tbl_class(id)
);

-- Tạo bảng TBL_GRADE_HISTORY
CREATE TABLE tbl_grade_history (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER,
    grade_id INTEGER,
    old_score NUMERIC(5,2),
    new_score NUMERIC(5,2),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(225),
    CONSTRAINT fk_grade_history_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id),
    CONSTRAINT fk_grade_history_grade FOREIGN KEY (grade_id) REFERENCES tbl_grade(id)
);