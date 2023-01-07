-- restituisce vero se il login è avvenuto correttamente, falso altrimenti
CREATE OR REPLACE FUNCTION project_salaried_login(in_email IN VARCHAR(100), in_passw IN password_type)
RETURNS boolean
language 'plpgsql'

AS $$
BEGIN
RETURN EXISTS(
    SELECT * 
    FROM project_salaried
    WHERE email = in_email AND passw = crypt(in_passw, passw)
);
END; $$;
