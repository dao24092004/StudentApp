

CREATE TABLE tbl_class (
    id SERIAL PRIMARY KEY,
    class_code VARCHAR(20) NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    subject_id INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    class_group_id INTEGER NOT NULL,
    semester_id INTEGER NOT NULL,
    max_students INTEGER NOT NULL CHECK (max_students > 0),
    start_date DATE,
    end_date DATE,
    room_type VARCHAR(20) NOT NULL CHECK (room_type IN ('theory', 'lab', 'drama')),
    shift VARCHAR(10) CHECK (shift IN ('Morning', 'Afternoon', 'Any')),
    priority INTEGER DEFAULT 10 CHECK (priority >= 0),
    period_length INTEGER NOT NULL DEFAULT 1 CHECK (period_length BETWEEN 1 AND 3),
    theory_periods INTEGER NOT NULL DEFAULT 0 CHECK (theory_periods >= 0),
    practical_periods INTEGER NOT NULL DEFAULT 0 CHECK (practical_periods >= 0),
    class_duration INTEGER NOT NULL DEFAULT 20 CHECK (class_duration > 0),
    CONSTRAINT uk_class_code UNIQUE (class_code),
    CONSTRAINT check_class_dates CHECK (start_date < end_date),
    CONSTRAINT check_total_periods CHECK (theory_periods + practical_periods > 0),
    CONSTRAINT fk_class_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
    CONSTRAINT fk_class_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id),
    CONSTRAINT fk_class_group FOREIGN KEY (class_group_id) REFERENCES tbl_class_group(id),
    CONSTRAINT fk_class_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);


CREATE TABLE tbl_room (
    id SERIAL PRIMARY KEY,
    room_code VARCHAR(20) NOT NULL, -- Mã phòng (ví dụ: A101)
    room_name VARCHAR(100) NOT NULL, -- Tên phòng
    room_type VARCHAR(20) NOT NULL CHECK (room_type IN ('theory', 'practical', 'lab')), -- Loại phòng
    capacity INTEGER NOT NULL CHECK (capacity > 0), -- Sức chứa
    dept_id INTEGER, -- Khoa quản lý phòng (tùy chọn)
    CONSTRAINT uk_room_code UNIQUE (room_code),
    CONSTRAINT fk_room_dept FOREIGN KEY (dept_id) REFERENCES tbl_department(id)
);



CREATE TABLE tbl_teacher_preference (
    id SERIAL PRIMARY KEY,
    teacher_id INTEGER NOT NULL,
    week INTEGER NOT NULL CHECK (week BETWEEN 1 AND 20),
    day_of_week VARCHAR(3) NOT NULL CHECK (day_of_week IN ('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')),
    slot INTEGER NOT NULL CHECK (slot BETWEEN 1 AND 10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_teacher_preference_teacher FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(id),
    CONSTRAINT uk_teacher_week_day_slot UNIQUE (teacher_id, week, day_of_week, slot)
);