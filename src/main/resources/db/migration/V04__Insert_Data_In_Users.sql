-- Inserir dados na tabela 'users' com base na classe fornecida
INSERT INTO users (username, fullname, password, account_non_expired, account_non_locked, credentials_non_expired,
                   enabled)
VALUES ('usuario1', 'Nome Completo 1',
        '$2a$12$4XMaepoylstENFe1q5.oJuL9.co3osoGHYX2/GEFA/IoLpt5kVenW', true, true, true, true),
       ('usuario2', 'Nome Completo 2',
        '$2a$12$4XMaepoylstENFe1q5.oJuL9.co3osoGHYX2/GEFA/IoLpt5kVenW', true, true, true, true);

