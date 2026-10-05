ALTER TABLE knowledge
ADD COLUMN next_review_at TIMESTAMP;

ALTER TABLE knowledge
ADD COLUMN review_interval_days INTEGER;

UPDATE knowledge
SET
    next_review_at = created_at + INTERVAL '1 day',
    review_interval_days = 1;

ALTER TABLE knowledge
ALTER COLUMN next_review_at SET NOT NULL;

ALTER TABLE knowledge
ALTER COLUMN review_interval_days SET NOT NULL;