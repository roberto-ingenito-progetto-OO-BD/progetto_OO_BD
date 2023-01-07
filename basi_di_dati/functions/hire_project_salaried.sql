CREATE OR REPLACE PROCEDURE hire_project_salaried(
    emp_cf      IN project_salaried.CF%TYPE,
    first_name  IN project_salaried.first_name%TYPE,
    last_name   IN project_salaried.last_name%TYPE,
    email       IN project_salaried.email%TYPE,
    passw       IN project_salaried.passw%TYPE,
    role        IN project_salaried.role%TYPE,
    birth_date  IN project_salaried.birth_date%TYPE,
    pay         IN works_on.pay%TYPE,
    hire_date   IN works_on.hire_date%TYPE,
    expiration  IN works_on.expiration%TYPE,
    CUP         IN works_on.CUP%TYPE
)
language 'plpgsql'
AS $$
BEGIN
    IF 
        NOT EXISTS(
            SELECT *
            FROM project_salaried
            WHERE cf = emp_cf
        )
    THEN
        INSERT INTO project_salaried (CF, first_name, last_name, email, passw, role, birth_date) VALUES
        (emp_cf, first_name, last_name, email, passw, role, birth_date);
    END IF;

    INSERT INTO works_on (pay, hire_date, expiration, CF, CUP) VALUES
    (pay, hire_date, expiration, emp_cf, CUP);
END; $$;