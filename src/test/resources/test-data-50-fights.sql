-- Скрипт для создания 50 боев с разнообразной статистикой
-- Очищаем существующие данные
DELETE FROM judge_scores;
DELETE FROM fight_rounds;
DELETE FROM fights;

-- Сброс автоинкремента
ALTER TABLE fights AUTO_INCREMENT = 1;

-- Массивы данных для генерации разнообразных боев
-- Имена бойцов
SET @fighters = '["Иван Петров", "Алексей Сидоров", "Дмитрий Козлов", "Сергей Волков", "Андрей Морозов", "Николай Соколов", "Владимир Лебедев", "Михаил Орлов", "Александр Новиков", "Евгений Медведев", "Роман Смирнов", "Игорь Кузнецов", "Антон Попов", "Максим Соколов", "Денис Васильев", "Артем Федоров", "Павел Козлов", "Степан Морозов", "Григорий Волков", "Тимофей Лебедев"]';

-- Имена соперников  
SET @opponents = '["Джон Смит", "Майк Джонсон", "Том Уилсон", "Джейк Браун", "Люк Дэвис", "Райан Миллер", "Кевин Гарсия", "Брэндон Мартинес", "Тайлер Андерсон", "Джошуа Тейлор", "Коди Уайт", "Джереми Кларк", "Зак Родригес", "Нейт Томпсон", "Блейк Мур", "Дрю Холл", "Кейси Янг", "Трой Кинг", "Дин Скотт", "Шон Ли"]';

-- Весовые категории
SET @weight_classes = '["FLYWEIGHT", "BANTAMWEIGHT", "FEATHERWEIGHT", "LIGHTWEIGHT", "WELTERWEIGHT", "MIDDLEWEIGHT", "LIGHT_HEAVYWEIGHT", "HEAVYWEIGHT"]';

-- Режимы боя
SET @fight_modes = '["MMA", "BOXING", "KICKBOXING"]';

-- Результаты
SET @results = '["WIN", "LOSS", "DRAW"]';

-- Методы
SET @methods = '["KNOCKOUT", "TKO", "DECISION", "SUBMISSION"]';

-- Создаем 50 боев с разнообразной статистикой
INSERT INTO fights (fight_date, fight_mode, season, result, method, rounds_played, rating_points, ranking_position, weight_class, my_fighter, opponent, notes, created_at, updated_at) VALUES
-- Бои 1-10: Высокий рейтинг (180-200 очков, места 1-5)
('2024-01-15 20:00:00', 'MMA', 1, 'WIN', 'DECISION', 3, 195, 1, 'LIGHTWEIGHT', 'Иван Петров', 'Джон Смит', 'Чемпионский бой', NOW(), NOW()),
('2024-01-20 21:00:00', 'MMA', 1, 'WIN', 'KNOCKOUT', 2, 190, 2, 'WELTERWEIGHT', 'Алексей Сидоров', 'Майк Джонсон', 'Нокаут в 2 раунде', NOW(), NOW()),
('2024-01-25 19:30:00', 'BOXING', 1, 'WIN', 'DECISION', 5, 185, 3, 'HEAVYWEIGHT', 'Дмитрий Козлов', 'Том Уилсон', 'Доминирующая победа', NOW(), NOW()),
('2024-02-01 20:30:00', 'MMA', 1, 'LOSS', 'SUBMISSION', 1, 180, 4, 'MIDDLEWEIGHT', 'Сергей Волков', 'Джейк Браун', 'Быстрое поражение', NOW(), NOW()),
('2024-02-05 21:15:00', 'KICKBOXING', 1, 'WIN', 'TKO', 3, 188, 5, 'FEATHERWEIGHT', 'Андрей Морозов', 'Люк Дэвис', 'Технический нокаут', NOW(), NOW()),
('2024-02-10 20:45:00', 'MMA', 1, 'WIN', 'DECISION', 4, 192, 1, 'LIGHT_HEAVYWEIGHT', 'Николай Соколов', 'Райан Миллер', 'Близкий бой', NOW(), NOW()),
('2024-02-15 19:00:00', 'BOXING', 1, 'LOSS', 'KNOCKOUT', 2, 175, 6, 'BANTAMWEIGHT', 'Владимир Лебедев', 'Кевин Гарсия', 'Нокаут соперника', NOW(), NOW()),
('2024-02-20 20:00:00', 'MMA', 1, 'WIN', 'SUBMISSION', 3, 186, 3, 'WELTERWEIGHT', 'Михаил Орлов', 'Брэндон Мартинес', 'Удушающий прием', NOW(), NOW()),
('2024-02-25 21:30:00', 'KICKBOXING', 1, 'DRAW', 'DECISION', 5, 170, 7, 'HEAVYWEIGHT', 'Александр Новиков', 'Тайлер Андерсон', 'Ничья по очкам', NOW(), NOW()),
('2024-03-01 20:15:00', 'MMA', 1, 'WIN', 'TKO', 2, 194, 2, 'LIGHTWEIGHT', 'Евгений Медведев', 'Джошуа Тейлор', 'Остановка рефери', NOW(), NOW()),

