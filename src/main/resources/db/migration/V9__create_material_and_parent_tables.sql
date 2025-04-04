-- Tạo bảng TBL_MATERIAL
CREATE TABLE tbl_material (
    id SERIAL PRIMARY KEY,
    subject_id INTEGER,
    material_name VARCHAR(100) NOT NULL,
    material_type VARCHAR(50),
    file_path VARCHAR(200) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(100),
    CONSTRAINT fk_material_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id)
);

-- Tạo bảng TBL_PARENT
CREATE TABLE tbl_parent (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    parent_name VARCHAR(100),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    CONSTRAINT uk_parent_phone UNIQUE (phone_number),
    CONSTRAINT uk_parent_email UNIQUE (email),
    CONSTRAINT fk_parent_student FOREIGN KEY (student_id) REFERENCES tbl_student(id)
);