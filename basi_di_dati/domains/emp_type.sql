CREATE DOMAIN emp_type AS VARCHAR(10) 
CONSTRAINT emp_type_check CHECK 
    (VALUE IN ('', 'junior', 'middle', 'senior', 'manager') 
        AND VALUE IS NOT NULL);