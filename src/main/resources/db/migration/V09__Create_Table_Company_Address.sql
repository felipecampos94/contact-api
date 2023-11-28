CREATE TABLE IF NOT EXISTS company_addresses
(
    zip        integer,
    company_id bigint not null,
    city       varchar(255),
    state      varchar(255),
    street     varchar(255)
);

alter table if exists company_addresses
    add constraint pk_company_id
        foreign key (company_id) references companies;