-- Trigger check_user_role_student
CREATE OR REPLACE FUNCTION check_user_role_student()
RETURNS TRIGGER AS $$
DECLARE
    v_count INTEGER;
    v_role VARCHAR(20);
BEGIN
    -- Kiểm tra role của user
    SELECT role_name INTO v_role FROM tbl_role r
    JOIN tbl_user u ON u.role_id = r.id
    WHERE u.id = NEW.user_id;

    IF v_role = 'Teacher' THEN
        RAISE EXCEPTION 'This account is already a teacher, cannot be a student.';
    END IF;

    -- Kiểm tra xem user đã là teacher chưa
    SELECT COUNT(*) INTO v_count FROM tbl_teacher WHERE user_id = NEW.user_id;
    IF v_count > 0 THEN
        RAISE EXCEPTION 'This account is already assigned to a teacher.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_user_role_student
BEFORE INSERT OR UPDATE ON tbl_student
FOR EACH ROW EXECUTE FUNCTION check_user_role_student();