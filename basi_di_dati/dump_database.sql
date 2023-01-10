CREATE DOMAIN salary_type AS DECIMAL(8,2);
CREATE DOMAIN emp_type AS VARCHAR(10) 
CONSTRAINT emp_type_check CHECK 
    (VALUE IN ('', 'junior', 'middle', 'senior', 'manager') 
        AND VALUE IS NOT NULL);
CREATE DOMAIN password_type AS VARCHAR(100);
CREATE DOMAIN name_type AS VARCHAR(30);
CREATE DOMAIN cup_type AS VARCHAR(15)
CONSTRAINT check_cup_type CHECK ( length(VALUE) = 15);
CREATE DOMAIN equipment_name_type 
AS VARCHAR(30);
CREATE DOMAIN cf_type AS VARCHAR(16)
CONSTRAINT cf_type_length CHECK (length(VALUE) = 16);
CREATE TABLE base_emp(
    CF          cf_type,
    first_name  name_type     NOT NULL,
    last_name   name_type     NOT NULL,
    email       VARCHAR(100)  NOT NULL,
    passw       password_type NOT NULL,
    birth_date  DATE          NOT NULL,
    "type"      emp_type      NOT NULL, 
    "role"      VARCHAR(30)   NOT NULL,
    salary      salary_type   NOT NULL,

    CONSTRAINT emp_email_unique UNIQUE (email),
    CONSTRAINT base_emp_pk PRIMARY KEY (CF)
);

-- INSERT INTO base_emp VALUES 
-- ('NGNRRT02M29H931J', 'Roberto', 'Ingenito', 'ingenitoroby@gmail.com', 'password', '29/08/2002', 'manager', 'developer', 10000);

CREATE TABLE career_log(
    ex_role         emp_type,
    new_role        emp_type,
    new_role_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CF              cf_type,
    
    -- foreign key constraint
    CONSTRAINT emp_career_fk FOREIGN KEY (CF) REFERENCES base_emp(CF)
        ON DELETE CASCADE 
        ON UPDATE CASCADE,

    CONSTRAINT check_new_grade CHECK (
        (ex_role, new_role) IN ( 
            ('', 'junior'), -- assunto
            ('junior', 'middle'), 
            ('middle', 'senior')
        ) OR

        -- licenziato
        ex_role <> '' AND new_role = '' 
        OR

        -- avanzamento a manger
        ex_role <> '' AND ex_role <> 'manager' AND new_role = 'manager'
        OR 

        -- declassato
        --
        -- un trigger verificherà che il new_role sia quello del log precedente
        -- se da middle è diventato manager e poi viene declassato, 
        --    sarà declassato a middle 
        ex_role = 'manager' AND new_role <> 'manager'
    )
);


