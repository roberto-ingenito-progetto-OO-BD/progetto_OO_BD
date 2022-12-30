/*
funds_to_hire: fondi rimanenti per l'assunzione di impiegati a progetto
funds_to_buy:  fondi rimanenti per l'acquisto di attrezzatura

La funzione COALESCE restituisce il valore del primo parametro se questo NON È NULL, altrimenti restituisce il secondo parametro.
	Le select come primo parametro, calcolano rispettivamente:
		la somma dei salari degli impiegati a progetto 
		la somma dei costi delle attrezzature
	È necessario l'utilizzo di questa funzione dato che se non ci sono impiegati a progetto (oppure acquisti), la select restituisce NULL e di conseguenza il progetto non viene inserito nella vista.
	Cosi facendo tutti i progetti, anche quelli che non hanno speso niente, vengono inseriti nella vista.
*/
CREATE OR REPLACE VIEW remaining_project_funds AS
SELECT 
	PR.CUP, 
	(PR.funds / 2) - COALESCE(	(SELECT SUM(pay)   FROM works_on WHERE cup = PR.cup),	0 ) AS funds_to_hire,
	(PR.funds / 2) - COALESCE(	(SELECT SUM(price) FROM purchase WHERE cup = PR.cup), 	0 ) AS funds_to_buy
FROM project AS PR;  
