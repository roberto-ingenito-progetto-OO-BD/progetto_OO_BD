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
    CONSTRAINT check_end_date CHECK ( end_date IS NULL OR end_date > start_date ),

    CONSTRAINT fk_cf_manager FOREIGN KEY (CF_manager) REFERENCES base_emp(CF)
    ON UPDATE CASCADE,
    
    CONSTRAINT fk_cf_scientific_referent FOREIGN KEY (CF_scientific_referent) REFERENCES base_emp(CF)
    ON UPDATE CASCADE
);