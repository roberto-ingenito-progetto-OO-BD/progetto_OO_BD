CREATE TABLE purchase(
    price DECIMAL(10,2) NOT NULL,
    purchase_date DATE NOT NULL,

    -- foreign keys
    CUP cup_type,
    equipment_code uuid DEFAULT uuid_generate_v4(),

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