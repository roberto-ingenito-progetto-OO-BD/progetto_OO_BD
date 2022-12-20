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