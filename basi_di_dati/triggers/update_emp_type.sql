/*
Aggiorna il campo type di base_emp all'ultimo new_role di career_log
*/
CREATE OR REPLACE FUNCTION update_emp_type()
RETURNS trigger
language 'plpgsql'
AS $$

BEGIN
    UPDATE base_emp
    SET "type" = NEW.new_role
    WHERE cf = NEW.cf;

    RETURN NEW;
END; $$;

CREATE TRIGGER update_emp_type
AFTER INSERT ON career_log
FOR EACH ROW
EXECUTE FUNCTION update_emp_type();