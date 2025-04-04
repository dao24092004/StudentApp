-- Tạo bảng TBL_SEMESTER
CREATE TABLE tbl_semester (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT check_semester_dates CHECK (start_date < end_date)
);

-- Tạo bảng TBL_CURRICULUM
CREATE TABLE tbl_curriculum (
    id SERIAL PRIMARY KEY,
    curriculum_code VARCHAR(20) NOT NULL,
    curriculum_name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_curriculum_code UNIQUE (curriculum_code)
);

-- Tạo bảng TBL_DEPARTMENT
CREATE TABLE tbl_department (
    id SERIAL PRIMARY KEY,
    dept_code VARCHAR(20) NOT NULL,
    dept_name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dept_code UNIQUE (dept_code)
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