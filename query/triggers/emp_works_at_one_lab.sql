CREATE OR REPLACE FUNCTION emp_works_at_one_lab() 
RETURNS trigger
language 'plpgsql'

AS $emp_works_at_one_lab$

BEGIN 
    IF
        EXISTS(
            SELECT *
            FROM works_at 
            WHERE cf_base_emp = NEW.cf_base_emp AND end_date IS NULL
        )
    THEN
        RAISE EXCEPTION 'emp is already working into a lab'; 
    END IF; 

    RETURN NEW;

END;
$emp_works_at_one_lab$; 

CREATE OR REPLACE TRIGGER emp_works_at_one_lab 
BEFORE INSERT OR UPDATE ON works_at
FOR EACH ROW 
WHEN (NEW.end_date IS NULL)
EXECUTE FUNCTION emp_works_at_one_lab();