-- Бои 11-20: Средний рейтинг (120-180 очков, места 6-15)
('2024-03-05 19:45:00', 'BOXING', 1, 'WIN', 'DECISION', 4, 165, 8, 'MIDDLEWEIGHT', 'Роман Смирнов', 'Коди Уайт', 'Победа по очкам', NOW(), NOW()),
('2024-03-10 20:30:00', 'MMA', 1, 'LOSS', 'TKO', 3, 155, 12, 'FEATHERWEIGHT', 'Игорь Кузнецов', 'Джереми Кларк', 'Поражение техническим нокаутом', NOW(), NOW()),
('2024-03-15 21:00:00', 'KICKBOXING', 1, 'WIN', 'KNOCKOUT', 1, 172, 9, 'WELTERWEIGHT', 'Антон Попов', 'Зак Родригес', 'Быстрый нокаут', NOW(), NOW()),
('2024-03-20 19:30:00', 'MMA', 1, 'WIN', 'SUBMISSION', 4, 168, 10, 'LIGHT_HEAVYWEIGHT', 'Максим Соколов', 'Нейт Томпсон', 'Болевой прием', NOW(), NOW()),
('2024-03-25 20:45:00', 'BOXING', 1, 'LOSS', 'DECISION', 5, 145, 15, 'HEAVYWEIGHT', 'Денис Васильев', 'Блейк Мур', 'Поражение по очкам', NOW(), NOW()),
('2024-03-30 20:00:00', 'MMA', 1, 'WIN', 'DECISION', 3, 158, 11, 'BANTAMWEIGHT', 'Артем Федоров', 'Дрю Холл', 'Единогласное решение', NOW(), NOW()),
('2024-04-05 21:15:00', 'KICKBOXING', 1, 'DRAW', 'DECISION', 5, 150, 13, 'LIGHTWEIGHT', 'Павел Козлов', 'Кейси Янг', 'Раздельное решение', NOW(), NOW()),
('2024-04-10 19:45:00', 'MMA', 1, 'WIN', 'TKO', 2, 162, 14, 'MIDDLEWEIGHT', 'Степан Морозов', 'Трой Кинг', 'Остановка угла', NOW(), NOW()),
('2024-04-15 20:30:00', 'BOXING', 1, 'LOSS', 'KNOCKOUT', 3, 138, 18, 'WELTERWEIGHT', 'Григорий Волков', 'Дин Скотт', 'Нокаут в 3 раунде', NOW(), NOW()),
('2024-04-20 21:00:00', 'MMA', 1, 'WIN', 'SUBMISSION', 3, 174, 6, 'FEATHERWEIGHT', 'Тимофей Лебедев', 'Шон Ли', 'Удушение', NOW(), NOW()),

