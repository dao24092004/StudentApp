-- Bảng tbl_schedule: Lưu lịch học chi tiết của từng lớp
CREATE TABLE tbl_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER, -- ID của lớp học (liên kết với tbl_class)
    subject_id INTEGER, -- ID của môn học (liên kết với tbl_subject), vì một ngày có thể học 2 môn
    day_of_week VARCHAR(50) NOT NULL, -- Ngày trong tuần (Mon, Tue, ...)
    slot INTEGER CHECK (slot IN (1, 2)), -- Ca học: 1 (sáng), 2 (chiều)
    period INTEGER CHECK (period BETWEEN 1 AND 5), -- Tiết học: 1-5 (mỗi tiết 45 phút)
    start_time TIMESTAMP NOT NULL, -- Thời gian bắt đầu của tiết học
    end_time TIMESTAMP NOT NULL, -- Thời gian kết thúc của tiết học
    CONSTRAINT check_schedule_periods CHECK (start_time < end_time), -- Ràng buộc: thời gian bắt đầu phải nhỏ hơn thời gian kết thúc
    CONSTRAINT fk_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id), -- Khóa ngoại tới tbl_class
    CONSTRAINT fk_schedule_subject FOREIGN KEY (subject_id) REFERENCES tbl_subject(id) -- Khóa ngoại tới tbl_subject
);

-- Bảng tbl_exam_schedule: Lưu lịch thi của từng lớp
CREATE TABLE tbl_exam_schedule (
    id SERIAL PRIMARY KEY,
    class_id INTEGER, -- ID của lớp học (liên kết với tbl_class)
    exam_date DATE, -- Ngày thi
    slot INTEGER CHECK (slot IN (1, 2)), -- Ca thi: 1 (sáng), 2 (chiều)
    period INTEGER CHECK (period BETWEEN 1 AND 5), -- Tiết thi: 1-5 (mỗi tiết 45 phút, tương tự lịch học)
    start_time TIMESTAMP, -- Thời gian bắt đầu của tiết thi
    end_time TIMESTAMP, -- Thời gian kết thúc của tiết thi
    exam_room VARCHAR(50), -- Phòng thi
    CONSTRAINT fk_exam_schedule_class FOREIGN KEY (class_id) REFERENCES tbl_class(id), -- Khóa ngoại tới tbl_class
    CONSTRAINT check_exam_times CHECK (start_time < end_time) -- Ràng buộc: thời gian bắt đầu phải nhỏ hơn thời gian kết thúc
);

-- Bảng tbl_time_window: Lưu khung thời gian khả dụng của lớp học
CREATE TABLE tbl_time_window (
    id_class INTEGER, -- ID của lớp học (liên kết với tbl_class)
    day_of_week VARCHAR(50) CHECK (day_of_week IN ('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')), -- Ngày trong tuần
    slot INTEGER CHECK (slot IN (1, 2, 3)), -- Ca học: 1 (sáng), 2 (chiều), 3 (tối, không dùng)
    created_date DATE NOT NULL DEFAULT CURRENT_DATE, -- Ngày tạo khung thời gian
    PRIMARY KEY (id_class, day_of_week, slot), -- Khóa chính: mỗi lớp chỉ có 1 khung thời gian/ngày/ca
    FOREIGN KEY (id_class) REFERENCES tbl_class(id) -- Khóa ngoại tới tbl_class
);