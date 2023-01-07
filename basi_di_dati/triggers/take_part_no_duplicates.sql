-- Ad un progetto non può lavorare lo STESSO laboratorio più di una volta nello stesso momemento 
CREATE OR REPLACE FUNCTION take_part_no_duplicates() 
RETURNS trigger
language 'plpgsql'
AS $$

DECLARE
labs_number INTEGER := 0;

BEGIN
    -- se end_date non è null, significa che un progetto non lavora più in quel laboratorio
    -- quind posso inserire nella tabella
    IF NEW.end_date IS NOT NULL THEN
        RETURN NEW;
    END IF;

    -- conto quante volte il laboratorio che si vuole inserire
    -- lavora nello stesso progetto (end_date is null)
    SELECT COUNT(*) INTO labs_number
    FROM take_part
    WHERE NEW.cup = cup AND NEW.lab_code = lab_code AND end_date IS NULL;

    -- se il laboratorio lavora gia in quel progetto, impedisco l'inserimento
    IF labs_number > 0 THEN
        RAISE EXCEPTION 'Il laboratorio % lavora già al progetto %', NEW.lab_code, NEW.cup; 
    END IF;
    RETURN NEW;
END $$;

CREATE TRIGGER take_part_no_duplicates 
BEFORE INSERT ON take_part
FOR EACH ROW
EXECUTE FUNCTION take_part_no_duplicates();