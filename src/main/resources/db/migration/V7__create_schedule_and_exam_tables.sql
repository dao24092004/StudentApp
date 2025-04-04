-- Tạo bảng TBL_SCHEDULE
CREATE TABLE tbl_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER,
    day_of_week VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    CONSTRAINT check_schedule_times CHECK (start_time < end_time),
    CONSTRAINT fk_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id)
);

-- Tạo bảng TBL_EXAM_SCHEDULE
CREATE TABLE tbl_exam_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER,
    exam_date DATE,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    exam_room VARCHAR(50),
    CONSTRAINT fk_exam_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id)
);