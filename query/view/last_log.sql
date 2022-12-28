/*
Prende gli ultimi log in base alla data (new_role_date)
*/
CREATE VIEW last_log AS
SELECT *
FROM career_log AS C1
WHERE C1.new_role_date = (
	SELECT MAX(C2.new_role_date) 
	FROM career_log AS C2 
	WHERE C1.cf = C2.cf
);