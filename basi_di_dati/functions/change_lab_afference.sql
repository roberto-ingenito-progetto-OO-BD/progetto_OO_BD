-- riceve in ingresso un cf di impiegato e il lab_code del laboratorio dove spostarlo 
CREATE OR REPLACE PROCEDURE change_lab_afference(
    cf base_emp.cf%TYPE,
    v_lab_code laboratory.lab_code%TYPE 
) 
LANGUAGE 'plpgsql'
AS $$
    
BEGIN 
    -- verificare che l'impiegato stia lavorando in un laboratorio che non sia quello nuovo 
    IF EXISTS ( 
        SELECT * 
        FROM works_at
        WHERE lab_code <> v_lab_code AND cf_base_emp = cf AND end_date IS NULL
    ) THEN 
        -- "sganciare" l'impiegato dal laboratorio attuale 
        UPDATE works_at
        SET end_date = current_date 
        WHERE cf_base_emp = cf AND end_date IS NULL; 
    END IF;
    
    -- assegnare l'impiegato al nuovo laboratorio 
    INSERT INTO works_at VALUES (current_date, null, cf, v_lab_code); 
    RAISE NOTICE '% is now working into lab: %', cf, v_lab_code; 
END; $$;