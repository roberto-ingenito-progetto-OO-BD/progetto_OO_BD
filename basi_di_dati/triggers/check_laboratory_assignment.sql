/*
Controlla che il manager scientifico del laboratorio sia senior
*/
CREATE OR REPLACE FUNCTION check_laboratory_assignment()
RETURNS trigger
language 'plpgsql'

AS $$

DECLARE

    scientific_manager_type base_emp.type%TYPE := ''; -- emp_type del manager di laboratorio

BEGIN

    SELECT "type" INTO scientific_manager_type
    FROM base_emp
    WHERE cf = NEW.cf_scientific_manager;
    
    IF scientific_manager_type <> 'senior' THEN
        RAISE EXCEPTION '% is not senior', NEW.cf_scientific_manager;
    END IF;

    RETURN NEW;

    EXCEPTION 
        -- Questo sqlstate si riferisce ad una violazione di un vincolo
        -- in questo caso se una delle due select non trova valori, assegna a scientific_manager_type il valore NULL
        -- sono di tipo emp_type il quale ha un vincolo che gli impedisce di essere NULL
        -- viene dunque lanciata l'eccezione
        WHEN SQLSTATE '23514' THEN 
            RAISE EXCEPTION 'cf_scientific_manager does not exist';

END;
$$;


CREATE TRIGGER check_laboratory_assignment
BEFORE INSERT OR UPDATE OF cf_scientific_manager ON laboratory
FOR EACH ROW
EXECUTE FUNCTION check_laboratory_assignment();