-- Бои 21-30: Низкий рейтинг (80-120 очков, места 16-25)
('2024-04-25 20:15:00', 'KICKBOXING', 1, 'LOSS', 'TKO', 2, 115, 20, 'LIGHT_HEAVYWEIGHT', 'Иван Петров', 'Джон Смит', 'Повторный бой', NOW(), NOW()),
('2024-04-30 19:30:00', 'MMA', 1, 'WIN', 'DECISION', 4, 125, 16, 'HEAVYWEIGHT', 'Алексей Сидоров', 'Майк Джонсон', 'Возвращение', NOW(), NOW()),
('2024-05-05 21:45:00', 'BOXING', 1, 'LOSS', 'DECISION', 5, 95, 25, 'BANTAMWEIGHT', 'Дмитрий Козлов', 'Том Уилсон', 'Слабое выступление', NOW(), NOW()),
('2024-05-10 20:00:00', 'MMA', 1, 'WIN', 'KNOCKOUT', 1, 135, 17, 'LIGHTWEIGHT', 'Сергей Волков', 'Джейк Браун', 'Реванш', NOW(), NOW()),
('2024-05-15 20:30:00', 'KICKBOXING', 1, 'DRAW', 'DECISION', 3, 105, 22, 'WELTERWEIGHT', 'Андрей Морозов', 'Люк Дэвис', 'Равный бой', NOW(), NOW()),
('2024-05-20 19:15:00', 'MMA', 1, 'LOSS', 'SUBMISSION', 2, 88, 28, 'MIDDLEWEIGHT', 'Николай Соколов', 'Райан Миллер', 'Быстрое поражение', NOW(), NOW()),
('2024-05-25 21:00:00', 'BOXING', 1, 'WIN', 'TKO', 3, 118, 19, 'FEATHERWEIGHT', 'Владимир Лебедев', 'Кевин Гарсия', 'Улучшение', NOW(), NOW()),
('2024-05-30 20:45:00', 'MMA', 1, 'LOSS', 'KNOCKOUT', 2, 92, 26, 'LIGHT_HEAVYWEIGHT', 'Михаил Орлов', 'Брэндон Мартинес', 'Нокаут', NOW(), NOW()),
('2024-06-05 19:00:00', 'KICKBOXING', 1, 'WIN', 'DECISION', 4, 128, 21, 'HEAVYWEIGHT', 'Александр Новиков', 'Тайлер Андерсон', 'Хорошая форма', NOW(), NOW()),
('2024-06-10 20:15:00', 'MMA', 1, 'DRAW', 'DECISION', 5, 110, 23, 'BANTAMWEIGHT', 'Евгений Медведев', 'Джошуа Тейлор', 'Близкая ничья', NOW(), NOW()),

-- Бои 31-40: Очень низкий рейтинг (40-80 очков, места 26-35)
('2024-06-15 21:30:00', 'BOXING', 1, 'LOSS', 'TKO', 1, 65, 32, 'LIGHTWEIGHT', 'Роман Смирнов', 'Коди Уайт', 'Плохое выступление', NOW(), NOW()),
('2024-06-20 19:45:00', 'MMA', 1, 'WIN', 'SUBMISSION', 3, 75, 29, 'WELTERWEIGHT', 'Игорь Кузнецов', 'Джереми Кларк', 'Неожиданная победа', NOW(), NOW()),
('2024-06-25 20:00:00', 'KICKBOXING', 1, 'LOSS', 'KNOCKOUT', 2, 45, 35, 'MIDDLEWEIGHT', 'Антон Попов', 'Зак Родригес', 'Тяжелое поражение', NOW(), NOW()),
('2024-06-30 21:15:00', 'MMA', 1, 'WIN', 'DECISION', 4, 82, 27, 'FEATHERWEIGHT', 'Максим Соколов', 'Нейт Томпсон', 'Трудная победа', NOW(), NOW()),
('2024-07-05 19:30:00', 'BOXING', 1, 'LOSS', 'DECISION', 5, 58, 33, 'HEAVYWEIGHT', 'Денис Васильев', 'Блейк Мур', 'Слабое выступление', NOW(), NOW()),
('2024-07-10 20:45:00', 'MMA', 1, 'DRAW', 'DECISION', 3, 68, 31, 'LIGHT_HEAVYWEIGHT', 'Артем Федоров', 'Дрю Холл', 'Равная борьба', NOW(), NOW()),
('2024-07-15 20:15:00', 'KICKBOXING', 1, 'WIN', 'TKO', 2, 78, 30, 'BANTAMWEIGHT', 'Павел Козлов', 'Кейси Янг', 'Хороший результат', NOW(), NOW()),
('2024-07-20 19:00:00', 'MMA', 1, 'LOSS', 'SUBMISSION', 1, 52, 34, 'WELTERWEIGHT', 'Степан Морозов', 'Трой Кинг', 'Быстрое поражение', NOW(), NOW()),
('2024-07-25 21:45:00', 'BOXING', 1, 'WIN', 'KNOCKOUT', 3, 85, 26, 'LIGHTWEIGHT', 'Григорий Волков', 'Дин Скотт', 'Отличный нокаут', NOW(), NOW()),
('2024-07-30 20:30:00', 'MMA', 1, 'LOSS', 'TKO', 2, 48, 36, 'MIDDLEWEIGHT', 'Тимофей Лебедев', 'Шон Ли', 'Остановка', NOW(), NOW()),

