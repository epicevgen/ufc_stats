package com.ufcstats.controller;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.*;
import com.ufcstats.repository.FightRepository;
import com.ufcstats.repository.FightRoundRepository;
import com.ufcstats.repository.JudgeScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Контроллер для генерации тестовых данных
 */
@Slf4j
@RestController
@RequestMapping("/api/test-data")
@RequiredArgsConstructor
public class TestDataController {

    private final FightRepository fightRepository;
    private final FightRoundRepository fightRoundRepository;
    private final JudgeScoreRepository judgeScoreRepository;
    
    private final Random random = new Random();
    
    // Массивы данных для генерации
    private final String[] fighters = {
        "Иван Петров", "Алексей Сидоров", "Дмитрий Козлов", "Сергей Волков", "Андрей Морозов",
        "Николай Соколов", "Владимир Лебедев", "Михаил Орлов", "Александр Новиков", "Евгений Медведев",
        "Роман Смирнов", "Игорь Кузнецов", "Антон Попов", "Максим Соколов", "Денис Васильев",
        "Артем Федоров", "Павел Козлов", "Степан Морозов", "Григорий Волков", "Тимофей Лебедев"
    };
    
    private final String[] opponents = {
        "Джон Смит", "Майк Джонсон", "Том Уилсон", "Джейк Браун", "Люк Дэвис",
        "Райан Миллер", "Кевин Гарсия", "Брэндон Мартинес", "Тайлер Андерсон", "Джошуа Тейлор",
        "Коди Уайт", "Джереми Кларк", "Зак Родригес", "Нейт Томпсон", "Блейк Мур",
        "Дрю Холл", "Кейси Янг", "Трой Кинг", "Дин Скотт", "Шон Ли"
    };
    
    private final FightMode[] fightModes = FightMode.values();
    private final WeightClass[] weightClasses = WeightClass.values();
    private final FightResult[] results = FightResult.values();
    private final FightMethod[] methods = FightMethod.values();

    @PostMapping("/generate")
    public String generateTestData(@RequestParam(defaultValue = "50") int count) {
        log.info("Начинаем генерацию {} боев с разнообразной статистикой...", count);
        
        // Очищаем существующие данные
        judgeScoreRepository.deleteAll();
        fightRoundRepository.deleteAll();
        fightRepository.deleteAll();
        
        List<Fight> fights = new ArrayList<>();
        
        // Создаем бои
        for (int i = 0; i < count; i++) {
            Fight fight = createFight(i);
            fights.add(fight);
        }
        
        // Сохраняем бои
        fightRepository.saveAll(fights);
        log.info("Сохранено {} боев", fights.size());
        
        // Создаем раунды и судейские оценки для каждого боя
        for (Fight fight : fights) {
            createRoundsForFight(fight);
            createJudgeScoresForFight(fight);
        }
        
        log.info("Генерация тестовых данных завершена!");
        return "Сгенерировано " + count + " боев с полной статистикой";
    }
    
    private Fight createFight(int index) {
        Fight fight = new Fight();
        
        // Базовая информация
        fight.setFightDate(LocalDateTime.now().minusDays(365 - index * 7));
        fight.setFightMode(fightModes[random.nextInt(fightModes.length)]);
        fight.setSeason(1);
        fight.setResult(results[random.nextInt(results.length)]);
        fight.setMethod(methods[random.nextInt(methods.length)]);
        fight.setWeightClass(weightClasses[random.nextInt(weightClasses.length)]);
        
        // Имена бойцов
        fight.setMyFighter(fighters[random.nextInt(fighters.length)]);
        fight.setOpponent(opponents[random.nextInt(opponents.length)]);
        
        // Количество раундов (1-5)
        int rounds = random.nextInt(5) + 1;
        fight.setRoundsPlayed(rounds);
        
        // Рейтинг и место в зависимости от индекса
        if (index < 10) {
            // Топ-10: высокий рейтинг
            fight.setRatingPoints(180 + random.nextInt(21)); // 180-200
            fight.setRankingPosition(1 + random.nextInt(5)); // 1-5
        } else if (index < 20) {
            // 11-20: средний рейтинг
            fight.setRatingPoints(120 + random.nextInt(61)); // 120-180
            fight.setRankingPosition(6 + random.nextInt(10)); // 6-15
        } else if (index < 30) {
            // 21-30: низкий рейтинг
            fight.setRatingPoints(80 + random.nextInt(41)); // 80-120
            fight.setRankingPosition(16 + random.nextInt(10)); // 16-25
        } else if (index < 40) {
            // 31-40: очень низкий рейтинг
            fight.setRatingPoints(40 + random.nextInt(41)); // 40-80
            fight.setRankingPosition(26 + random.nextInt(10)); // 26-35
        } else {
            // 41-50: начинающие
            fight.setRatingPoints(10 + random.nextInt(31)); // 10-40
            fight.setRankingPosition(36 + random.nextInt(15)); // 36-50
        }
        
        // Примечания
        fight.setNotes("Тестовый бой #" + (index + 1) + " - " + getRandomNote());
        
        return fight;
    }
    
