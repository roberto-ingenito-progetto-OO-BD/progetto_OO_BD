-- EXTENSION
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- DOMAINS
CREATE DOMAIN cf_type AS VARCHAR(16)
CONSTRAINT cf_type_length CHECK (length(VALUE) = 16);

CREATE DOMAIN cup_type AS VARCHAR(15)
CONSTRAINT check_cup_type CHECK ( length(VALUE) = 15);

CREATE DOMAIN emp_type AS VARCHAR(10) 
CONSTRAINT emp_type_check CHECK (VALUE IN ('', 'junior', 'middle', 'senior', 'manager') AND VALUE IS NOT NULL);

CREATE DOMAIN equipment_name_type 
AS VARCHAR(30);

CREATE DOMAIN password_type AS VARCHAR(100);

CREATE DOMAIN name_type AS VARCHAR(30);

CREATE DOMAIN salary_type AS DECIMAL(8,2);

-- CREATE TABLE
CREATE TABLE base_emp(
    CF cf_type,
    first_name name_type NOT NULL,
    last_name name_type NOT NULL,
    email VARCHAR(100) NOT NULL,
    passw password_type NOT NULL,
    birth_date DATE NOT NULL,
    "type" emp_type NOT NULL, 
    "role" VARCHAR(30) NOT NULL,
    salary salary_type NOT NULL,

    CONSTRAINT emp_email_unique UNIQUE (email),
    CONSTRAINT base_emp_pk PRIMARY KEY (CF)
);

CREATE TABLE career_log(
    ex_role emp_type,
    new_role emp_type,
    new_role_date DATE NOT NULL,
    CF cf_type,
    
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
        -- se da middle è diventato manager e poi viene declassato, sarà declassato a middle 
        ex_role = 'manager' AND new_role <> 'manager'
    )
);

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
    CUP cup_type,
    funds DECIMAL(10,2) NOT NULL,
    "name" VARCHAR(50),
    description VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL DEFAULT CURRENT_DATE,
    end_date DATE,
    deadline DATE,

    -- foreign key
    cf_manager cf_type NOT NULL,
    cf_scientific_referent cf_type NOT NULL,

    CONSTRAINT project_pk PRIMARY KEY (CUP),    
    CONSTRAINT check_deadline CHECK ( deadline IS NULL OR deadline > start_date ),

    CONSTRAINT fk_cf_manager FOREIGN KEY (CF_manager) REFERENCES base_emp(CF)
    ON UPDATE CASCADE,
    
    CONSTRAINT fk_cf_scientific_referent FOREIGN KEY (CF_scientific_referent) REFERENCES base_emp(CF)
    ON UPDATE CASCADE
);

