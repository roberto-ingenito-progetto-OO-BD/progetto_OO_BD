CREATE OR REPLACE FUNCTION crypt_password()
RETURNS trigger
language 'plpgsql'
AS $$ 
BEGIN
    NEW.passw := crypt(NEW.passw, gen_salt('md5'));

    RETURN NEW;
END; $$;

CREATE TRIGGER crypt_password_project_salaried
BEFORE INSERT ON project_salaried
FOR EACH ROW
EXECUTE FUNCTION crypt_password();

CREATE TRIGGER crypt_password_base_emp
BEFORE INSERT ON  base_emp
FOR EACH ROW
EXECUTE FUNCTION crypt_password();