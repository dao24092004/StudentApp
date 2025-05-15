-- Tạo bảng TBL_USER
CREATE TABLE tbl_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role_id  INTEGER DEFAULT 3,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES tbl_role(id)
);


-- Bảng tbl_major: Lưu thông tin ngành học (CNTT, TMĐT, ANM, v.v.)
CREATE TABLE tbl_major (
    id SERIAL PRIMARY KEY, -- Khóa chính, tự động tăng
    major_code VARCHAR(20) NOT NULL, -- Mã ngành, duy nhất
    major_name VARCHAR(100) NOT NULL, -- Tên ngành
    dept_id INTEGER NOT NULL, -- Khoa phụ trách ngành
    curriculum_id INTEGER, -- Chương trình khung của ngành
    description VARCHAR(250), -- Mô tả ngành
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian cập nhật
    CONSTRAINT uk_major_code UNIQUE (major_code), -- Ràng buộc duy nhất cho major_code
    CONSTRAINT fk_major_dept FOREIGN KEY (dept_id) REFERENCES tbl_department(id), -- Liên kết với tbl_department
    CONSTRAINT fk_major_curriculum FOREIGN KEY (curriculum_id) REFERENCES tbl_curriculum(id) -- Liên kết với tbl_curriculum
);

-- Sửa bảng tbl_class_group
CREATE TABLE tbl_class_group (
    id SERIAL PRIMARY KEY,
    group_code VARCHAR(20) NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    major_id INTEGER NOT NULL,
    shift VARCHAR(10) NOT NULL CHECK (shift IN ('Morning', 'Afternoon')),
    semester_id INTEGER NOT NULL,
    CONSTRAINT uk_class_group_code UNIQUE (group_code),
    CONSTRAINT fk_class_group_major FOREIGN KEY (major_id) REFERENCES tbl_major(id),
    CONSTRAINT fk_class_group_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);
-- Bảng tbl_student: Lưu thông tin sinh viên
CREATE TABLE tbl_student (
    id SERIAL PRIMARY KEY,
    user_id INTEGER, -- ID của người dùng (liên kết với tbl_user)
    student_code VARCHAR(20) NOT NULL, -- Mã sinh viên
    student_name VARCHAR(100) NOT NULL, -- Tên sinh viên
    date_of_birth DATE, -- Ngày sinh
    gender VARCHAR(10), -- Giới tính: 'Male', 'Female'
    address VARCHAR(100), -- Địa chỉ
    phone_number VARCHAR(15), -- Số điện thoại
    major_id INTEGER NOT NULL, -- ID của ngành học (liên kết với tbl_major)
    class_group_id INTEGER NOT NULL, -- ID của nhóm lớp (liên kết với tbl_class_group)
    CONSTRAINT uk_student_code UNIQUE (student_code), -- Ràng buộc: mã sinh viên là duy nhất
    CONSTRAINT uk_student_phone UNIQUE (phone_number), -- Ràng buộc: số điện thoại là duy nhất
    CONSTRAINT uk_student_user UNIQUE (user_id), -- Ràng buộc: mỗi sinh viên chỉ liên kết với 1 user
    CONSTRAINT check_student_gender CHECK (gender IN ('Male', 'Female')), -- Ràng buộc: giới tính chỉ được là 'Male' hoặc 'Female'
    CONSTRAINT fk_student_major FOREIGN KEY (major_id) REFERENCES tbl_major(id), -- Khóa ngoại tới tbl_major
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES tbl_user(id) ON DELETE SET NULL, -- Khóa ngoại tới tbl_user
    CONSTRAINT fk_student_class_group FOREIGN KEY (class_group_id) REFERENCES tbl_class_group(id), -- Khóa ngoại tới tbl_class_group
    CONSTRAINT check_student_class_group CHECK (class_group_id IS NOT NULL) -- Ràng buộc: mỗi sinh viên phải thuộc một nhóm lớp
);

-- Bảng tbl_teacher: Lưu thông tin giáo viên
-- Bảng tbl_teacher: Lưu thông tin giáo viên
CREATE TABLE tbl_teacher (
    id SERIAL PRIMARY KEY,
    user_id INTEGER, -- ID của người dùng (liên kết với tbl_user)
    teacher_code VARCHAR(20) NOT NULL, -- Mã giáo viên
    teacher_name VARCHAR(100) NOT NULL, -- Tên giáo viên
    date_of_birth DATE, -- Ngày sinh
    gender VARCHAR(10), -- Giới tính: 'Male', 'Female'
    address VARCHAR(100), -- Địa chỉ
    phone_number VARCHAR(15), -- Số điện thoại
    email VARCHAR(100) NOT NULL, -- Email
    dept_id INTEGER NOT NULL, -- ID của khoa (liên kết với tbl_department, bắt buộc)
    CONSTRAINT uk_teacher_code UNIQUE (teacher_code), -- Ràng buộc: mã giáo viên là duy nhất
    CONSTRAINT uk_teacher_email UNIQUE (email), -- Ràng buộc: email là duy nhất
    CONSTRAINT uk_teacher_phone UNIQUE (phone_number), -- Ràng buộc: số điện thoại là duy nhất
    CONSTRAINT uk_teacher_user UNIQUE (user_id), -- Ràng buộc: mỗi giáo viên chỉ liên kết với 1 user
    CONSTRAINT check_teacher_gender CHECK (gender IN ('Male', 'Female')), -- Ràng buộc: giới tính chỉ được là 'Male' hoặc 'Female'
    CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES tbl_user(id) ON DELETE SET NULL, -- Khóa ngoại tới tbl_user
    CONSTRAINT fk_teacher_dept FOREIGN KEY (dept_id) REFERENCES tbl_department(id) -- Khóa ngoại tới tbl_department
);