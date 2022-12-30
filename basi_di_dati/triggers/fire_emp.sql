CREATE OR REPLACE FUNCTION fire_emp()
RETURNS trigger
language 'plpgsql'

AS $$
DECLARE

BEGIN
    -- se l'impiegato da licenziare è un referente scientifico oppure manager di progetto
    -- e il progetto è in corso (end_date NULL)
    -- OPPURE
    -- è manager scientifico di un laboratorio
    -- non posso licenziarlo, ma bisognerà prima cambiare
    IF 
    EXISTS(
        SELECT *
        FROM project
        WHERE (cf_manager = NEW.cf OR cf_scientific_referent = NEW.cf)
                AND end_date IS NULL;
    ) OR
    EXISTS(
        SELECT *
        FROM laboratory
        WHERE cf_scientific_manager = NEW.cf
    )    
    THEN
        RAISE EXCEPTION '% lavora come manager o referente in un progetto o laboratorio', NEW.cf;
    END IF;

    -- aggiorna l'end date dei laboratori in cui lavora, alla data del licenziamento
    -- ciò significa che non lavora più in quel laboratorio
    UPDATE works_at
    SET end_date = NEW.new_role_date
    WHERE cf_base_emp = NEW.cf AND end_date IS NULL;

    RETURN NEW;
END;
$$;


CREATE TRIGGER fire_emp
BEFORE INSERT ON career_log
FOR EACH ROW
WHEN (NEW.new_role = '')
EXECUTE FUNCTION fire_emp();
