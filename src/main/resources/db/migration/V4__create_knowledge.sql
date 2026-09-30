CREATE TABLE knowledge (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_knowledge_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);