-- Бои 41-50: Начинающие бойцы (10-40 очков, места 36-50)
('2024-08-05 19:15:00', 'KICKBOXING', 1, 'LOSS', 'DECISION', 3, 25, 42, 'FEATHERWEIGHT', 'Иван Петров', 'Джон Смит', 'Дебют', NOW(), NOW()),
('2024-08-10 20:00:00', 'MMA', 1, 'WIN', 'SUBMISSION', 2, 35, 38, 'WELTERWEIGHT', 'Алексей Сидоров', 'Майк Джонсон', 'Первая победа', NOW(), NOW()),
('2024-08-15 21:30:00', 'BOXING', 1, 'LOSS', 'KNOCKOUT', 1, 15, 45, 'HEAVYWEIGHT', 'Дмитрий Козлов', 'Том Уилсон', 'Тяжелый дебют', NOW(), NOW()),
('2024-08-20 19:45:00', 'MMA', 1, 'DRAW', 'DECISION', 4, 30, 40, 'LIGHTWEIGHT', 'Сергей Волков', 'Джейк Браун', 'Равная борьба', NOW(), NOW()),
('2024-08-25 20:15:00', 'KICKBOXING', 1, 'WIN', 'TKO', 2, 40, 37, 'MIDDLEWEIGHT', 'Андрей Морозов', 'Люк Дэвис', 'Хороший старт', NOW(), NOW()),
('2024-08-30 19:30:00', 'MMA', 1, 'LOSS', 'SUBMISSION', 1, 20, 44, 'LIGHT_HEAVYWEIGHT', 'Николай Соколов', 'Райан Миллер', 'Быстрое поражение', NOW(), NOW()),
('2024-09-05 21:00:00', 'BOXING', 1, 'WIN', 'DECISION', 3, 38, 39, 'BANTAMWEIGHT', 'Владимир Лебедев', 'Кевин Гарсия', 'Трудная победа', NOW(), NOW()),
('2024-09-10 20:45:00', 'MMA', 1, 'LOSS', 'TKO', 2, 22, 43, 'FEATHERWEIGHT', 'Михаил Орлов', 'Брэндон Мартинес', 'Остановка', NOW(), NOW()),
('2024-09-15 19:00:00', 'KICKBOXING', 1, 'DRAW', 'DECISION', 5, 28, 41, 'WELTERWEIGHT', 'Александр Новиков', 'Тайлер Андерсон', 'Близкая ничья', NOW(), NOW()),
('2024-09-20 20:30:00', 'MMA', 1, 'WIN', 'KNOCKOUT', 1, 42, 36, 'HEAVYWEIGHT', 'Евгений Медведев', 'Джошуа Тейлор', 'Отличный нокаут', NOW(), NOW());

