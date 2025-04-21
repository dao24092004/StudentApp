-- Tạo bảng TBL_SEMESTER
CREATE TABLE tbl_semester (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT check_semester_dates CHECK (start_date < end_date)
);

-- Bảng tbl_curriculum: Lưu thông tin chương trình khung (CTK của từng ngành)
CREATE TABLE tbl_curriculum (
    id SERIAL PRIMARY KEY, -- Khóa chính, tự động tăng
    curriculum_code VARCHAR(20) NOT NULL, -- Mã chương trình khung, duy nhất
    curriculum_name VARCHAR(100) NOT NULL, -- Tên chương trình khung
    description VARCHAR(250), -- Mô tả chương trình khung
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian cập nhật
    CONSTRAINT uk_curriculum_code UNIQUE (curriculum_code) -- Ràng buộc duy nhất cho curriculum_code
);
-- Bảng tbl_department: Lưu thông tin khoa (CNTT, Chính trị, v.v.)
CREATE TABLE tbl_department (
    id SERIAL PRIMARY KEY, -- Khóa chính, tự động tăng
    dept_code VARCHAR(20) NOT NULL, -- Mã khoa, duy nhất
    dept_name VARCHAR(100) NOT NULL, -- Tên khoa
    description VARCHAR(250), -- Mô tả khoa
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian cập nhật
    CONSTRAINT uk_dept_code UNIQUE (dept_code) -- Ràng buộc duy nhất cho dept_code
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