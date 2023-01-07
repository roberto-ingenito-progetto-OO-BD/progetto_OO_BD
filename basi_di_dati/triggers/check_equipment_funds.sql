-- controlla che l'acquisto di attrezzatura
-- non faccia superare il limite imposto
CREATE OR REPLACE FUNCTION check_equipment_funds() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

DECLARE 
    remaining_funds remaining_project_funds.funds_to_buy%TYPE; 

BEGIN 
    SELECT funds_to_buy INTO remaining_funds 
    FROM remaining_project_funds
    WHERE CUP = NEW.CUP; 

    IF remaining_funds - NEW.price < 0 THEN 
        RAISE EXCEPTION 'not enough funds to buy equipment'; 
    END IF; 

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER check_equipment_funds 
BEFORE INSERT ON purchase 
FOR EACH ROW
EXECUTE FUNCTION check_equipment_funds(); 