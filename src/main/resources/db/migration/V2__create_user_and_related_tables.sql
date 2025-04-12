-- Tạo bảng TBL_USER
CREATE TABLE tbl_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role_id  INTEGER ,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES tbl_role(id)
);

-- Tạo bảng TBL_MAJOR
CREATE TABLE tbl_major (
    id SERIAL PRIMARY KEY,
    major_code VARCHAR(20) NOT NULL,
    major_name VARCHAR(100) NOT NULL,
    dept_id INTEGER NOT NULL,
    curriculum_id INTEGER,
    description VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_major_code UNIQUE (major_code),
    CONSTRAINT uk_major_curriculum UNIQUE (curriculum_id),
    CONSTRAINT fk_major_dept FOREIGN KEY (dept_id) REFERENCES tbl_department(id),
    CONSTRAINT fk_major_curriculum FOREIGN KEY (curriculum_id) REFERENCES tbl_curriculum(id)
);

-- Tạo bảng TBL_STUDENT
CREATE TABLE tbl_student (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    student_code VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    address VARCHAR(100),
    phone_number VARCHAR(15),
    major_id INTEGER NOT NULL,
    CONSTRAINT uk_student_code UNIQUE (student_code),
    CONSTRAINT uk_student_phone UNIQUE (phone_number),
    CONSTRAINT uk_student_user UNIQUE (user_id),
    CONSTRAINT check_student_gender CHECK (gender IN ('Male', 'Female')),
    CONSTRAINT fk_student_major FOREIGN KEY (major_id) REFERENCES tbl_major(id),
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES tbl_user(id) ON DELETE SET NULL
);

-- Tạo bảng TBL_TEACHER
CREATE TABLE tbl_teacher (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    teacher_code VARCHAR(20) NOT NULL,
    teacher_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    address VARCHAR(100),
    phone_number VARCHAR(15),
    email VARCHAR(100) NOT NULL,
    CONSTRAINT uk_teacher_code UNIQUE (teacher_code),
    CONSTRAINT uk_teacher_email UNIQUE (email),
    CONSTRAINT uk_teacher_phone UNIQUE (phone_number),
    CONSTRAINT uk_teacher_user UNIQUE (user_id),
    CONSTRAINT check_teacher_gender CHECK (gender IN ('Male', 'Female')),
    CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES tbl_user(id) ON DELETE SET NULL
);