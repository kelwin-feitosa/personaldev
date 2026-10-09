ALTER TABLE goals
    ADD COLUMN deadline_type VARCHAR(20) NOT NULL DEFAULT 'FLEXIBLE';

ALTER TABLE goals
    ADD COLUMN parent_goal_id UUID;

ALTER TABLE goals
    ADD CONSTRAINT fk_goals_parent_goal
        FOREIGN KEY (parent_goal_id)
        REFERENCES goals(id);

ALTER TABLE goals
    ADD CONSTRAINT chk_goals_deadline_type
        CHECK (deadline_type IN ('FIXED', 'FLEXIBLE'));

ALTER TABLE goals
    ADD CONSTRAINT chk_goals_not_own_parent
        CHECK (parent_goal_id IS NULL OR parent_goal_id <> id);