CREATE TABLE IF NOT EXISTS company_branches
(
    company_id       bigint NOT NULL,
    company_branch_id bigint NOT NULL
);

ALTER TABLE IF EXISTS company_branches
    ADD CONSTRAINT uc_company_branches UNIQUE (company_id, company_branch_id);
