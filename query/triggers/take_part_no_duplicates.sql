-- Ad un progetto non può lavorare lo STESSO laboratorio più di una volta nello stesso momemento 
CREATE OR REPLACE FUNCTION take_part_no_duplicates() 
RETURNS trigger

language 'plpgsql'

AS $$

DECLARE
labs_number INTEGER := 0;

BEGIN

    SELECT COUNT(*) INTO labs_number
    FROM take_part
    WHERE NEW.cup = cup AND NEW.lab_code = lab_code AND end_date IS NULL;

    IF labs_number > 0 THEN
        RAISE EXCEPTION 'Il laboratorio % lavora già al progetto %', NEW.lab_code, NEW.cup; 
    END IF;
    RETURN NEW;
END; 
$$;

CREATE TRIGGER take_part_no_duplicates 
BEFORE INSERT ON take_part
FOR EACH ROW
EXECUTE FUNCTION take_part_no_duplicates();


