-- V1: creating table templete for rides

CREATE TABLE rides (
    id UUID PRIMARY KEY ,
    rider_id UUID NOT NULL ,
    driver_id UUID NOT NULL ,
    status VARCHAR(32) NOT NULL ,
    pickup_latitude DOUBLE PRECISION NOT NULL ,
    pickup_longitude DOUBLE PRECISION NOT NULL ,
    dropoff_latitude DOUBLE PRECISION NOT NULL ,
    dropoff_longitude DOUBLE PRECISION NOT NULL ,
    requested_at TIMESTAMP,
    matched_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL 
);

-- Indexing
CREATE INDEX idx_rides_rider_id ON rides (rider_id);
CREATE INDEX idx_rides_driver_id ON rides (driver_id);
CREATE INDEX idx_rides_status ON rides (status);