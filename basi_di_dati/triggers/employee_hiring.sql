-- Quando un impiegato è assunto (ovvero quando viene inserito nella tabella base_emp)
-- viene inserito un log in career_log 
CREATE OR REPLACE FUNCTION employee_hiring()
RETURNS trigger
language 'plpgsql'
AS $$
BEGIN
    INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
    VALUES ('', 'junior', CURRENT_TIMESTAMP, NEW.cf);

    RETURN NEW;
END; $$;


CREATE TRIGGER employee_hiring
AFTER INSERT ON base_emp
FOR EACH ROW
EXECUTE FUNCTION employee_hiring();
