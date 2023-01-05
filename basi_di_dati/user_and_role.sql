-- dev'essere eseguito da solo dato che non può essere eseguito in un transaction block
CREATE DATABASE company WITH OWNER postgres;

--------------- POSTGRES
-- mi connetto al database company CON POSTGRES

CREATE USER project_admin WITH 
    PASSWORD 'a'
    CREATEROLE
    CONNECTION LIMIT 1;

CREATE USER login_user WITH
	PASSWORD 'login';


CREATE SCHEMA projects_schema;


GRANT ALL PRIVILEGES ON SCHEMA projects_schema TO project_admin;
ALTER USER project_admin IN DATABASE company SET search_path TO projects_schema;


-- i privilegi di login_user sullo schema projects_schema, devono essere assegnati dal possessore dello schema
-- in questo caso postgres
GRANT USAGE ON SCHEMA projects_schema TO login_user;
ALTER USER login_user IN DATABASE company SET search_path TO projects_schema;

-- le estensioni possono essere attivate solo da un superuser (postgres in questo caso)
-- project_admin non può essere superuser altrimenti ha gli stessi privilegi di postgres
-- creo l'estensione sullo schema projects_schema cosi che l'user project_admin ne abbia l'accesso
CREATE EXTENSION IF NOT EXISTS "pgcrypto" SCHEMA projects_schema;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA projects_schema;


--------------- project_admin
-- mi connetto al database company con project_admin

-- eseguo il dump
-- eseguo il dump degli insert

GRANT SELECT ON base_emp, project_salaried TO login_user;


