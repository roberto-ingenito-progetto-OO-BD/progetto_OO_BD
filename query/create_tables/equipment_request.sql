CREATE TABLE equipment_request(
    name equipment_name_type NOT NULL,
    specs VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,

    -- foreign keys
    CUP         cup_type,
    lab_code    SERIAL,

    CONSTRAINT CUP_fk FOREIGN KEY CUP REFERENCES project(CUP)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT lab_code_fk FOREIGN KEY lab_code REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);