ALTER TABLE activities
ADD COLUMN status VARCHAR(255);

UPDATE activities
SET status = CASE
    WHEN active = true THEN 'PENDING'
    ELSE 'CANCELLED'
END;

ALTER TABLE activities
ALTER COLUMN status SET NOT NULL;

ALTER TABLE activities
DROP COLUMN active;