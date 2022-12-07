CREATE TABLE career_log(
    exRole emp_type,
    newRole emp_type,
    newRoleDate DATE NOT NULL,
    CF cf_type,

    CONSTRAINT emp_career_fk FOREIGN KEY (CF) REFERENCES base_emp(CF)
        ON DELETE CASCADE 
        ON UPDATE CASCADE  
);

-- INSERT INTO career_log VALUES 
-- ('senior', 'manager', '7/12/2020', 'aasdasd');