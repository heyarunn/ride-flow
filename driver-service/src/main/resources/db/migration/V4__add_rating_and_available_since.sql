--V3: Add rating and available_since columns

ALTER TABLE drivers ADD COLUMN rating DOUBLE PRECISION NOT NULL DEFAULT 4.5;
ALTER TABLE drivers ADD CONSTRAINT chk_drivers_rating CHECK ( rating BETWEEN 1.0 AND 5.0);

ALTER TABLE drivers ADD COLUMN available_since TIMESTAMP;
UPDATE drivers SET available_since=now() WHERE status='AVAILABLE';