-- V1: creating table for auth

CREATE TABLE credentials (
    id UUID PRIMARY KEY ,
    subject_id UUID NOT NULL UNIQUE ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    password_hash VARCHAR(100) NOT NULL ,
    role VARCHAR(32) NOT NULL ,
    created_at TIMESTAMP NOT NULL
);

-- Indexing
CREATE INDEX idx_auth_email ON credentials (email);