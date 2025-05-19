CREATE TABLE tbl_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    semester_id INTEGER NOT NULL,
    week INTEGER NOT NULL CHECK (week BETWEEN 1 AND 20),
    day_of_week VARCHAR(3) NOT NULL CHECK (day_of_week IN ('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')),
    slot INTEGER NOT NULL CHECK (slot BETWEEN 1 AND 10),
    room_id INTEGER NOT NULL,
    period INTEGER NOT NULL DEFAULT 1 CHECK (period BETWEEN 1 AND 10),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    CONSTRAINT fk_schedule_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
    CONSTRAINT fk_schedule_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id),
    CONSTRAINT fk_schedule_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id),
    CONSTRAINT fk_schedule_room FOREIGN KEY (room_id) REFERENCES tbl_room(id),
    CONSTRAINT unique_teacher_slot UNIQUE (teacher_id, semester_id, week, day_of_week, slot),
    CONSTRAINT unique_room_slot UNIQUE (room_id, semester_id, week, day_of_week, slot),
    CONSTRAINT check_times CHECK (start_time < end_time)
);

CREATE TABLE tbl_exam_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER NOT NULL,
    week INTEGER NOT NULL CHECK (week BETWEEN 1 AND 20),
    exam_date DATE NOT NULL,
    period INTEGER NOT NULL CHECK (period BETWEEN 1 AND 10),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    room_id INTEGER NOT NULL,
    CONSTRAINT fk_exam_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    CONSTRAINT fk_exam_schedule_room FOREIGN KEY (room_id) REFERENCES tbl_room(id),
    CONSTRAINT check_exam_times CHECK (start_time < end_time)
);
CREATE TABLE tbl_time_window (
    id_class INTEGER NOT NULL,
    week INTEGER NOT NULL CHECK (week BETWEEN 1 AND 20),
    day_of_week VARCHAR(10) NOT NULL CHECK (day_of_week IN ('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')),
    period INTEGER NOT NULL CHECK (period BETWEEN 1 AND 10),
    PRIMARY KEY (id_class, week, day_of_week, period),
    CONSTRAINT fk_time_window_class FOREIGN KEY (id_class) REFERENCES tbl_class(id)
);