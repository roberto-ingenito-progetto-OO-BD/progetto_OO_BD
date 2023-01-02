CREATE OR REPLACE PROCEDURE revoke_manager( v_cf_manager IN base_emp.cf%TYPE ) 
language 'plpgsql'

AS $$

    DECLARE

        emp_log last_log%ROWTYPE; 
        v_ex_role career_log.ex_role%TYPE := '';
        hire_date career_log.new_role_date%TYPE; 
        difference_years INTEGER; 

    BEGIN
        
        IF 
            EXISTS(
                SELECT *
                FROM project
                WHERE cf_manager = v_cf_manager AND end_date IS NULL
            )
        THEN
            RAISE EXCEPTION '% cannot be declassed, is a project manager', v_cf_manager;
        END IF;

        -- recuperare l'utlimo log dell'impiegato in ingresso e verificare che sia manager 
        SELECT * INTO emp_log 
        FROM last_log 
        WHERE cf = v_cf_manager AND new_role = 'manager'; 

        IF NOT FOUND THEN 
            RAISE EXCEPTION '% is not manager', v_cf_manager;
        ELSE 
            -- l'impiegato è un manager e deve essere declassato al ruolo precedente 
            -- selezionare la data di assunzione
            SELECT new_role_date INTO hire_date 
            FROM career_log
            WHERE (ex_role, new_role) = ('', 'junior') AND cf = v_cf_manager;

            -- calcolare gli anni di lavoro 
            difference_years := EXTRACT( YEAR FROM AGE(CURRENT_TIMESTAMP, hire_date));
            

            -- assegnare ex_role in base agli anni di lavoro 
            IF difference_years >= 7 THEN 
                v_ex_role := 'senior'; 
            ELSIF difference_years >= 3 AND difference_years < 7 THEN 
                v_ex_role := 'middle'; 
            ELSE 
                v_ex_role := 'junior'; 
            END IF; 

            -- inserimento nuova tupla in carrer log
            INSERT INTO career_log(ex_role, new_role, new_role_date, CF)
            VALUES ('manager', v_ex_role, CURRENT_TIMESTAMP, v_cf_manager);

            RAISE NOTICE '% declassed to %', v_cf_manager, v_ex_role; 

        END IF; 

    END;
$$;
