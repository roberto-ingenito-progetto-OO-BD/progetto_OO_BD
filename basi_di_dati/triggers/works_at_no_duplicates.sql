-- Ad un laboratorio non può lavorare lo STESSO impiegato più di una volta nello stesso momemento 
CREATE OR REPLACE FUNCTION works_at_no_duplicates() 
RETURNS trigger

language 'plpgsql'

AS $$

DECLARE
    emps_number INTEGER := 0;

BEGIN
    -- se end_date non è null, significa che non lavoro più in quel laboratorio
    -- quind posso inserire nella tabella
    IF NEW.end_date IS NOT NULL THEN
        RETURN NEW;
    END IF;

    -- conto quante volte l'impiegato che si vuole inserire
    -- lavora nello stesso laboratorio (end_date is null)
    SELECT COUNT(*) INTO emps_number
    FROM works_at
    WHERE NEW.cf_base_emp = cf_base_emp AND 
          NEW.lab_code = lab_code AND 
          end_date IS NULL;

    -- se l'impiegato lavora gia in quel laboratorio, impedisco l'inserimento
    IF emps_number > 0 THEN
        RAISE EXCEPTION 'L''impiegato % lavora già al laboratorio %', NEW.cf_base_emp, NEW.lab_code; 
    END IF;
    RETURN NEW;
END $$;

CREATE TRIGGER works_at_no_duplicates 
BEFORE INSERT ON works_at
FOR EACH ROW
EXECUTE FUNCTION works_at_no_duplicates();