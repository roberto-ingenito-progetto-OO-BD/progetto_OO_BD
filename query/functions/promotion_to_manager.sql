CREATE OR REPLACE PROCEDURE promotion_to_manager( v_cf_manager IN base_emp.cf%TYPE ) 
language 'plpgsql'

AS $$

    DECLARE
        emp_log last_log%ROWTYPE; 
    BEGIN
        -- recuperare l'utlimo log dell'impiegato in ingresso e verificare che non sia già manager 
        SELECT * INTO emp_log 
        FROM last_log 
        WHERE cf = v_cf_manager AND new_role <> 'manager'; 

        IF NOT FOUND THEN 
            RAISE EXCEPTION '% is already manager', v_cf_manager;

        -- se è senior ed è un referente scientifico (progetto) oppure un manager scientifico (laboratorio)
        ELSIF 
            emp_log.new_role = 'senior' AND
            (
                EXISTS(
                    SELECT * 
                    FROM project
                    WHERE cf_scientific_referent = v_cf_manager AND end_date IS NULL
                ) 
            OR
                EXISTS(
                    SELECT * 
                    FROM laboratory
                    WHERE cf_scientific_manager = v_cf_manager
                ) 
            )
        THEN
            RAISE EXCEPTION '% cannot be promoted, is scientific manager and/or scientific referent', v_cf_manager;
        END IF; 
            

        -- inserimento nuova tupla in carrer log
        INSERT INTO career_log(ex_role, new_role, new_role_date, CF)
        VALUES (emp_log.new_role, 'manager', CURRENT_TIMESTAMP, v_cf_manager);

        RAISE NOTICE '% promoted to manager', v_cf_manager; 
    END;
$$;
