CREATE DOMAIN cf_type AS VARCHAR(16);
CONSTRAINT cf_type_length CHECK (length(cf_type) = 16);  
