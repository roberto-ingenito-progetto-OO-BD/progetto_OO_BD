CREATE TABLE equipment(
    code uuid DEFAULT uuid_generate_v4(),
    name equipment_name_type NOT NULL,
    type VARCHAR(30) NOT NULL,
    techspecs VARCHAR(100),

    -- foreign keys
    lab_code    SERIAL,

    -- primary key
    CONSTRAINT equipment_pk PRIMARY KEY code,

    -- foreign key constraint
    CONSTRAINT lab_code_fk FOREIGN KEY lab_code REFERENCES laboratory(lab_code)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

    -- check
    CONSTRAINT check_price CHECK ( price > 0)
);