-- Создаем раунды для каждого боя (1-5 раундов в зависимости от rounds_played)
-- Бой 1 (3 раунда)
INSERT INTO fight_rounds (fight_id, round_number, my_head_damage, my_body_damage, my_leg_damage, my_knockdowns, my_significant_landed, my_significant_attempted, my_total_landed, my_total_attempted, my_takedowns_successful, my_takedowns_attempted, my_control_time, opponent_head_damage, opponent_body_damage, opponent_leg_damage, opponent_knockdowns, opponent_significant_landed, opponent_significant_attempted, opponent_total_landed, opponent_total_attempted, opponent_takedowns_successful, opponent_takedowns_attempted, opponent_control_time) VALUES
(1, 1, 15, 8, 5, 0, 28, 35, 45, 60, 2, 3, '02:30', 12, 6, 3, 0, 22, 30, 38, 50, 1, 2, '01:45'),
(1, 2, 18, 10, 6, 1, 32, 40, 48, 65, 3, 4, '03:00', 10, 5, 2, 0, 20, 28, 35, 48, 0, 1, '01:15'),
(1, 3, 20, 12, 8, 0, 35, 42, 52, 70, 2, 3, '02:45', 8, 4, 1, 0, 18, 25, 32, 45, 1, 2, '01:30');

-- Бой 2 (2 раунда)
INSERT INTO fight_rounds (fight_id, round_number, my_head_damage, my_body_damage, my_leg_damage, my_knockdowns, my_significant_landed, my_significant_attempted, my_total_landed, my_total_attempted, my_takedowns_successful, my_takedowns_attempted, my_control_time, opponent_head_damage, opponent_body_damage, opponent_leg_damage, opponent_knockdowns, opponent_significant_landed, opponent_significant_attempted, opponent_total_landed, opponent_total_attempted, opponent_takedowns_successful, opponent_takedowns_attempted, opponent_control_time) VALUES
(2, 1, 25, 15, 10, 1, 40, 50, 60, 80, 3, 4, '03:30', 18, 12, 8, 0, 30, 40, 45, 65, 1, 2, '02:00'),
(2, 2, 30, 18, 12, 2, 45, 55, 70, 90, 2, 3, '04:00', 15, 10, 6, 0, 25, 35, 40, 60, 0, 1, '01:30');

-- Бой 3 (5 раундов)
INSERT INTO fight_rounds (fight_id, round_number, my_head_damage, my_body_damage, my_leg_damage, my_knockdowns, my_significant_landed, my_significant_attempted, my_total_landed, my_total_attempted, my_takedowns_successful, my_takedowns_attempted, my_control_time, opponent_head_damage, opponent_body_damage, opponent_leg_damage, opponent_knockdowns, opponent_significant_landed, opponent_significant_attempted, opponent_total_landed, opponent_total_attempted, opponent_takedowns_successful, opponent_takedowns_attempted, opponent_control_time) VALUES
(3, 1, 12, 6, 4, 0, 22, 30, 35, 50, 1, 2, '02:00', 15, 8, 5, 0, 25, 35, 40, 55, 2, 3, '02:30'),
(3, 2, 15, 8, 6, 0, 25, 35, 40, 55, 2, 3, '02:30', 12, 6, 4, 0, 22, 30, 35, 50, 1, 2, '02:00'),
(3, 3, 18, 10, 8, 1, 28, 38, 45, 60, 2, 3, '03:00', 10, 5, 3, 0, 20, 28, 32, 48, 0, 1, '01:30'),
(3, 4, 20, 12, 10, 0, 30, 40, 50, 65, 3, 4, '03:30', 8, 4, 2, 0, 18, 25, 30, 45, 1, 2, '01:00'),
(3, 5, 22, 14, 12, 1, 32, 42, 55, 70, 2, 3, '04:00', 6, 3, 1, 0, 15, 22, 28, 42, 0, 1, '00:45');

-- Создаем судейские оценки для первых 10 боев
-- Бой 1 (3 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_total_score, opponent_total_score, winner_by_judge) VALUES
(1, 'Судья 1', 10, 9, 10, 9, 10, 9, 30, 27, 'MY_FIGHTER'),
(1, 'Судья 2', 10, 9, 9, 10, 10, 9, 29, 28, 'MY_FIGHTER'),
(1, 'Судья 3', 10, 9, 10, 9, 9, 10, 29, 28, 'MY_FIGHTER');

