-- controlla che lo stesso impiegato non lavori già allo stesso progetto 
CREATE OR REPLACE FUNCTION check_works_on_date()
RETURNS trigger
language 'plpgsql'
AS $$
DECLARE
    max_expiration works_on.expiration%TYPE;
BEGIN
    SELECT MAX(expiration) INTO max_expiration
    FROM works_on
    WHERE cf = NEW.cf AND cup = NEW.cup;

    IF max_expiration IS NOT NULL   AND   NEW.hire_date < max_expiration THEN
        RAISE EXCEPTION '% sta gia lavorando per il progetto %', NEW.cf, NEW.cup;
    END IF;

    RETURN NEW;
END;
$$;


CREATE TRIGGER check_works_on_date
BEFORE INSERT ON works_on
FOR EACH ROW
EXECUTE FUNCTION check_works_on_date();