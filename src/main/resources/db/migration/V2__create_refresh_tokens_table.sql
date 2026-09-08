CREATE TABLE refresh_tokens (
                                id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                token VARCHAR(255) UNIQUE NOT NULL,
                                user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                expires_at TIMESTAMP NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);