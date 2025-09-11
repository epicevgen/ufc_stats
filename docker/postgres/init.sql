-- PostgreSQL initialization script for UFC Stats

-- Create database if not exists (handled by POSTGRES_DB env var)
-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create initial schema (will be managed by Flyway in production)
-- This is just for Docker initialization

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE ufc_stats TO ufc_stats;
