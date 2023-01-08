-- dev'essere eseguito da solo dato che non può essere eseguito in un transaction block
CREATE DATABASE company WITH OWNER postgres;

--------------- POSTGRES
-- mi connetto al database company CON POSTGRES

CREATE USER project_admin WITH 
    PASSWORD 'proj_admin'
    CREATEROLE
    CONNECTION LIMIT 1;

CREATE USER login_user WITH
	PASSWORD 'login';

CREATE SCHEMA projects_schema;

GRANT ALL PRIVILEGES ON SCHEMA projects_schema TO project_admin;
ALTER USER project_admin IN DATABASE company SET search_path TO projects_schema;


-- i privilegi di login_user sullo schema projects_schema, devono essere assegnati dal possessore dello schema
-- in questo caso postgres
-- GRANT USAGE ON SCHEMA projects_schema TO login_user;
ALTER USER login_user IN DATABASE company SET search_path TO projects_schema;

-- le estensioni possono essere attivate solo da un superuser (postgres in questo caso)
-- project_admin non può essere superuser altrimenti ha gli stessi privilegi di postgres
-- creo l'estensione sullo schema projects_schema cosi che l'user project_admin ne abbia l'accesso
CREATE EXTENSION IF NOT EXISTS "pgcrypto" SCHEMA projects_schema;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA projects_schema;

CREATE ROLE base_emp_role;

GRANT USAGE ON SCHEMA projects_schema TO base_emp_role;

CREATE USER junior_user WITH 
	PASSWORD 'junior'
	IN ROLE base_emp_role;

CREATE USER middle_user WITH
	PASSWORD 'middle'
	IN ROLE base_emp_role;

CREATE USER senior_user WITH
	PASSWORD 'senior'
	IN ROLE base_emp_role;

CREATE USER manager_user WITH
	PASSWORD 'manager'
	IN ROLE base_emp_role;

CREATE USER project_salaried_user WITH
	PASSWORD 'proj_sal';
	
GRANT USAGE ON SCHEMA projects_schema TO project_salaried_user;

ALTER USER junior_user  		 IN DATABASE company SET search_path TO projects_schema;
ALTER USER middle_user  		 IN DATABASE company SET search_path TO projects_schema;
ALTER USER senior_user  		 IN DATABASE company SET search_path TO projects_schema;
ALTER USER manager_user 		 IN DATABASE company SET search_path TO projects_schema;
ALTER USER project_salaried_user IN DATABASE company SET search_path TO projects_schema;

--------------- project_admin
-- mi connetto al database company con project_admin

-- eseguo il dump
-- eseguo il dump degli insert

GRANT SELECT ON base_emp, project_salaried TO login_user;

GRANT SELECT ON base_emp, 
				career_log, 
				works_at, 
				laboratory, 
				take_part, 
				equipment, 
				site 
			 TO base_emp_role;

GRANT SELECT(CUP, name, description, start_date, end_date, deadline) ON project TO base_emp_role;

GRANT SELECT ON project, 
				purchase, 
				equipment_request, 
				project_salaried, 
				works_on,
				remaining_project_funds
			 TO manager_user, senior_user;

GRANT SELECT ON project_salaried,
				works_on,
				project
			 TO project_salaried_user;

-- insert
GRANT INSERT ON take_part, -- scientific manager che partecipa ad un progetto con quel lab
				equipment_request
			 TO senior_user; 

GRANT INSERT ON equipment, 
				purchase, 
				project_salaried, 
				works_on 
			 TO manager_user, senior_user;




-- delete
GRANT DELETE ON equipment_request TO manager_user, senior_user;

-- update
GRANT UPDATE(end_date) ON take_part TO senior_user;  -- scientific manager che termina di partecipare ad un progetto con quel lab
GRANT UPDATE(specs, quantity) ON equipment_request TO senior_user; -- scientific manager