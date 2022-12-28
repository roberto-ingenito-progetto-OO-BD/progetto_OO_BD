-- Licenzia l'impiegato preso in input
CREATE OR REPLACE PROCEDURE fire(fired_cf IN career_log.cf%TYPE)
language 'plpgsql'
AS $$

DECLARE 

    ex_role career_log.ex_role%TYPE := '';

BEGIN

    SELECT LL.ex_role INTO ex_role
    FROM last_log AS LL
    WHERE LL.cf = fired_cf;

    INSERT INTO career_log (ex_role, new_role, new_role_date, CF)
    VALUES (ex_role, '', CURRENT_DATE, fired_cf);

END;
$$;