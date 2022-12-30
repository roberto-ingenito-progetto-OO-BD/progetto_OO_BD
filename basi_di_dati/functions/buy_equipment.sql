CREATE OR REPLACE PROCEDURE buy_equipment(
    request_code    IN equipment_request.code%TYPE,
    price           IN purchase.price%TYPE -- prezzo per la singola unità di prodotto
)
language 'plpgsql'
AS $$
DECLARE
    -- codice dell'attrezzatura
    equipment_code equipment.code%TYPE;
    
    request equipment_request%ROWTYPE;

    -- variabile contatore che scorre la quantità da acquistare 
    quantity equipment_request.quantity%TYPE; 
BEGIN

    -- prendo la richiesta
    SELECT * INTO request 
    FROM equipment_request
    WHERE code = request_code;

    -- se non esiste nessuna richiesta con quel codice, blocca l'acquisto
    IF NOT FOUND THEN
        RAISE EXCEPTION 'request % not found', request_code;
    END IF;

    quantity := request.quantity;

    WHILE quantity > 0 LOOP
        quantity := quantity - 1;

        equipment_code := uuid_generate_v4();

        INSERT INTO equipment (code, name, type, tech_specs, lab_code) VALUES
        (equipment_code, request.name, request.type, request.specs, request.lab_code);

        INSERT INTO purchase (price, purchase_date, CUP, equipment_code) VALUES
        (price, CURRENT_DATE, request.CUP, equipment_code);
    END LOOP;
    
    DELETE FROM equipment_request
    WHERE code = request_code; 

END;
$$;