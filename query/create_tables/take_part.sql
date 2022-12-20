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