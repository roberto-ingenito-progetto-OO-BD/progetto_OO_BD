-- Ad un progetto possono prendere parte contemporaneamente al massimo 3 laboratori 
CREATE OR REPLACE FUNCTION max_lab_in_project() 
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

    -- conto in quanti laboratori lavora il progetto che voglio inserire
    -- quando end_date è null, il progetto sta ancora lavorando in quel laboratorio
    SELECT COUNT(*) INTO labs_number
    FROM take_part
    WHERE NEW.cup = cup AND end_date IS NULL;

    -- essendo un BEFORE INSERT, se lavora in più di 2 laboratori, 
    -- allora lancia un eccezione e impedisce l'inserimento
    IF labs_number > 2 THEN
        RAISE EXCEPTION 'Al progetto lavorano gia 3 laboratori'; 
    END IF;
    RETURN NEW;
END $$;

CREATE TRIGGER max_lab_in_project 
BEFORE INSERT ON take_part
FOR EACH ROW
EXECUTE FUNCTION max_lab_in_project();