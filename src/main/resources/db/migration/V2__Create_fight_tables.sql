-- Создание таблиц для системы статистики боев UFC 5

-- Таблица боев
CREATE TABLE IF NOT EXISTS fights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fight_date TIMESTAMP NOT NULL,
    fight_mode VARCHAR(20) NOT NULL,
    season INTEGER NOT NULL,
    result VARCHAR(20) NOT NULL,
    method VARCHAR(20) NOT NULL,
    rounds_played INTEGER NOT NULL CHECK (rounds_played >= 1 AND rounds_played <= 5),
    rating_points INTEGER CHECK (rating_points >= 0),
    ranking_position INTEGER CHECK (ranking_position >= 1),
    weight_class VARCHAR(20) NOT NULL,
    my_fighter VARCHAR(100) NOT NULL,
    opponent VARCHAR(100) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_season CHECK (season >= 1),
    CONSTRAINT chk_rounds CHECK (rounds_played >= 1 AND rounds_played <= 5)
);

-- Таблица раундов боев
CREATE TABLE IF NOT EXISTS fight_rounds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fight_id BIGINT NOT NULL,
    round_number INTEGER NOT NULL CHECK (round_number >= 1 AND round_number <= 5),
    
    -- Статистика моего бойца
    my_head_damage INTEGER NOT NULL DEFAULT 0 CHECK (my_head_damage >= 0),
    my_body_damage INTEGER NOT NULL DEFAULT 0 CHECK (my_body_damage >= 0),
    my_leg_damage INTEGER NOT NULL DEFAULT 0 CHECK (my_leg_damage >= 0),
    my_knockdowns INTEGER NOT NULL DEFAULT 0 CHECK (my_knockdowns >= 0),
    my_significant_strikes_landed INTEGER NOT NULL DEFAULT 0 CHECK (my_significant_strikes_landed >= 0),
    my_significant_strikes_attempted INTEGER NOT NULL DEFAULT 0 CHECK (my_significant_strikes_attempted >= 0),
    my_total_strikes_landed INTEGER NOT NULL DEFAULT 0 CHECK (my_total_strikes_landed >= 0),
    my_total_strikes_attempted INTEGER NOT NULL DEFAULT 0 CHECK (my_total_strikes_attempted >= 0),
    my_takedowns_successful INTEGER NOT NULL DEFAULT 0 CHECK (my_takedowns_successful >= 0),
    my_takedowns_attempted INTEGER NOT NULL DEFAULT 0 CHECK (my_takedowns_attempted >= 0),
    my_control_time VARCHAR(8) DEFAULT '00:00',
    
    -- Статистика соперника
    opponent_head_damage INTEGER NOT NULL DEFAULT 0 CHECK (opponent_head_damage >= 0),
    opponent_body_damage INTEGER NOT NULL DEFAULT 0 CHECK (opponent_body_damage >= 0),
    opponent_leg_damage INTEGER NOT NULL DEFAULT 0 CHECK (opponent_leg_damage >= 0),
    opponent_knockdowns INTEGER NOT NULL DEFAULT 0 CHECK (opponent_knockdowns >= 0),
    opponent_significant_strikes_landed INTEGER NOT NULL DEFAULT 0 CHECK (opponent_significant_strikes_landed >= 0),
    opponent_significant_strikes_attempted INTEGER NOT NULL DEFAULT 0 CHECK (opponent_significant_strikes_attempted >= 0),
    opponent_total_strikes_landed INTEGER NOT NULL DEFAULT 0 CHECK (opponent_total_strikes_landed >= 0),
    opponent_total_strikes_attempted INTEGER NOT NULL DEFAULT 0 CHECK (opponent_total_strikes_attempted >= 0),
    opponent_takedowns_successful INTEGER NOT NULL DEFAULT 0 CHECK (opponent_takedowns_successful >= 0),
    opponent_takedowns_attempted INTEGER NOT NULL DEFAULT 0 CHECK (opponent_takedowns_attempted >= 0),
    opponent_control_time VARCHAR(8) DEFAULT '00:00',
    
    FOREIGN KEY (fight_id) REFERENCES fights(id) ON DELETE CASCADE,
    CONSTRAINT unique_fight_round UNIQUE (fight_id, round_number)
);

-- Таблица судейских оценок
CREATE TABLE IF NOT EXISTS judge_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fight_id BIGINT NOT NULL,
    judge_number INTEGER NOT NULL CHECK (judge_number >= 1 AND judge_number <= 3),
    
    -- Оценки за раунды (мой боец)
    round_1_my_score INTEGER DEFAULT 0 CHECK (round_1_my_score >= 0 AND round_1_my_score <= 10),
    round_2_my_score INTEGER DEFAULT 0 CHECK (round_2_my_score >= 0 AND round_2_my_score <= 10),
    round_3_my_score INTEGER DEFAULT 0 CHECK (round_3_my_score >= 0 AND round_3_my_score <= 10),
    round_4_my_score INTEGER DEFAULT 0 CHECK (round_4_my_score >= 0 AND round_4_my_score <= 10),
    round_5_my_score INTEGER DEFAULT 0 CHECK (round_5_my_score >= 0 AND round_5_my_score <= 10),
    
    -- Оценки за раунды (соперник)
    round_1_opponent_score INTEGER DEFAULT 0 CHECK (round_1_opponent_score >= 0 AND round_1_opponent_score <= 10),
    round_2_opponent_score INTEGER DEFAULT 0 CHECK (round_2_opponent_score >= 0 AND round_2_opponent_score <= 10),
    round_3_opponent_score INTEGER DEFAULT 0 CHECK (round_3_opponent_score >= 0 AND round_3_opponent_score <= 10),
    round_4_opponent_score INTEGER DEFAULT 0 CHECK (round_4_opponent_score >= 0 AND round_4_opponent_score <= 10),
    round_5_opponent_score INTEGER DEFAULT 0 CHECK (round_5_opponent_score >= 0 AND round_5_opponent_score <= 10),
    
    FOREIGN KEY (fight_id) REFERENCES fights(id) ON DELETE CASCADE,
    CONSTRAINT unique_fight_judge UNIQUE (fight_id, judge_number)
);

-- Создание индексов для оптимизации запросов
CREATE INDEX idx_fights_date ON fights(fight_date);
CREATE INDEX idx_fights_result ON fights(result);
CREATE INDEX idx_fights_mode ON fights(fight_mode);
CREATE INDEX idx_fights_weight_class ON fights(weight_class);
CREATE INDEX idx_fights_season ON fights(season);
CREATE INDEX idx_fights_my_fighter ON fights(my_fighter);
CREATE INDEX idx_fights_opponent ON fights(opponent);
CREATE INDEX idx_fights_rating ON fights(rating_points);
CREATE INDEX idx_fights_ranking ON fights(ranking_position);

CREATE INDEX idx_fight_rounds_fight_id ON fight_rounds(fight_id);
CREATE INDEX idx_fight_rounds_round_number ON fight_rounds(round_number);

CREATE INDEX idx_judge_scores_fight_id ON judge_scores(fight_id);
CREATE INDEX idx_judge_scores_judge_number ON judge_scores(judge_number);

-- Комментарии к таблицам
ALTER TABLE fights COMMENT = 'Таблица боев UFC 5';
ALTER TABLE fight_rounds COMMENT = 'Статистика раундов боев';
ALTER TABLE judge_scores COMMENT = 'Судейские оценки за бои';
