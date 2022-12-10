CREATE TABLE laboratory(
    lab_code    SERIAL,
    lab_name    VARCHAR(30) NOT NULL,
    topic       VARCHAR(30) NOT NULL,

    -- foreign keys
    site_number             SERIAL  NOT NULL,
    cf_scientific_manager   cf_type NOT NULL,

    CONSTRAINT lab_pk                   PRIMARY KEY lab_code,
    CONSTRAINT site_number_fk           FOREIGN KEY site_number             REFERENCES site(site_number),
    CONSTRAINT cf_scientific_manager_fk FOREIGN KEY cf_scientific_manager   REFERENCES base_emp(cf)
);