-- Бой 2 (2 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_total_score, opponent_total_score, winner_by_judge) VALUES
(2, 'Судья 1', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER'),
(2, 'Судья 2', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER'),
(2, 'Судья 3', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER');

-- Бой 3 (5 раундов)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_round4, opponent_round4, my_round5, opponent_round5, my_total_score, opponent_total_score, winner_by_judge) VALUES
(3, 'Судья 1', 9, 10, 10, 9, 10, 9, 10, 9, 10, 9, 49, 46, 'MY_FIGHTER'),
(3, 'Судья 2', 9, 10, 9, 10, 10, 9, 10, 9, 10, 9, 48, 47, 'MY_FIGHTER'),
(3, 'Судья 3', 9, 10, 10, 9, 9, 10, 10, 9, 10, 9, 48, 47, 'MY_FIGHTER');

-- Бой 4 (1 раунд)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_total_score, opponent_total_score, winner_by_judge) VALUES
(4, 'Судья 1', 8, 10, 8, 10, 'OPPONENT'),
(4, 'Судья 2', 8, 10, 8, 10, 'OPPONENT'),
(4, 'Судья 3', 8, 10, 8, 10, 'OPPONENT');

-- Бой 5 (3 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_total_score, opponent_total_score, winner_by_judge) VALUES
(5, 'Судья 1', 10, 9, 10, 9, 10, 9, 30, 27, 'MY_FIGHTER'),
(5, 'Судья 2', 10, 9, 9, 10, 10, 9, 29, 28, 'MY_FIGHTER'),
(5, 'Судья 3', 9, 10, 10, 9, 10, 9, 29, 28, 'MY_FIGHTER');

-- Бой 6 (4 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_round4, opponent_round4, my_total_score, opponent_total_score, winner_by_judge) VALUES
(6, 'Судья 1', 9, 10, 10, 9, 10, 9, 10, 9, 39, 37, 'MY_FIGHTER'),
(6, 'Судья 2', 9, 10, 9, 10, 10, 9, 10, 9, 38, 38, 'DRAW'),
(6, 'Судья 3', 9, 10, 10, 9, 9, 10, 10, 9, 38, 38, 'DRAW');

-- Бой 7 (2 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_total_score, opponent_total_score, winner_by_judge) VALUES
(7, 'Судья 1', 8, 10, 8, 10, 16, 20, 'OPPONENT'),
(7, 'Судья 2', 8, 10, 8, 10, 16, 20, 'OPPONENT'),
(7, 'Судья 3', 8, 10, 8, 10, 16, 20, 'OPPONENT');

-- Бой 8 (3 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_total_score, opponent_total_score, winner_by_judge) VALUES
(8, 'Судья 1', 10, 9, 10, 9, 10, 9, 30, 27, 'MY_FIGHTER'),
(8, 'Судья 2', 10, 9, 9, 10, 10, 9, 29, 28, 'MY_FIGHTER'),
(8, 'Судья 3', 9, 10, 10, 9, 10, 9, 29, 28, 'MY_FIGHTER');

-- Бой 9 (5 раундов)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_round3, opponent_round3, my_round4, opponent_round4, my_round5, opponent_round5, my_total_score, opponent_total_score, winner_by_judge) VALUES
(9, 'Судья 1', 9, 10, 10, 9, 9, 10, 10, 9, 9, 10, 47, 48, 'OPPONENT'),
(9, 'Судья 2', 9, 10, 9, 10, 10, 9, 9, 10, 10, 9, 47, 48, 'OPPONENT'),
(9, 'Судья 3', 10, 9, 9, 10, 9, 10, 10, 9, 9, 10, 47, 48, 'OPPONENT');

-- Бой 10 (2 раунда)
INSERT INTO judge_scores (fight_id, judge_name, my_round1, opponent_round1, my_round2, opponent_round2, my_total_score, opponent_total_score, winner_by_judge) VALUES
(10, 'Судья 1', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER'),
(10, 'Судья 2', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER'),
(10, 'Судья 3', 10, 8, 10, 8, 20, 16, 'MY_FIGHTER');
