CREATE TABLE IF NOT EXISTS user_permissions
(
    user_id       bigint NOT NULL,
    permission_id bigint NOT NULL
);

ALTER TABLE IF EXISTS user_permissions
    ADD CONSTRAINT uc_user_permissions UNIQUE (user_id, permission_id);
