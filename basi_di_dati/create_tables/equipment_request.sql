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