-- Tạo bảng TBL_TUITION_PAYMENT
CREATE TABLE tbl_tuition_payment (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    amount NUMERIC(10,2),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50),
    note VARCHAR(100),
    CONSTRAINT fk_tuition_payment_student FOREIGN KEY (student_id) REFERENCES tbl_student(id)
);

-- Tạo bảng TBL_TUITION_DEBT
CREATE TABLE tbl_tuition_debt (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    semester_id INTEGER,
    debt_amount NUMERIC(10,2),
    status VARCHAR(20) DEFAULT 'Unpaid',
    CONSTRAINT check_debt_status CHECK (status IN ('Unpaid', 'Paid')),
    CONSTRAINT fk_debt_student FOREIGN KEY (student_id) REFERENCES tbl_student(id),
    CONSTRAINT fk_debt_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);

-- Tạo bảng TBL_FINAL_EVALUATION
CREATE TABLE tbl_final_evaluation (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    class_id INTEGER,
    evaluation_comment VARCHAR(255),
    evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_final_eval_student FOREIGN KEY (student_id) REFERENCES tbl_student(id),
    CONSTRAINT fk_final_eval_class FOREIGN KEY (class_id) REFERENCES tbl_class(id)
);

-- Tạo bảng TBL_CONDUCT_EVALUATION
CREATE TABLE tbl_conduct_evaluation (
    id SERIAL PRIMARY KEY,
    student_id INTEGER,
    semester_id INTEGER,
    conduct_score NUMERIC(5,2),
    conduct_comment VARCHAR(255),
    evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conduct_student FOREIGN KEY (student_id) REFERENCES tbl_student(id),
    CONSTRAINT fk_conduct_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);