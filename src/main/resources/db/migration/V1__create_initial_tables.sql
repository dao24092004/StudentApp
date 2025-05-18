-- Tạo bảng tbl_department
CREATE TABLE tbl_department (
    id SERIAL PRIMARY KEY,
    dept_code VARCHAR(20) NOT NULL,
    dept_name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dept_code UNIQUE (dept_code)
);


-- Tạo bảng tbl_semester
CREATE TABLE tbl_semester (
    id SERIAL PRIMARY KEY,
    semester_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT check_semester_dates CHECK (start_date < end_date)
);

-- Tạo bảng tbl_curriculum
CREATE TABLE tbl_curriculum (
    id SERIAL PRIMARY KEY,
    curriculum_code VARCHAR(20) NOT NULL,
    curriculum_name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_curriculum_code UNIQUE (curriculum_code)
);
-- Tạo bảng tbl_subject
CREATE TABLE tbl_subject (
    id SERIAL PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    credits INTEGER NOT NULL,
    description VARCHAR(250),
    dept_id INTEGER NOT NULL,
    theory_periods INTEGER NOT NULL,
    practical_periods INTEGER NOT NULL,
    CONSTRAINT fk_subject_department FOREIGN KEY (dept_id) REFERENCES tbl_department(id),
    CONSTRAINT uk_subject_code UNIQUE (subject_code),
    CONSTRAINT check_periods CHECK (theory_periods >= 0 AND practical_periods >= 0 AND (theory_periods + practical_periods) > 0)
);

-- Tạo bảng tbl_curriculum_detail
CREATE TABLE tbl_curriculum_detail (
    id SERIAL PRIMARY KEY,
    curriculum_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    semester_id INTEGER NOT NULL,
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_curriculum_detail_curriculum FOREIGN KEY (curriculum_id) REFERENCES tbl_curriculum(id),
    CONSTRAINT fk_curriculum_detail_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
    CONSTRAINT fk_curriculum_detail_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id),
    CONSTRAINT uk_curriculum_subject_semester UNIQUE (curriculum_id, subject_id, semester_id)
);




-- Tạo bảng TBL_ROLE
CREATE TABLE tbl_role (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_name UNIQUE (role_name)
);




-- Tạo bảng TBL_PERMISSION
CREATE TABLE tbl_permission (
    id SERIAL PRIMARY KEY,
    permission_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_permission_name UNIQUE (permission_name)
);