-- Licenzia l'impiegato preso in input
CREATE OR REPLACE PROCEDURE fire(fired_cf IN career_log.cf%TYPE)
language 'plpgsql'
AS $$

DECLARE 

    new_role career_log.ex_role%TYPE := '';

BEGIN

    SELECT LL.new_role INTO new_role
    FROM last_log AS LL
    WHERE LL.cf = fired_cf;

    IF new_role = '' THEN
        RAISE EXCEPTION '% already fired', fired_cf;
    END IF;

    INSERT INTO career_log (ex_role, new_role, new_role_date, CF)
    VALUES (new_role, '', CURRENT_TIMESTAMP, fired_cf);
    
    EXCEPTION 
        WHEN SQLSTATE '23514' THEN
            RAISE EXCEPTION '% non trovato', fired_cf;

END;
$$;