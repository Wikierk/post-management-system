ALTER TABLE branches ADD COLUMN is_active BOOLEAN;

ALTER TABLE branches ADD COLUMN version BIGINT;

UPDATE branches SET is_active = true WHERE is_active IS NULL;

UPDATE branches SET version = 0 WHERE version IS NULL;

ALTER TABLE branches ALTER COLUMN is_active SET NOT NULL;

ALTER TABLE branches ALTER COLUMN version SET NOT NULL;