CREATE TABLE IF NOT EXISTS companies
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    cnpj              VARCHAR(14) UNIQUE,
    street            VARCHAR(255),
    city              VARCHAR(255),
    state             VARCHAR(255),
    zip               integer
);

