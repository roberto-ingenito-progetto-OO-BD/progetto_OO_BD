CREATE OR REPLACE FUNCTION end_project() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

BEGIN 
    UPDATE take_part
    SET end_date = NEW.end_date
    WHERE end_date IS NULL AND CUP = NEW.CUP;

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER end_project
AFTER UPDATE OF end_date ON project
FOR EACH ROW
EXECUTE FUNCTION end_project();