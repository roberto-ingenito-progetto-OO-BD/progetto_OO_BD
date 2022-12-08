CREATE TABLE career_log(
    ex_role emp_type,
    new_role emp_type,
    new_role_date DATE NOT NULL,
    CF cf_type,

    CONSTRAINT emp_career_fk FOREIGN KEY (CF) REFERENCES base_emp(CF)
        ON DELETE CASCADE 
        ON UPDATE CASCADE,

    CONSTRAINT check_new_grade CHECK (
        -- assunto
        -- quando un impiegato viene assunto, viene assunto come junior
        ( ex_role IS NULL                              AND new_role =  'junior' ) OR  

        ( ex_role = 'junior'                           AND new_role =  'middle' ) OR 

        ( ex_role = 'middle'                           AND new_role =  'senior' ) OR 

        -- licenziato
        ( ex_role IS NOT NULL                          AND new_role IS NULL     ) OR 

        -- avanzamento a manager da qualsiasi ruolo che non sia manager (da manager a manager) 
        -- e che non sia null (non posso essere assunto come manager)
        ( ex_role IS NOT NULL AND ex_role <> 'manager' AND new_role =  'manager') OR 

        -- declassato da manager a qualsiasi altro ruolo che non non sia manager (da manager a manager) 
        -- e che non sia null (in questo caso entrerebbe nel caso del licenziamento)
        --
        -- un trigger verificherà che il new_role sia quello del log precedente
        -- se da middle è diventato manager e poi viene declassato, sarà declassato a middle 
        ( ex_role = 'manager' AND new_role IS NOT NULL AND new_role <> 'manager') 
    )
);

/* 
check_new_grade, tutte le possibili combinazioni

(null, null); -- no
(null, 'junior');
(null, 'middle'); -- no
(null, 'senior'); -- no
(null, 'manager'); -- no

('junior', null);
('junior', 'junior'); -- no
('junior', 'middle');
('junior', 'senior'); -- no
('junior', 'manager');

('middle', null);
('middle', 'junior'); -- no
('middle', 'middle'); -- no
('middle', 'senior');
('middle', 'manager');

('senior', null);
('senior', 'junior'); -- no
('senior', 'middle'); -- no
('senior', 'senior'); -- no
('senior', 'manager');

('manager', null);
('manager', 'junior');
('manager', 'middle');
('manager', 'senior'); 
('manager', 'manager'); -- no
*/