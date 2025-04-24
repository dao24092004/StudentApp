-- Bảng tbl_registration: Lưu thông tin đăng ký học phần của sinh viên
CREATE TABLE tbl_registration (
    id SERIAL PRIMARY KEY,
    class_id INTEGER, -- ID của lớp học (liên kết với tbl_class)
    student_id INTEGER, -- ID của sinh viên (liên kết với tbl_student)
    semester_id INTEGER, -- ID của kỳ học (liên kết với tbl_semester)
    registration_date DATE DEFAULT CURRENT_DATE, -- Ngày đăng ký
    status VARCHAR(10), -- Trạng thái đăng ký: 'Registered', 'Cancelled'
    CONSTRAINT uk_registration UNIQUE (class_id, student_id), -- Ràng buộc: mỗi sinh viên chỉ được đăng ký 1 lớp 1 lần
    CONSTRAINT check_registration_status CHECK (status IN ('Registered', 'Cancelled')), -- Ràng buộc: trạng thái chỉ được là 'Registered' hoặc 'Cancelled'
    CONSTRAINT fk_registration_class FOREIGN KEY (class_id) REFERENCES tbl_class(id), -- Khóa ngoại tới tbl_class
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES tbl_student(id), -- Khóa ngoại tới tbl_student
    CONSTRAINT fk_registration_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id) -- Khóa ngoại tới tbl_semester
);

CREATE TABLE tbl_teacher_subject_registration (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    semester_id INTEGER NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id),
    FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
    FOREIGN KEY (semester_id) REFERENCES tbl_semester(id),
    UNIQUE (teacher_id, subject_id, semester_id) -- Giáo viên chỉ đăng ký 1 môn 1 lần trong 1 kỳ
);
-- Bảng tbl_attendance: Lưu thông tin điểm danh của sinh viên
CREATE TABLE tbl_attendance (
    id SERIAL PRIMARY KEY,
    class_id INTEGER, -- ID của lớp học
    student_id INTEGER, -- ID của sinh viên
    attendance_date DATE NOT NULL, -- Ngày điểm danh
    status VARCHAR(20), -- Trạng thái: 'Present', 'Absent', 'Late'
    note VARCHAR(100), -- Ghi chú (nếu có)
    CONSTRAINT uk_attendance UNIQUE (class_id, student_id, attendance_date), -- Ràng buộc: mỗi sinh viên chỉ điểm danh 1 lần/ngày/lớp
    CONSTRAINT check_attendance_status CHECK (status IN ('Present', 'Absent', 'Late')), -- Ràng buộc: trạng thái chỉ được là 'Present', 'Absent', 'Late'
    CONSTRAINT fk_attendance_class FOREIGN KEY (class_id) REFERENCES tbl_class(id), -- Khóa ngoại tới tbl_class
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES tbl_student(id) -- Khóa ngoại tới tbl_student
);

-- Bảng tbl_grade: Lưu điểm của sinh viên cho từng lớp
CREATE TABLE tbl_grade (
    id SERIAL PRIMARY KEY,
    class_id INTEGER, -- ID của lớp học
    student_id INTEGER, -- ID của sinh viên
    attendance_score NUMERIC(5,2), -- Điểm chuyên cần
    exam_score NUMERIC(5,2), -- Điểm thi
    final_score NUMERIC(5,2), -- Điểm tổng kết
    note VARCHAR(100), -- Ghi chú (nếu có)
    CONSTRAINT check_scores CHECK (attendance_score BETWEEN 0 AND 10 AND exam_score BETWEEN 0 AND 10 AND final_score BETWEEN 0 AND 10), -- Ràng buộc: điểm từ 0-10
    CONSTRAINT fk_grade_class FOREIGN KEY (class_id) REFERENCES tbl_class(id), -- Khóa ngoại tới tbl_class
    CONSTRAINT fk_grade_student FOREIGN KEY (student_id) REFERENCES tbl_student(id) -- Khóa ngoại tới tbl_student
);
-- Bảng tbl_grade_appeal: Lưu thông tin phúc khảo điểm của sinh viên
CREATE TABLE tbl_grade_appeal (
    id SERIAL PRIMARY KEY,
    student_id INTEGER, -- ID của sinh viên
    class_id INTEGER, -- ID của lớp học
    reason VARCHAR(255), -- Lý do phúc khảo
    status VARCHAR(20) DEFAULT 'Pending', -- Trạng thái: 'Pending', 'Approved', 'Rejected'
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian nộp đơn
    processed_at TIMESTAMP, -- Thời gian xử lý
    CONSTRAINT check_appeal_status CHECK (status IN ('Pending', 'Approved', 'Rejected')), -- Ràng buộc: trạng thái chỉ được là 'Pending', 'Approved', 'Rejected'
    CONSTRAINT fk_appeal_student FOREIGN KEY (student_id) REFERENCES tbl_student(id), -- Khóa ngoại tới tbl_student
    CONSTRAINT fk_appeal_class FOREIGN KEY (class_id) REFERENCES tbl_class(id) -- Khóa ngoại tới tbl_class
);

-- Bảng tbl_grade_history: Lưu lịch sử thay đổi điểm
CREATE TABLE tbl_grade_history (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER, -- ID của giáo viên thực hiện thay đổi
    grade_id INTEGER, -- ID của bản ghi điểm (liên kết với tbl_grade)
    old_score NUMERIC(5,2), -- Điểm cũ
    new_score NUMERIC(5,2), -- Điểm mới
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian thay đổi
    reason VARCHAR(225), -- Lý do thay đổi
    CONSTRAINT fk_grade_history_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id), -- Khóa ngoại tới tbl_teacher
    CONSTRAINT fk_grade_history_grade FOREIGN KEY (grade_id) REFERENCES tbl_grade(id) -- Khóa ngoại tới tbl_grade
);