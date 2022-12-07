CREATE TABLE base_emp(
    CF cf_type,
    first_name name_type NOT NULL,
    last_name name_type NOT NULL,
    email VARCHAR(100) NOT NULL,
    passw password_type NOT NULL,
    birth_date DATE NOT NULL,
    "type" emp_type NOT NULL, 
    "role" VARCHAR(30) NOT NULL,
    salary salary_type NOT NULL,

    CONSTRAINT check_type CHECK ( "type" IN ('junior', 'middle', 'senior', 'manager') ),
    CONSTRAINT emp_email_unique UNIQUE (email),
    CONSTRAINT base_emp_pk PRIMARY KEY (CF)
);

-- INSERT INTO base_emp VALUES 
-- ('NGNRRT02M29H931J', 'Roberto', 'Ingenito', 'ingenitoroby@gmail.com', 'password', '29/08/2002', 'manager', 'developer', 10000);
