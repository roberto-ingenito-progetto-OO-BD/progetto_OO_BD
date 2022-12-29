-- controlla il grado di anzianità degli impiegati e li aggiorna nel caso
CREATE OR REPLACE PROCEDURE check_seniority()
language 'plpgsql'
AS $$

DECLARE
difference_years INTEGER;
emp_current_role career_log.new_role%TYPE := '';

-- prendo l'ultimo log dove new_role = junior
-- escludendo quelli che attualmente sono manager o licenzianti
log_cursor CURSOR FOR
    SELECT *
    FROM career_log AS C

    -- escludo quelli attualmente manager o licenziati
    WHERE C.cf IN(

    	-- prendo l'ULTIMO LOG di ogni impiegato 
        -- escludendo i log dove new_role è manager oppure '' (licenziato) 
    	SELECT C1.cf    
    	FROM last_log AS C1
    	WHERE 
            C1.new_role <> 'manager' AND    -- escludo i manager
    	  	C1.new_role <> ''   -- escludo i licenziati
    ) AND 
	
	-- seleziono l'ultimo log dov'è stato assunto
    C.new_role_date = ( 
        SELECT MAX(C1.new_role_date)
    	FROM career_log AS C1
    	WHERE C.cf = C1.cf AND (C1.ex_role,C1.new_role) = ('','junior')
    )
;

BEGIN
    FOR item IN log_cursor LOOP
        difference_years := EXTRACT( YEAR FROM AGE(CURRENT_TIMESTAMP, item.new_role_date));

        -- prendo il ruolo attuale dell'impiegato
        SELECT new_role INTO emp_current_role 
        FROM career_log 
        WHERE cf = item.cf AND
	        new_role_date = (
		        SELECT MAX(C2.new_role_date)
		        FROM career_log AS C2
		        WHERE C2.cf = item.cf
	        );

        -- lavora da più di 7 anni e NON è già senior
        -- avanza a senior
        IF difference_years >= 7 AND emp_current_role <> 'senior' THEN
            INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
            VALUES ('middle', 'senior', CURRENT_TIMESTAMP, item.cf);

        -- lavora da 3 anni (o più) ma meno di 7 e NON è già middle
        -- avanza a middle
        ELSIF difference_years >= 3 AND difference_years < 7 AND emp_current_role <> 'middle' THEN
            INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
            VALUES ('junior', 'middle', CURRENT_TIMESTAMP, item.cf);
        END IF;

    END LOOP;
END;
$$;


/*
Si suppone che il controllo dell'avanzamento di grado verrà fatto una volta al mese dopo il giorno di paga.
Cosi facendo, i dipendenti che avanzano di grado iniziano a lavorare con il nuovo grado (e il nuovo stipendio) subito dopo l'ultima paga.
Quest'idea nasce dal fatto che l'inserimento viene fatto sulla data corrente.
    Avendo pensato che questa funzione venisse eseguita dall'amministratore del database una tantum, avrebbe poi creato problemi con l'avanzamento di grado e la corretta data dell'avanzamento.
Cosi invece si sfora al massimo di un mese.

----------------------------------------------------

escludo i manager e i licenziati

----------------------------------------------------

nella funzione per declassare, bisogna controllare se l'impiegato (nel frattempo) ha avuto uno avanzamento di ruolo per anzianità e nel caso inserirlo

----------------------------------------------------

prendo tutti gli impiegati, inclusi quelli ri-assunti
devo quindi stare attento a fare le query solo sui log dopo l'ultimo licenziamento (nel caso fosse stato riassunto)

----------------------------------------------------

parto dal new_role junior, ovvero nel momento in cui è stato assunto. Da lì calcolo quanto tempo è passato per poi aggiornare il grado di anzianità
*/
 
