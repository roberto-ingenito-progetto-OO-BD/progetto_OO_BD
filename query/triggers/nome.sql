CREATE OR REPLACE FUNCTION nome()
RETURNS trigger
language 'plpgsql'

AS $$

referent_type base_emp.type%TYPE; -- emp_type del referente di progetto

manager_type base_emp.type%TYPE; -- emp_type del supervisore del progetto

BEGIN

    SELECT "type" INTO referent_type
    FROM base_emp
    WHERE cf = NEW.cf_scientific_referent;

    SELECT "type" INTO manager_type
    FROM base_emp
    WHERE cf = NEW.cf_manager;

    IF referent_type <> 'senior' THEN
        RAISE EXCEPTION '% is not senior', NEW.cf_scientific_referent;
    END IF;

    IF manager_type <> 'manager' THEN
        RAISE EXCEPTION '% is not manager', NEW.cf_manager;
    END IF;

    RETURN NEW;

END;
$$;


CREATE TRIGGER nome
BEFORE INSERT ON project
FOR EACH ROW
EXECUTE FUNCTION nome();