CREATE TABLE laboratory(
    lab_code    SERIAL,
    lab_name    VARCHAR(30) NOT NULL,
    topic       VARCHAR(30) NOT NULL,

    -- foreign keys
    site_number             SERIAL  NOT NULL,
    cf_scientific_manager   cf_type NOT NULL,

    CONSTRAINT lab_pk                   PRIMARY KEY (lab_code),
    CONSTRAINT site_number_fk           FOREIGN KEY (site_number)             REFERENCES site(site_number),
    CONSTRAINT cf_scientific_manager_fk FOREIGN KEY (cf_scientific_manager)   REFERENCES base_emp(cf)
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
    
    -- mi interessa salvare i contratti anche se un impiegato a progetto viene eliminato
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

    CONSTRAINT lab_code_fk FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
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
    code uuid DEFAULT uuid_generate_v4(),
    name equipment_name_type NOT NULL,
    type VARCHAR(30) NOT NULL,
    tech_specs VARCHAR(100),

    -- foreign keys
    lab_code    SERIAL,

    -- primary key
    CONSTRAINT equipment_pk PRIMARY KEY (code),

    -- foreign key constraint
    CONSTRAINT lab_code_fk FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE equipment_request(
    name equipment_name_type NOT NULL,
    specs VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,

    -- foreign keys
    CUP         cup_type,
    lab_code    SERIAL,

    CONSTRAINT CUP_fk FOREIGN KEY (CUP) REFERENCES project(CUP)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT lab_code_fk FOREIGN KEY (lab_code) REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE purchase(
    price DECIMAL(10,2) NOT NULL,
    purchase_date DATE NOT NULL,

    -- foreign keys
    CUP cup_type,
    equipment_code uuid,

    -- foreign key constraint
    CONSTRAINT cup_type_fk FOREIGN KEY (CUP) REFERENCES project(CUP)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

    CONSTRAINT equipment_code_fk FOREIGN KEY (equipment_code) REFERENCES equipment(code)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    -- check
    CONSTRAINT check_price CHECK ( price > 0)
);

-- VIEW
CREATE VIEW last_log AS
SELECT *
FROM career_log AS C1
WHERE C1.new_role_date = (
	SELECT MAX(C2.new_role_date) 
	FROM career_log AS C2 
	WHERE C1.cf = C2.cf
);

-- TRIGGER
/*
Controlla che il referente scientifico sia senior e che il supervisore sia manager
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


-- Quando un impiegato è assunto (ovvero quando viene inserito nella tabella base_emp)
-- viene inserito un log in career_log 
CREATE OR REPLACE FUNCTION employee_hiring()
RETURNS trigger
language 'plpgsql'
AS $$
BEGIN
    INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
    VALUES ('', 'junior', CURRENT_DATE, NEW.cf);

    RETURN NEW;
END;
$$;


CREATE TRIGGER employee_hiring
AFTER INSERT ON base_emp
FOR EACH ROW
EXECUTE FUNCTION employee_hiring();



CREATE OR REPLACE FUNCTION fire_emp()
RETURNS trigger
language 'plpgsql'

AS $$
DECLARE

BEGIN
    -- se l'impiegato da licenziare è un referente scientifico oppure manager di laboratorio
    -- OPPURE
    -- è manager scientifico di un laboratorio
    -- non posso licenziarlo, ma bisognerà prima cambiare
    IF 
    EXISTS(
        SELECT *
        FROM project
        WHERE cf_manager = NEW.cf OR cf_scientific_referent = NEW.cf
    ) OR
    EXISTS(
        SELECT *
        FROM laboratory
        WHERE cf_scientific_manager = NEW.cf
    )    
    THEN
        RAISE EXCEPTION '% lavora come manager o referente in un progetto o laboratorio', NEW.cf;
    END IF;

    -- aggiorna l'end date dei progetti in cui lavora, alla data del licenziamento
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



-- FUNCTIONS

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
        difference_years := ((CURRENT_DATE - item.new_role_date) / 365);

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
            VALUES ('middle', 'senior', CURRENT_DATE, item.cf);

        -- lavora da 3 anni (o più) ma meno di 7 e NON è già middle
        -- avanza a middle
        ELSIF difference_years >= 3 AND difference_years < 7 AND emp_current_role <> 'middle' THEN
            INSERT INTO career_log (ex_role, new_role, new_role_date, CF) 
            VALUES ('junior', 'middle', CURRENT_DATE, item.cf);
        END IF;

    END LOOP;
END;
$$;



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




CREATE OR REPLACE PROCEDURE revoke_manager( cf_manager IN base_emp.cf%TYPE ) 
language 'plpgsql'

AS $$

    DECLARE

        emp_log last_log%ROWTYPE; 
        v_ex_role career_log.ex_role%TYPE := '';
        hire_date career_log.new_role_date%TYPE; 
        difference_years INTEGER; 

    BEGIN

        -- recuperare l'utlimo log dell'impiegato in ingresso e verificare che sia manager 
        SELECT * INTO emp_log 
        FROM last_log 
        WHERE cf = cf_manager AND new_role = 'manager'; 

        IF NOT FOUND THEN 
            RAISE EXCEPTION '% is not manager', cf_manager;
        ELSE 
            -- l'impiegato è un manager e deve essere declassato al ruolo precedente 
            -- selezionare la data di assunzione
            SELECT new_role_date INTO hire_date 
            FROM career_log
            WHERE (ex_role, new_role) = ('', 'junior') AND cf = cf_manager;

            -- calcolare gli anni di lavoro 
            difference_years := ((CURRENT_DATE - hire_date) / 365); 

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
            VALUES ('manager', v_ex_role, current_date, cf_manager);

            RAISE NOTICE '% declassed to %', cf_manager, v_ex_role; 

        END IF; 

    END;
$$;
