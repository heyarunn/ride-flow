--V3: Add auth_subject_id column

ALTER TABLE drivers ADD COLUMN auth_subject_id UUID;

ALTER TABLE drivers ADD CONSTRAINT uq_drivers_auth_subject_id UNIQUE (auth_subject_id);

CREATE INDEX idx_drivers_auth_subject_id ON drivers (auth_subject_id);