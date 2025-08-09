CREATE TABLE Users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    link VARCHAR(255) DEFAULT 't.me/' NOT NULL UNIQUE
);

CREATE TABLE Tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority INTEGER DEFAULT 1,
    created_at DATE DEFAULT CURRENT_DATE,
    is_completed BOOLEAN DEFAULT false,
    user_id BIGINT NOT NULL,
    CONTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);