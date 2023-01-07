-- restituisce emp_type (junior, middle, senior, manager) se il login è avvenuto correttamente
-- restituisce NULL altrimenti
CREATE OR REPLACE FUNCTION base_emp_login(in_email IN VARCHAR(100), in_passw IN password_type)
RETURNS VARCHAR
language 'plpgsql'
AS $$

DECLARE
    emp_t VARCHAR;

BEGIN
    SELECT type INTO emp_t 
    FROM base_emp
    WHERE email = in_email AND passw = crypt(in_passw, passw);

    RETURN emp_t;
END; $$;