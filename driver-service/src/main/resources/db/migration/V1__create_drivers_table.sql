-- V1: Inital drivers table

CREATE TABLE drivers (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL ,
    phone_number VARCHAR(32) NOT NULL,
    vehicle_number VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    current_latitude DOUBLE PRECISION,
    current_longitude DOUBLE PRECISION,
    location_updated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Indexing
CREATE INDEX idx_drivers_email ON drivers (email);
CREATE INDEX idx_drivers_phone ON drivers (phone_number);
-- The matching engine will query "available drivers", so idx status
CREATE INDEX idx_drivers_status ON drivers (status);