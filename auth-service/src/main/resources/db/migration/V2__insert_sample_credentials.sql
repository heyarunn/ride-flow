-- V2: seed 5 dev credentials (local development only)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO credentials (id, subject_id, email, password_hash, role, created_at) VALUES
     (gen_random_uuid(), '00000000-0000-0000-0000-000000000001', 'admin@rideflow.com',   crypt('Admin@12345',  gen_salt('bf', 10)), 'ADMIN',  now()),
     (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'rider1@rideflow.com',  crypt('Rider@12345',  gen_salt('bf', 10)), 'RIDER',  now()),
     (gen_random_uuid(), '00000000-0000-0000-0000-000000000003', 'rider2@rideflow.com',  crypt('Rider@12345',  gen_salt('bf', 10)), 'RIDER',  now()),
     (gen_random_uuid(), '00000000-0000-0000-0000-000000000004', 'driver1@rideflow.com', crypt('Driver@12345', gen_salt('bf', 10)), 'DRIVER', now()),
     (gen_random_uuid(), '00000000-0000-0000-0000-000000000005', 'driver2@rideflow.com', crypt('Driver@12345', gen_salt('bf', 10)), 'DRIVER', now());