-- Tạo bảng TBL_BLACKLIST_TOKEN
CREATE TABLE tbl_blacklist_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT uk_blacklist_token UNIQUE (token)
);

-- Tạo bảng TBL_REFRESH_TOKEN
CREATE TABLE tbl_refresh_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_refresh_token UNIQUE (token),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

-- Tạo bảng TBL_RESET_PASSWORD_TOKEN
CREATE TABLE tbl_reset_password_token (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    token VARCHAR(255),
    expiry_time TIMESTAMP,
    CONSTRAINT fk_reset_token_user FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

-- Tạo bảng TBL_EMAIL_CONFIG
CREATE TABLE tbl_email_config (
    id SERIAL PRIMARY KEY,
    smtp_server VARCHAR(100),
    port INTEGER,
    email VARCHAR(100),
    password VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Active',
    CONSTRAINT check_email_status CHECK (status IN ('Active', 'Inactive'))
);

-- Tạo bảng TBL_PAYMENT_CONFIG
CREATE TABLE tbl_payment_config (
    id SERIAL PRIMARY KEY,
    gateway_name VARCHAR(50),
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Active',
    CONSTRAINT check_payment_status CHECK (status IN ('Active', 'Inactive'))
);