CREATE TABLE project_salaried(
    CF          cf_type,
    first_name  name_type       NOT NULL,
    last_name   name_type       NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    passw       password_type   NOT NULL,
    "role"      VARCHAR(30)     NOT NULL,
    birth_date  DATE            NOT NULL,

    CONSTRAINT emp_email_unique UNIQUE (email),
    CONSTRAINT project_salaried_pk PRIMARY KEY (CF)
);