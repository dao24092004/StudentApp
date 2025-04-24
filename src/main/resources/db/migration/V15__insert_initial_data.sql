-- Thêm dữ liệu cho tbl_role
INSERT INTO tbl_role (id, role_name, description) VALUES
(1, 'ADMIN', 'Quản trị viên hệ thống, có toàn quyền'),
(2, 'TEACHER', 'Giảng viên, quản lý lớp học và điểm số'),
(3, 'STUDENT', 'Sinh viên, xem thông tin lớp học và điểm số');

-- Thêm dữ liệu cho tbl_permission
INSERT INTO tbl_permission (permission_name, description, created_at, updated_at) VALUES
('USER_CREATE', 'Tạo người dùng mới', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER_UPDATE', 'Sửa thông tin người dùng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER_DELETE', 'Xóa người dùng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER_VIEW', 'Xem thông tin người dùng', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CLASS_CREATE', 'Tạo lớp học mới', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CLASS_UPDATE', 'Sửa thông tin lớp học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CLASS_DELETE', 'Xóa lớp học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CLASS_VIEW', 'Xem thông tin lớp học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SUBJECT_CREATE', 'Tạo môn học mới', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SUBJECT_UPDATE', 'Sửa thông tin môn học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SUBJECT_DELETE', 'Xóa môn học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SUBJECT_VIEW', 'Xem thông tin môn học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GRADE_CREATE', 'Nhập điểm số', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GRADE_UPDATE', 'Sửa điểm số', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GRADE_DELETE', 'Xóa điểm số', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GRADE_VIEW', 'Xem điểm số', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SCHEDULE_CREATE', 'Tạo lịch học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SCHEDULE_UPDATE', 'Sửa lịch học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SCHEDULE_DELETE', 'Xóa lịch học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SCHEDULE_VIEW', 'Xem lịch học', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NOTIFICATION_CREATE', 'Gửi thông báo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NOTIFICATION_VIEW', 'Xem thông báo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEPARTMENT_VIEW', 'Xem thông tin Khoa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEPARTMENT_CREATE', 'Tạo khoa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEPARTMENT_UPDATE', 'Cập nhật khoa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEPARTMENT_DELETE', 'Xóa khoa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERMISSION_CREATE', 'Create a new permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERMISSION_UPDATE', 'Update an existing permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERMISSION_DELETE', 'Delete a permission', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERMISSION_ASSIGN', 'Assign a permission to a role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PERMISSION_REVOKE', 'Revoke a permission from a role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROFILE_VIEW', 'Xem thông tin cá nhân', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('STUDENT_REGISTER', 'Sinh vien dang ky hoc phan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Gán permission cho role Admin (role_id = 1, có tất cả quyền)
INSERT INTO tbl_role_permission (role_id, permission_id, created_at, updated_at)
SELECT 1, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM tbl_permission;

-- Gán permission cho role Teacher (role_id = 2)
INSERT INTO tbl_role_permission (role_id, permission_id, created_at, updated_at)
SELECT 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM tbl_permission
WHERE permission_name IN (
    'USER_VIEW', 'CLASS_VIEW', 'SUBJECT_VIEW', 'GRADE_CREATE', 'GRADE_UPDATE', 'GRADE_VIEW',
    'SCHEDULE_VIEW', 'NOTIFICATION_CREATE', 'NOTIFICATION_VIEW', 'PROFILE_VIEW'
);

-- Gán permission cho role Student (role_id = 3)
INSERT INTO tbl_role_permission (role_id, permission_id, created_at, updated_at)
SELECT 3, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM tbl_permission
WHERE permission_name IN (
    'CLASS_VIEW', 'SUBJECT_VIEW', 'GRADE_VIEW', 'SCHEDULE_VIEW', 'NOTIFICATION_VIEW', 'PROFILE_VIEW','STUDENT_REGISTER'
);