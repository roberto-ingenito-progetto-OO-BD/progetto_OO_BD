CREATE DOMAIN cup_type AS VARCHAR(15)
CONSTRAINT check_cup_type CHECK ( length(VALUE) = 15);