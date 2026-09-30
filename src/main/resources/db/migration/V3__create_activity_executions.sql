CREATE TABLE activity_executions (
    id UUID PRIMARY KEY,
    activity_id UUID NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    actual_duration INTEGER,

    CONSTRAINT fk_activity_executions_activity
        FOREIGN KEY (activity_id)
        REFERENCES activities(id)
);