/* 
check_new_grade, tutte le possibili combinazioni

('', ''); -- no
('', 'junior');
('', 'middle'); -- no
('', 'senior'); -- no
('', 'manager'); -- no

('junior', '');
('junior', 'junior'); -- no
('junior', 'middle');
('junior', 'senior'); -- no
('junior', 'manager');

('middle', '');
('middle', 'junior'); -- no
('middle', 'middle'); -- no
('middle', 'senior');
('middle', 'manager');

('senior', '');
('senior', 'junior'); -- no
('senior', 'middle'); -- no
('senior', 'senior'); -- no
('senior', 'manager');

('manager', '');
('manager', 'junior');
('manager', 'middle');
('manager', 'senior'); 
('manager', 'manager'); -- no
*/
CREATE TABLE project_salaried(
    CF          cf_type,
    first_name  name_type       NOT NULL,
    last_name   name_type       NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    passw       password_type   NOT NULL,
    "role"      VARCHAR(30)     NOT NULL,
    birth_date  DATE            NOT NULL,

    CONSTRAINT project_salaried_email_unique UNIQUE (email),
    CONSTRAINT project_salaried_pk PRIMARY KEY (CF)
);
CREATE TABLE site(
    site_number     SERIAL,
    name            VARCHAR(40) NOT NULL,
    street          VARCHAR(30) NOT NULL,
    street_number   VARCHAR(10) NOT NULL,
    postal_code     VARCHAR(10) NOT NULL,
    city            VARCHAR(20) NOT NULL,

    CONSTRAINT site_pk PRIMARY KEY (site_number)
);
CREATE TABLE project(
    CUP             cup_type,
    funds           DECIMAL(10,2)   NOT NULL,
    "name"          VARCHAR(50)     UNIQUE,
    description     VARCHAR(100)    NOT NULL,
    start_date      DATE            NOT NULL DEFAULT CURRENT_DATE,
    end_date        DATE,
    deadline        DATE,

    -- foreign key
    cf_manager              cf_type NOT NULL,
    cf_scientific_referent  cf_type NOT NULL,

    CONSTRAINT project_pk PRIMARY KEY (CUP),    

    CONSTRAINT check_deadline CHECK ( deadline IS NULL OR deadline > start_date ),
    CONSTRAINT check_end_date CHECK ( end_date IS NULL OR end_date > start_date ),

    CONSTRAINT fk_cf_manager FOREIGN KEY (CF_manager) REFERENCES base_emp(CF)
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_cf_scientific_referent    FOREIGN KEY (CF_scientific_referent) 
                                            REFERENCES base_emp(CF)
        ON UPDATE CASCADE
);
CREATE TABLE laboratory(
    lab_code    SERIAL,
    lab_name    VARCHAR(200)    NOT NULL,
    topic       VARCHAR(1000)   NOT NULL,

    -- foreign keys
    site_number             SERIAL  NOT NULL,
    cf_scientific_manager   cf_type NOT NULL,

    CONSTRAINT lab_pk                   PRIMARY KEY (lab_code),
    
    CONSTRAINT site_number_fk           FOREIGN KEY (site_number)             
                                        REFERENCES site(site_number),

    CONSTRAINT cf_scientific_manager_fk FOREIGN KEY (cf_scientific_manager)   
                                        REFERENCES base_emp(cf)
);
CREATE TABLE works_on (
    pay         salary_type NOT NULL,
    hire_date   DATE        NOT NULL,
    expiration  DATE        NOT NULL,

    -- foreign keys
    CF          cf_type,
    CUP         cup_type,

    CONSTRAINT check_expiration_date CHECK ( expiration > hire_date ),

    CONSTRAINT fk_cf FOREIGN KEY (CF) REFERENCES project_salaried(CF)
    ON UPDATE CASCADE
    
    -- mi interessa salvare i contratti anche se 
    -- un impiegato a progetto viene eliminato
    ON DELETE SET NULL, 
    
    CONSTRAINT fk_cup FOREIGN KEY (CUP) REFERENCES project(CUP)
    ON UPDATE CASCADE
);
CREATE TABLE works_at(
    start_date  DATE    NOT NULL,
    end_date    DATE,

    -- foreign keys
    cf_base_emp cf_type,
    lab_code    SERIAL,

    CONSTRAINT date_integrity CHECK (end_date IS NULL OR end_date > start_date),

    CONSTRAINT cf_base_emp_fk FOREIGN KEY (cf_base_emp) REFERENCES base_emp(cf)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT lab_code_fk    FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE take_part(
    start_date  DATE    NOT NULL,
    end_date    DATE,

    -- foreign keys
    CUP         cup_type,
    lab_code    SERIAL,

    CONSTRAINT date_integrity CHECK (end_date IS NULL OR end_date > start_date),

    CONSTRAINT CUP_fk FOREIGN KEY (CUP) REFERENCES project(CUP)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT lab_code_fk FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);
CREATE TABLE equipment(
    code        uuid                DEFAULT uuid_generate_v4(),
    name        equipment_name_type NOT NULL,
    type        VARCHAR(30)         NOT NULL,
    tech_specs  VARCHAR(100),

    -- foreign keys
    lab_code    SERIAL,

    CONSTRAINT equipment_pk PRIMARY KEY (code),

    CONSTRAINT lab_code_fk FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);
CREATE TABLE equipment_request(
    code     uuid                    DEFAULT uuid_generate_v4(),

    name     equipment_name_type     NOT NULL,
    specs    VARCHAR(100)            NOT NULL,
    type     VARCHAR(30)             NOT NULL,

    quantity INTEGER                 NOT NULL   DEFAULT 1,

    -- foreign keys
    CUP      cup_type,
    lab_code SERIAL,

    CONSTRAINT equipment_request_pk PRIMARY KEY (code),

    CONSTRAINT CUP_fk       FOREIGN KEY (CUP)      REFERENCES project(CUP)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT lab_code_fk  FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT check_quantity CHECK ( quantity > 0 )
);
CREATE TABLE purchase(
    price           DECIMAL(10,2)   NOT NULL,
    purchase_date   DATE            NOT NULL,

    -- foreign keys
    CUP             cup_type,
    equipment_code  uuid            DEFAULT uuid_generate_v4(),

    CONSTRAINT cup_type_fk FOREIGN KEY (CUP) REFERENCES project(CUP)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT equipment_code_fk    FOREIGN KEY (equipment_code) 
                                    REFERENCES equipment(code)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT check_price CHECK ( price > 0)
);
/*
funds_to_hire: fondi rimanenti per l'assunzione di impiegati a progetto
funds_to_buy:  fondi rimanenti per l'acquisto di attrezzatura

La funzione COALESCE restituisce il valore del primo parametro se questo NON È NULL, altrimenti restituisce il secondo parametro.
	Le select come primo parametro, calcolano rispettivamente:
		la somma dei salari degli impiegati a progetto 
		la somma dei costi delle attrezzature
	È necessario l'utilizzo di questa funzione dato che se non ci sono impiegati a progetto (oppure acquisti), la select restituisce NULL e di conseguenza il progetto non viene inserito nella vista.
	Cosi facendo tutti i progetti, anche quelli che non hanno speso niente, vengono inseriti nella vista.
*/
CREATE OR REPLACE VIEW remaining_project_funds AS
SELECT 
	PR.CUP, 
	(PR.funds / 2) - COALESCE(	(SELECT SUM(pay)   FROM works_on WHERE cup = PR.cup),	0 ) AS funds_to_hire,
	(PR.funds / 2) - COALESCE(	(SELECT SUM(price) FROM purchase WHERE cup = PR.cup), 	0 ) AS funds_to_buy
FROM project AS PR;  

/*
Prende gli ultimi log in base alla data (new_role_date)
*/
CREATE VIEW last_log AS
SELECT *
FROM career_log AS C1
WHERE C1.new_role_date = (
	SELECT MAX(C2.new_role_date) 
	FROM career_log AS C2 
	WHERE C1.cf = C2.cf
);
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

-- restituisce vero se il login è avvenuto correttamente, falso altrimenti
CREATE OR REPLACE FUNCTION project_salaried_login(in_email IN VARCHAR(100), in_passw IN password_type)
RETURNS boolean
language 'plpgsql'

AS $$
BEGIN
RETURN EXISTS(
    SELECT * 
    FROM project_salaried
    WHERE email = in_email AND passw = crypt(in_passw, passw)
);
END;
$$;

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

CREATE OR REPLACE PROCEDURE hire_project_salaried(
    emp_cf      IN project_salaried.CF%TYPE,
    first_name  IN project_salaried.first_name%TYPE,
    last_name   IN project_salaried.last_name%TYPE,
    email       IN project_salaried.email%TYPE,
    passw       IN project_salaried.passw%TYPE,
    role        IN project_salaried.role%TYPE,
    birth_date  IN project_salaried.birth_date%TYPE,
    pay         IN works_on.pay%TYPE,
    hire_date   IN works_on.hire_date%TYPE,
    expiration  IN works_on.expiration%TYPE,
    CUP         IN works_on.CUP%TYPE
)
language 'plpgsql'
AS $$
BEGIN

    IF 
        NOT EXISTS(
            SELECT *
            FROM project_salaried
            WHERE cf = emp_cf
        )
    THEN
        INSERT INTO project_salaried (CF, first_name, last_name, email, passw, role, birth_date) VALUES
        (emp_cf, first_name, last_name, email, passw, role, birth_date);
    END IF;


    INSERT INTO works_on (pay, hire_date, expiration, CF, CUP) VALUES
    (pay, hire_date, expiration, emp_cf, CUP);

END;
$$;
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
 

-- riceve in ingresso un cf di impiegato e il lab_code del laboratorio dove spostarlo 
CREATE OR REPLACE PROCEDURE change_lab_afference (
    cf base_emp.cf%TYPE,
    v_lab_code laboratory.lab_code%TYPE 
) 
LANGUAGE 'plpgsql'

AS $$
    
BEGIN 
    
    -- verificare che l'impiegato stia lavorando in un laboratorio che non sia quello nuovo 
    IF EXISTS ( 
        SELECT * 
        FROM works_at
        WHERE lab_code <> v_lab_code AND cf_base_emp = cf AND end_date IS NULL
    ) THEN 
        raise notice 'entro1';
        -- "sganciare" l'impiegato dal laboratorio attuale 
        UPDATE works_at
        SET end_date = current_date 
        WHERE cf_base_emp = cf AND end_date IS NULL; 
    END IF;

    raise notice 'entro2';
    
    -- assegnare l'impiegato al nuovo laboratorio 
    INSERT INTO works_at VALUES (current_date, null, cf, v_lab_code); 
    RAISE NOTICE '% is now working into lab: %', cf, v_lab_code; 

END;
$$;
CREATE OR REPLACE PROCEDURE buy_equipment(
    request_code    IN equipment_request.code%TYPE,
    price           IN purchase.price%TYPE -- prezzo per la singola unità di prodotto
)
language 'plpgsql'
AS $$
DECLARE
    -- codice dell'attrezzatura
    equipment_code equipment.code%TYPE;
    
    request equipment_request%ROWTYPE;

    -- variabile contatore che scorre la quantità da acquistare 
    quantity equipment_request.quantity%TYPE; 
BEGIN

    -- prendo la richiesta
    SELECT * INTO request 
    FROM equipment_request
    WHERE code = request_code;

    -- se non esiste nessuna richiesta con quel codice, blocca l'acquisto
    IF NOT FOUND THEN
        RAISE EXCEPTION 'request % not found', request_code;
    END IF;

    quantity := request.quantity;

    WHILE quantity > 0 LOOP
        quantity := quantity - 1;

        equipment_code := uuid_generate_v4();

        INSERT INTO equipment (code, name, type, tech_specs, lab_code) VALUES
        (equipment_code, request.name, request.type, request.specs, request.lab_code);

        INSERT INTO purchase (price, purchase_date, CUP, equipment_code) VALUES
        (price, CURRENT_DATE, request.CUP, equipment_code);
    END LOOP;
    
    DELETE FROM equipment_request
    WHERE code = request_code; 

END;
$$;
-- restituisce emp_type (junior, middle, senior, manager) se il login è avvenuto correttamente
-- restituisce NULL altrimenti
CREATE OR REPLACE FUNCTION base_emp_login(in_email IN VARCHAR(100), in_passw IN password_type)
RETURNS VARCHAR
language 'plpgsql'

AS $$
DECLARE

emp_t VARCHAR;

BEGIN

SELECT type INTO emp_t 
FROM base_emp
WHERE email = in_email AND passw = crypt(in_passw, passw);

RETURN emp_t;

END;
$$;
-- Ad un laboratorio non può lavorare lo STESSO impiegato più di una volta nello stesso momemento 
CREATE OR REPLACE FUNCTION works_at_no_duplicates() 
RETURNS trigger

language 'plpgsql'

AS $$

DECLARE
emps_number INTEGER := 0;

BEGIN
    -- se end_date non è null, significa che non lavoro più in quel laboratorio
    -- quind posso inserire nella tabella
    IF NEW.end_date IS NOT NULL THEN
        RETURN NEW;
    END IF;

    -- conto quante volte l'impiegato che si vuole inserire
    -- lavora nello stesso laboratorio (end_date is null)
    SELECT COUNT(*) INTO emps_number
    FROM works_at
    WHERE NEW.cf_base_emp = cf_base_emp AND 
          NEW.lab_code = lab_code AND 
          end_date IS NULL;

    -- se l'impiegato lavora gia in quel laboratorio, impedisco l'inserimento
    IF emps_number > 0 THEN
        RAISE EXCEPTION 'L''impiegato % lavora già al laboratorio %', NEW.cf_base_emp, NEW.lab_code; 
    END IF;
    RETURN NEW;
END $$;

CREATE TRIGGER works_at_no_duplicates 
BEFORE INSERT ON works_at
FOR EACH ROW
EXECUTE FUNCTION works_at_no_duplicates();
/*
Aggiorna il campo type di base_emp all'ultimo new_role di career_log
*/

CREATE OR REPLACE FUNCTION update_emp_type()
RETURNS trigger
language 'plpgsql'

AS $$
BEGIN

    UPDATE base_emp
    SET "type" = NEW.new_role
    WHERE cf = NEW.cf;

    RETURN NEW;

END;
$$;


CREATE TRIGGER update_emp_type
AFTER INSERT ON career_log
FOR EACH ROW
EXECUTE FUNCTION update_emp_type();
-- Controlla nella richiesta se il laboratorio partecipa al progetto  
CREATE OR REPLACE FUNCTION validate_equipment_request() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$
BEGIN 

    IF 
        NOT EXISTS(
            SELECT *
            FROM take_part
            WHERE end_date IS NULL AND 
                  CUP = NEW.CUP AND lab_code = NEW.lab_code 
        )
    THEN
        RAISE EXCEPTION 'lab % non partecipa al progetto %', NEW.lab_code, NEW.CUP;
    END IF;
  

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER validate_equipment_request 
BEFORE INSERT ON equipment_request 
FOR EACH ROW
EXECUTE FUNCTION validate_equipment_request(); 


CREATE OR REPLACE FUNCTION end_project() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

BEGIN 
    UPDATE take_part
    SET end_date = NEW.end_date
    WHERE end_date IS NULL AND CUP = NEW.CUP;

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER end_project
AFTER UPDATE OF end_date ON project
FOR EACH ROW
EXECUTE FUNCTION end_project();


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
                AND end_date IS NULL
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

CREATE OR REPLACE FUNCTION emp_works_at_one_lab() 
RETURNS trigger
language 'plpgsql'

AS $emp_works_at_one_lab$

BEGIN 
    IF
        EXISTS(
            SELECT *
            FROM works_at 
            WHERE cf_base_emp = NEW.cf_base_emp AND end_date IS NULL
        )
    THEN
        RAISE EXCEPTION 'emp is already working into a lab'; 
    END IF; 

    RETURN NEW;

END;
$emp_works_at_one_lab$; 

CREATE OR REPLACE TRIGGER emp_works_at_one_lab 
BEFORE INSERT OR UPDATE ON works_at
FOR EACH ROW 
WHEN (NEW.end_date IS NULL)
EXECUTE FUNCTION emp_works_at_one_lab();
-- Quando un impiegato è assunto (ovvero quando viene inserito nella tabella base_emp)
-- viene inserito un log in career_log 
CREATE OR REPLACE FUNCTION employee_hiring()
RETURNS trigger
language 'plpgsql'
AS $$
BEGIN
    INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
    VALUES ('', 'junior', CURRENT_TIMESTAMP, NEW.cf);

    RETURN NEW;
END;
$$;


CREATE TRIGGER employee_hiring
AFTER INSERT ON base_emp
FOR EACH ROW
EXECUTE FUNCTION employee_hiring();

CREATE OR REPLACE FUNCTION crypt_password()
RETURNS trigger
language 'plpgsql'
AS $$ 
BEGIN
    NEW.passw := crypt(NEW.passw, gen_salt('md5'));

    RETURN NEW;
END;
$$;

CREATE TRIGGER crypt_password_project_salaried
BEFORE INSERT ON project_salaried
FOR EACH ROW
EXECUTE FUNCTION crypt_password();

CREATE TRIGGER crypt_password_base_emp
BEFORE INSERT ON  base_emp
FOR EACH ROW
EXECUTE FUNCTION crypt_password();
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
/*
Controlla che il referente scientifico sia senior e che il supervisore sia manager
*/
CREATE OR REPLACE FUNCTION check_project_assignment()
RETURNS trigger
language 'plpgsql'

AS $$

DECLARE

    referent_type base_emp.type%TYPE := ''; -- emp_type del referente di progetto
    manager_type base_emp.type%TYPE := ''; -- emp_type del supervisore del progetto

BEGIN

    SELECT "type" INTO referent_type
    FROM base_emp
    WHERE cf = NEW.cf_scientific_referent;

    SELECT "type" INTO manager_type
    FROM base_emp
    WHERE cf = NEW.cf_manager;

    
    IF referent_type <> 'senior' THEN
        RAISE EXCEPTION '% is not senior', NEW.cf_scientific_referent;
    END IF;

    IF manager_type <> 'manager' THEN
        RAISE EXCEPTION '% is not manager', NEW.cf_manager;
    END IF;


    RETURN NEW;

    EXCEPTION 
        -- Questo sqlstate si riferisce ad una violazione di un vincolo
        -- in questo caso se una delle due select non trova valori, assegna a referent_type (o manager_type) il valore NULL
        -- sono di tipo emp_type il quale ha un vincolo che gli impedisce di essere null
        -- viene dunque lanciata l'eccezione
        WHEN SQLSTATE '23514' THEN 
            RAISE EXCEPTION 'cf_scientific_referent or cf_manager does not exist';

END;
$$;


CREATE TRIGGER check_project_assignment
BEFORE INSERT OR UPDATE OF cf_scientific_referent, cf_manager ON project
FOR EACH ROW
EXECUTE FUNCTION check_project_assignment();
/*
Controlla che il manager scientifico del laboratorio sia senior
*/
CREATE OR REPLACE FUNCTION check_laboratory_assignment()
RETURNS trigger
language 'plpgsql'

AS $$

DECLARE

    scientific_manager_type base_emp.type%TYPE := ''; -- emp_type del manager di laboratorio

BEGIN

    SELECT "type" INTO scientific_manager_type
    FROM base_emp
    WHERE cf = NEW.cf_scientific_manager;
    
    IF scientific_manager_type <> 'senior' THEN
        RAISE EXCEPTION '% is not senior', NEW.cf_scientific_manager;
    END IF;

    RETURN NEW;

    EXCEPTION 
        -- Questo sqlstate si riferisce ad una violazione di un vincolo
        -- in questo caso se una delle due select non trova valori, assegna a scientific_manager_type il valore NULL
        -- sono di tipo emp_type il quale ha un vincolo che gli impedisce di essere NULL
        -- viene dunque lanciata l'eccezione
        WHEN SQLSTATE '23514' THEN 
            RAISE EXCEPTION 'cf_scientific_manager does not exist';

END;
$$;


CREATE TRIGGER check_laboratory_assignment
BEFORE INSERT OR UPDATE OF cf_scientific_manager ON laboratory
FOR EACH ROW
EXECUTE FUNCTION check_laboratory_assignment();
-- controlla che l'assunzione di un impiegato a progetto 
-- non faccia superare il limite imposto
CREATE OR REPLACE FUNCTION check_funds_to_hire() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

    DECLARE 

        remaining_funds remaining_project_funds.funds_to_hire%TYPE; 

    BEGIN 

        SELECT funds_to_hire INTO remaining_funds 
        FROM remaining_project_funds
        WHERE CUP = NEW.CUP; 

        IF remaining_funds - NEW.pay < 0 THEN 
            RAISE EXCEPTION 'not enough funds to hire'; 
        END IF; 

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER check_funds_to_hire 
BEFORE INSERT ON works_on 
FOR EACH ROW
EXECUTE FUNCTION check_funds_to_hire(); 
-- controlla che l'acquisto di attrezzatura
-- non faccia superare il limite imposto
CREATE OR REPLACE FUNCTION check_equipment_funds() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

    DECLARE 

        remaining_funds remaining_project_funds.funds_to_buy%TYPE; 

    BEGIN 

        SELECT funds_to_buy INTO remaining_funds 
        FROM remaining_project_funds
        WHERE CUP = NEW.CUP; 

        IF remaining_funds - NEW.price < 0 THEN 
            RAISE EXCEPTION 'not enough funds to buy equipment'; 
        END IF; 

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER check_equipment_funds 
BEFORE INSERT ON purchase 
FOR EACH ROW
EXECUTE FUNCTION check_equipment_funds(); 