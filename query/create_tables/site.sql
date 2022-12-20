CREATE TABLE site(
    site_number     SERIAL,
    name            VARCHAR(40) NOT NULL,
    street          VARCHAR(30) NOT NULL
    street_number   VARCHAR(10) NOT NULL,
    postal_code     VARCHAR(10) NOT NULL,
    city            VARCHAR(20) NOT NULL,

    CONSTRAINT site_pk PRIMARY KEY (site_number)
);