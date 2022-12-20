-- Ad un progetto possono prendere parte contemporaneamente al massimo 3 laboratori 
CREATE OR REPLACE FUNCTION max_lab_in_project() 
RETURNS trigger

language 'plpgsql'

AS $$

DECLARE
labs_number INTEGER := 0;

BEGIN

    SELECT COUNT(*) INTO labs_number
    FROM take_part
    WHERE NEW.cup = cup AND end_date IS NULL;

    IF labs_number > 2 THEN
        RAISE EXCEPTION 'Al progetto lavorano gia 3 laboratori'; 
    END IF;
    RETURN NEW;
END; 
$$;

CREATE TRIGGER max_lab_in_project 
BEFORE INSERT ON take_part
FOR EACH ROW
EXECUTE FUNCTION max_lab_in_project();