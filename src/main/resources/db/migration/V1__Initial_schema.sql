-- Initial database schema for UFC Stats application
-- This file will be updated when you provide the business logic requirements

-- Create a simple table structure as a placeholder
-- This will be replaced with actual fight and statistics tables

CREATE TABLE IF NOT EXISTS app_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    version VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial app info
INSERT INTO app_info (version) VALUES ('1.0.0');

-- Placeholder for future tables:
-- - fights (бои)
-- - fighters (бойцы) 
-- - fight_rounds (раунды боев)
-- - fight_statistics (статистика боев)
-- - user_settings (настройки пользователя)
