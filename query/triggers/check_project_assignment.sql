/*
Controlla che il referente scientifico sia senior e che il supervisore sia manager
*/
CREATE OR REPLACE FUNCTION check_project_assignment()
RETURNS trigger
language 'plpgsql'

AS $$

DECLARE

    referent_type base_emp.type%TYPE := ''; -- emp_type del referente di progetto
    manager_type base_emp.type%TYPE := ''; -- emp_type del supervisore del progetto

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

    EXCEPTION 
        -- Questo sqlstate si riferisce ad una violazione di un vincolo
        -- in questo caso se una delle due select non trova valori, assegna a referent_type (o manager_type) il valore NULL
        -- sono di tipo emp_type il quale ha un vincolo che gli impedisce di essere null
        -- viene dunque lanciata l'eccezione
        WHEN SQLSTATE '23514' THEN 
            RAISE EXCEPTION 'cf_scientific_referent or cf_manager does not exist';

END;
$$;


CREATE TRIGGER check_project_assignment
BEFORE INSERT OR UPDATE OF cf_scientific_referent, cf_manager ON project
FOR EACH ROW
EXECUTE FUNCTION check_project_assignment();