    private void createRoundsForFight(Fight fight) {
        int rounds = fight.getRoundsPlayed();
        
        for (int round = 1; round <= rounds; round++) {
            FightRound fightRound = new FightRound();
            fightRound.setFight(fight);
            fightRound.setRoundNumber(round);
            
            // Статистика моего бойца (обычно лучше для побед)
            boolean isWin = fight.getResult() == FightResult.WIN;
            int myMultiplier = isWin ? 1 : (random.nextBoolean() ? 1 : 0);
            
            fightRound.setMyHeadDamage(10 + random.nextInt(21) * myMultiplier);
            fightRound.setMyBodyDamage(5 + random.nextInt(11) * myMultiplier);
            fightRound.setMyLegDamage(3 + random.nextInt(8) * myMultiplier);
            fightRound.setMyKnockdowns(random.nextInt(3) * myMultiplier);
            fightRound.setMySignificantStrikesLanded(20 + random.nextInt(31) * myMultiplier);
            fightRound.setMySignificantStrikesAttempted(fightRound.getMySignificantStrikesLanded() + random.nextInt(21));
            fightRound.setMyTotalStrikesLanded(30 + random.nextInt(41) * myMultiplier);
            fightRound.setMyTotalStrikesAttempted(fightRound.getMyTotalStrikesLanded() + random.nextInt(31));
            fightRound.setMyTakedownsSuccessful(random.nextInt(4) * myMultiplier);
            fightRound.setMyTakedownsAttempted(fightRound.getMyTakedownsSuccessful() + random.nextInt(3));
            fightRound.setMyControlTime(String.format("%02d:%02d", random.nextInt(5), random.nextInt(60)));
            
            // Статистика соперника
            fightRound.setOpponentHeadDamage(8 + random.nextInt(17));
            fightRound.setOpponentBodyDamage(4 + random.nextInt(9));
            fightRound.setOpponentLegDamage(2 + random.nextInt(6));
            fightRound.setOpponentKnockdowns(random.nextInt(2));
            fightRound.setOpponentSignificantStrikesLanded(15 + random.nextInt(26));
            fightRound.setOpponentSignificantStrikesAttempted(fightRound.getOpponentSignificantStrikesLanded() + random.nextInt(16));
            fightRound.setOpponentTotalStrikesLanded(25 + random.nextInt(36));
            fightRound.setOpponentTotalStrikesAttempted(fightRound.getOpponentTotalStrikesLanded() + random.nextInt(26));
            fightRound.setOpponentTakedownsSuccessful(random.nextInt(3));
            fightRound.setOpponentTakedownsAttempted(fightRound.getOpponentTakedownsSuccessful() + random.nextInt(2));
            fightRound.setOpponentControlTime(String.format("%02d:%02d", random.nextInt(3), random.nextInt(60)));
            
            fightRoundRepository.save(fightRound);
        }
    }
    
    private void createJudgeScoresForFight(Fight fight) {
        int rounds = fight.getRoundsPlayed();
        
        for (int judge = 1; judge <= 3; judge++) {
            JudgeScore judgeScore = new JudgeScore();
            judgeScore.setFight(fight);
            judgeScore.setJudgeNumber(judge);
            
            // Заполняем оценки по раундам
            int myTotal = 0;
            int opponentTotal = 0;
            
            for (int round = 1; round <= rounds; round++) {
                int myRound = 8 + random.nextInt(3); // 8-10
                int opponentRound = 8 + random.nextInt(3); // 8-10
                
                // Учитываем результат боя, но не превышаем 10
                if (fight.getResult() == FightResult.WIN) {
                    myRound = Math.min(Math.max(myRound, opponentRound + 1), 10);
                } else if (fight.getResult() == FightResult.LOSS) {
                    opponentRound = Math.min(Math.max(opponentRound, myRound + 1), 10);
                }
                
                setJudgeRoundScore(judgeScore, round, myRound, opponentRound);
                myTotal += myRound;
                opponentTotal += opponentRound;
            }
            
            // Общие оценки и победитель вычисляются автоматически в модели
            
            judgeScoreRepository.save(judgeScore);
        }
    }
    
    private void setJudgeRoundScore(JudgeScore judgeScore, int round, int myScore, int opponentScore) {
        switch (round) {
            case 1:
                judgeScore.setRound1MyScore(myScore);
                judgeScore.setRound1OpponentScore(opponentScore);
                break;
            case 2:
                judgeScore.setRound2MyScore(myScore);
                judgeScore.setRound2OpponentScore(opponentScore);
                break;
            case 3:
                judgeScore.setRound3MyScore(myScore);
                judgeScore.setRound3OpponentScore(opponentScore);
                break;
            case 4:
                judgeScore.setRound4MyScore(myScore);
                judgeScore.setRound4OpponentScore(opponentScore);
                break;
            case 5:
                judgeScore.setRound5MyScore(myScore);
                judgeScore.setRound5OpponentScore(opponentScore);
                break;
        }
    }
    
    private String getRandomNote() {
        String[] notes = {
            "Отличный бой", "Доминирующая победа", "Близкий бой", "Неожиданный результат",
            "Техническое превосходство", "Борьба до конца", "Быстрая победа", "Трудная битва",
            "Показательный бой", "Эмоциональный поединок", "Стратегическая победа", "Физическое превосходство"
        };
        return notes[random.nextInt(notes.length)];
    }
}
