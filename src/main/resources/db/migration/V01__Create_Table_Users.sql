CREATE TABLE IF NOT EXISTS users
(
    id                      SERIAL PRIMARY KEY,
    zip                     integer,
    city                    varchar(255),
    phone_number            varchar(255),
    phone_type              varchar(255),
    state                   varchar(255),
    street                  varchar(255),
    username                varchar(255),
    fullname                varchar(255),
    password                varchar(255),
    account_non_expired     BOOLEAN,
    account_non_locked      BOOLEAN,
    credentials_non_expired BOOLEAN,
    enabled                 BOOLEAN,
    CONSTRAINT uk_user_name UNIQUE (username)
);


CREATE TABLE IF NOT EXISTS user_addresses
(
    zip     integer,
    user_id bigint not null,
    city    varchar(255),
    state   varchar(255),
    street  varchar(255)
);

alter table if exists user_addresses
    add constraint pk_user_id
        foreign key (user_id) references users;