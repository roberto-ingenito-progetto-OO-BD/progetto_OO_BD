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