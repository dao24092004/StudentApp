-- Tạo bảng TBL_NOTIFICATION
CREATE TABLE tbl_notification (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    content VARCHAR(500),
    sender_id INTEGER,
    notification_type VARCHAR(20),
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_notification_type CHECK (notification_type IN ('General', 'Personal')),
    CONSTRAINT fk_notification_sender FOREIGN KEY (sender_id) REFERENCES tbl_user(id)
);

-- Tạo bảng TBL_NOTIFICATION_RECIPIENT
CREATE TABLE tbl_notification_recipient (
    id SERIAL PRIMARY KEY,
    notification_id INTEGER,
    recipient_id INTEGER,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_notif_recipient_notification FOREIGN KEY (notification_id) REFERENCES tbl_notification(id),
    CONSTRAINT fk_notif_recipient_user FOREIGN KEY (recipient_id) REFERENCES tbl_user(id)
);

-- Tạo bảng TBL_QUESTION
CREATE TABLE tbl_question (
    id SERIAL PRIMARY KEY,
    asker_id INTEGER,
    content VARCHAR(500),
    asked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_asker FOREIGN KEY (asker_id) REFERENCES tbl_user(id)
);

-- Tạo bảng TBL_ANSWER
CREATE TABLE tbl_answer (
    id SERIAL PRIMARY KEY,
    question_id INTEGER,
    answerer_id INTEGER,
    content VARCHAR(500),
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES tbl_question(id),
    CONSTRAINT fk_answer_answerer FOREIGN KEY (answerer_id) REFERENCES tbl_user(id)
);