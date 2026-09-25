CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE goals (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(255) NOT NULL,
    priority INTEGER NOT NULL,
    deadline DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_goals_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE activities (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    goal_id UUID,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    estimated_duration INTEGER,
    difficulty INTEGER,
    priority INTEGER,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_activities_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_activities_goal
        FOREIGN KEY (goal_id)
        REFERENCES goals(id)
);