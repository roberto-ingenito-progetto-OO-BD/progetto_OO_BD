-- controlla che l'assunzione di un impiegato a progetto 
-- non faccia superare il limite imposto
CREATE OR REPLACE FUNCTION check_funds_to_hire() 
RETURNS TRIGGER 
language 'plpgsql'
AS $$

    DECLARE 

        remaining_funds remaining_project_funds.funds_to_hire%TYPE; 

    BEGIN 

        SELECT funds_to_hire INTO remaining_funds 
        FROM remaining_project_funds
        WHERE CUP = NEW.CUP; 

        IF remaining_funds - NEW.pay < 0 THEN 
            RAISE EXCEPTION 'not enough funds to hire'; 
        END IF; 

    RETURN NEW; 
END $$; 

CREATE OR REPLACE TRIGGER check_funds_to_hire 
BEFORE INSERT ON works_on 
FOR EACH ROW
EXECUTE FUNCTION check_funds_to_hire(); 