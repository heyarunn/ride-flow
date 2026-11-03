-- v3: Link each rider profile to its auth identity

ALTER TABLE users ADD COLUMN auth_subject_id UUID;

ALTER TABLE users ADD CONSTRAINT uq_users_auth_subject_id UNIQUE (auth_subject_id);

CREATE INDEX idx_users_auth_subject_id ON users (auth_subject_id);