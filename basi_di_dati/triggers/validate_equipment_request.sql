-- Controlla nella richiesta se il laboratorio partecipa al progetto  
CREATE OR REPLACE FUNCTION validate_equipment_request() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

BEGIN 
    IF 
        NOT EXISTS(
            SELECT *
            FROM take_part
            WHERE end_date IS NULL AND 
                  CUP = NEW.CUP AND lab_code = NEW.lab_code 
        )
    THEN
        RAISE EXCEPTION 'lab % non partecipa al progetto %', NEW.lab_code, NEW.CUP;
    END IF;

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER validate_equipment_request 
BEFORE INSERT ON equipment_request 
FOR EACH ROW
EXECUTE FUNCTION validate_equipment_request(); 