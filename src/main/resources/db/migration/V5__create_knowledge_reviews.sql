CREATE TABLE knowledge_reviews (
    id UUID PRIMARY KEY,
    knowledge_id UUID NOT NULL,
    reviewed_at TIMESTAMP NOT NULL,
    performance VARCHAR(255) NOT NULL,

    CONSTRAINT fk_knowledge_reviews_knowledge
        FOREIGN KEY (knowledge_id)
        REFERENCES knowledge(id)
);