package com.ufcstats.util;

import com.ufcstats.model.*;
import com.ufcstats.model.enums.*;
import com.ufcstats.service.FightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Создатель тестовых данных для демонстрации функциональности
 */
@Component
public class TestDataCreator implements CommandLineRunner {

    @Autowired
    private FightService fightService;
    
    @Autowired
    private com.ufcstats.repository.FightRepository fightRepository;

    @Autowired
    private com.ufcstats.repository.FightRoundRepository fightRoundRepository;

    @Autowired
    private com.ufcstats.repository.JudgeScoreRepository judgeScoreRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создаем несколько боев для тестирования пагинации
        createMultipleFights();
    }

    private void createSimpleFight() {
        System.out.println("🥊 Создание простого тестового боя...");
        
        Fight fight = new Fight();
        fight.setFightDate(LocalDate.of(2024, 12, 15).atStartOfDay());
        fight.setFightMode(FightMode.STANCE);
        fight.setSeason(1);
        fight.setResult(FightResult.WIN);
        fight.setMethod(FightMethod.DECISION);
        fight.setRoundsPlayed(3);
        fight.setRatingPoints(85);
        fight.setRankingPosition(5);
        fight.setWeightClass(WeightClass.LIGHTWEIGHT);
        fight.setMyFighter("Тестовый Боец");
        fight.setOpponent("Тестовый Соперник");
        fight.setNotes("Простой тестовый бой для проверки функции просмотра");
        
        Fight savedFight = fightRepository.save(fight);
        System.out.println("✅ Простой тестовый бой создан с ID: " + savedFight.getId());
        System.out.println("🥊 Бой: " + savedFight.getMyFighter() + " vs " + savedFight.getOpponent());
    }

    private void createFullFight() {
        System.out.println("🥊 Создание полностью заполненного 5-раундового боя...");

        Fight fight = new Fight();
        
        // Основная информация о бое
        fight.setFightDate(LocalDate.of(2024, 12, 15).atStartOfDay());
        fight.setFightMode(FightMode.STANCE);
        fight.setSeason(3);
        fight.setResult(FightResult.WIN);
        fight.setMethod(FightMethod.DECISION);
        fight.setRoundsPlayed(5);
        fight.setRatingPoints(95);
        fight.setRankingPosition(3);
        fight.setWeightClass(WeightClass.MIDDLEWEIGHT);
        fight.setMyFighter("Александр Волков");
        fight.setOpponent("Джон Джонс");
        fight.setNotes("Эпический 5-раундовый бой за титул чемпиона. Оба бойца показали выдающуюся технику и выносливость.");

        // Создаем статистику по раундам
        List<FightRound> rounds = new ArrayList<>();
        
        // Раунд 1 - активный старт
        FightRound round1 = new FightRound();
        round1.setRoundNumber(1);
        round1.setMyHeadDamage(15);
        round1.setMyBodyDamage(8);
        round1.setMyLegDamage(3);
        round1.setMyKnockdowns(0);
        round1.setOpponentHeadDamage(12);
        round1.setOpponentBodyDamage(6);
        round1.setOpponentLegDamage(2);
        round1.setOpponentKnockdowns(0);
        round1.setMySignificantStrikesAttempted(45);
        round1.setMySignificantStrikesLanded(28);
        round1.setOpponentSignificantStrikesAttempted(38);
        round1.setOpponentSignificantStrikesLanded(22);
        round1.setMyTotalStrikesAttempted(67);
        round1.setMyTotalStrikesLanded(41);
        round1.setOpponentTotalStrikesAttempted(55);
        round1.setOpponentTotalStrikesLanded(33);
        round1.setMyTakedownsAttempted(2);
        round1.setMyTakedownsSuccessful(1);
        round1.setOpponentTakedownsAttempted(1);
        round1.setOpponentTakedownsSuccessful(0);
        round1.setMyControlTime("02:15");
        round1.setOpponentControlTime("00:45");
        rounds.add(round1);

        // Раунд 2 - обострение
        FightRound round2 = new FightRound();
        round2.setRoundNumber(2);
        round2.setMyHeadDamage(18);
        round2.setMyBodyDamage(12);
        round2.setMyLegDamage(5);
        round2.setMyKnockdowns(0);
        round2.setOpponentHeadDamage(16);
        round2.setOpponentBodyDamage(9);
        round2.setOpponentLegDamage(4);
        round2.setOpponentKnockdowns(0);
        round2.setMySignificantStrikesAttempted(52);
        round2.setMySignificantStrikesLanded(35);
        round2.setOpponentSignificantStrikesAttempted(44);
        round2.setOpponentSignificantStrikesLanded(29);
        round2.setMyTotalStrikesAttempted(78);
        round2.setMyTotalStrikesLanded(52);
        round2.setOpponentTotalStrikesAttempted(68);
        round2.setOpponentTotalStrikesLanded(45);
        round2.setMyTakedownsAttempted(3);
        round2.setMyTakedownsSuccessful(2);
        round2.setOpponentTakedownsAttempted(2);
        round2.setOpponentTakedownsSuccessful(1);
        round2.setMyControlTime("03:30");
        round2.setOpponentControlTime("01:20");
        rounds.add(round2);

        // Раунд 3 - кульминация
        FightRound round3 = new FightRound();
        round3.setRoundNumber(3);
        round3.setMyHeadDamage(22);
        round3.setMyBodyDamage(15);
        round3.setMyLegDamage(7);
        round3.setMyKnockdowns(0);
        round3.setOpponentHeadDamage(20);
        round3.setOpponentBodyDamage(13);
        round3.setOpponentLegDamage(6);
        round3.setOpponentKnockdowns(0);
        round3.setMySignificantStrikesAttempted(58);
        round3.setMySignificantStrikesLanded(42);
        round3.setOpponentSignificantStrikesAttempted(49);
        round3.setOpponentSignificantStrikesLanded(35);
        round3.setMyTotalStrikesAttempted(85);
        round3.setMyTotalStrikesLanded(62);
        round3.setOpponentTotalStrikesAttempted(75);
        round3.setOpponentTotalStrikesLanded(54);
        round3.setMyTakedownsAttempted(4);
        round3.setMyTakedownsSuccessful(3);
        round3.setOpponentTakedownsAttempted(3);
        round3.setOpponentTakedownsSuccessful(2);
        round3.setMyControlTime("04:15");
        round3.setOpponentControlTime("02:10");
        rounds.add(round3);

        // Раунд 4 - выносливость
        FightRound round4 = new FightRound();
        round4.setRoundNumber(4);
        round4.setMyHeadDamage(19);
        round4.setMyBodyDamage(11);
        round4.setMyLegDamage(4);
        round4.setMyKnockdowns(0);
        round4.setOpponentHeadDamage(17);
        round4.setOpponentBodyDamage(10);
        round4.setOpponentLegDamage(3);
        round4.setOpponentKnockdowns(0);
        round4.setMySignificantStrikesAttempted(48);
        round4.setMySignificantStrikesLanded(32);
        round4.setOpponentSignificantStrikesAttempted(42);
        round4.setOpponentSignificantStrikesLanded(28);
        round4.setMyTotalStrikesAttempted(72);
        round4.setMyTotalStrikesLanded(48);
        round4.setOpponentTotalStrikesAttempted(65);
        round4.setOpponentTotalStrikesLanded(43);
        round4.setMyTakedownsAttempted(2);
        round4.setMyTakedownsSuccessful(1);
        round4.setOpponentTakedownsAttempted(2);
        round4.setOpponentTakedownsSuccessful(1);
        round4.setMyControlTime("02:45");
        round4.setOpponentControlTime("01:55");
        rounds.add(round4);

        // Раунд 5 - финиш
        FightRound round5 = new FightRound();
        round5.setRoundNumber(5);
        round5.setMyHeadDamage(16);
        round5.setMyBodyDamage(9);
        round5.setMyLegDamage(3);
        round5.setMyKnockdowns(0);
        round5.setOpponentHeadDamage(14);
        round5.setOpponentBodyDamage(8);
        round5.setOpponentLegDamage(2);
        round5.setOpponentKnockdowns(0);
        round5.setMySignificantStrikesAttempted(41);
        round5.setMySignificantStrikesLanded(27);
        round5.setOpponentSignificantStrikesAttempted(36);
        round5.setOpponentSignificantStrikesLanded(24);
        round5.setMyTotalStrikesAttempted(63);
        round5.setMyTotalStrikesLanded(42);
        round5.setOpponentTotalStrikesAttempted(58);
        round5.setOpponentTotalStrikesLanded(39);
        round5.setMyTakedownsAttempted(1);
        round5.setMyTakedownsSuccessful(1);
        round5.setOpponentTakedownsAttempted(1);
        round5.setOpponentTakedownsSuccessful(0);
        round5.setMyControlTime("01:30");
        round5.setOpponentControlTime("00:30");
        rounds.add(round5);

        // Сначала сохраняем бой без раундов и судейских оценок
        Fight savedFight = fightRepository.save(fight);
        
        // Теперь сохраняем раунды с привязкой к бою
        for (FightRound round : rounds) {
            round.setFight(savedFight);
            fightRoundRepository.save(round);
        }
        
        // Теперь создаем судейские оценки с привязкой к сохраненному бою
        List<JudgeScore> judgeScores = new ArrayList<>();

        // Судья 1 - близкий счет
        JudgeScore judge1 = new JudgeScore();
        judge1.setFight(savedFight);
        judge1.setJudgeNumber(1);
        judge1.setRound1MyScore(10);
        judge1.setRound1OpponentScore(9);
        judge1.setRound2MyScore(10);
        judge1.setRound2OpponentScore(9);
        judge1.setRound3MyScore(10);
        judge1.setRound3OpponentScore(9);
        judge1.setRound4MyScore(9);
        judge1.setRound4OpponentScore(10);
        judge1.setRound5MyScore(10);
        judge1.setRound5OpponentScore(9);
        judgeScores.add(judge1);

        // Судья 2 - более убедительная победа
        JudgeScore judge2 = new JudgeScore();
        judge2.setFight(savedFight);
        judge2.setJudgeNumber(2);
        judge2.setRound1MyScore(10);
        judge2.setRound1OpponentScore(9);
        judge2.setRound2MyScore(10);
        judge2.setRound2OpponentScore(9);
        judge2.setRound3MyScore(10);
        judge2.setRound3OpponentScore(9);
        judge2.setRound4MyScore(10);
        judge2.setRound4OpponentScore(9);
        judge2.setRound5MyScore(10);
        judge2.setRound5OpponentScore(9);
        judgeScores.add(judge2);

        // Судья 3 - единогласное решение
        JudgeScore judge3 = new JudgeScore();
        judge3.setFight(savedFight);
        judge3.setJudgeNumber(3);
        judge3.setRound1MyScore(10);
        judge3.setRound1OpponentScore(9);
        judge3.setRound2MyScore(10);
        judge3.setRound2OpponentScore(9);
        judge3.setRound3MyScore(10);
        judge3.setRound3OpponentScore(9);
        judge3.setRound4MyScore(9);
        judge3.setRound4OpponentScore(10);
        judge3.setRound5MyScore(10);
        judge3.setRound5OpponentScore(9);
        judgeScores.add(judge3);

        // Сохраняем судейские оценки с привязкой к бою
        try {
            for (JudgeScore judgeScore : judgeScores) {
                judgeScore.setFight(savedFight);
                judgeScoreRepository.save(judgeScore);
            }
            System.out.println("✅ Полностью заполненный 5-раундовый бой создан с ID: " + savedFight.getId());
            System.out.println("🥊 Бой: " + savedFight.getMyFighter() + " vs " + savedFight.getOpponent());
            System.out.println("📊 Статистика: 5 раундов, 3 судьи, полная статистика по всем показателям");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при сохранении судейских оценок: " + e.getMessage());
            e.printStackTrace();
            System.out.println("✅ Бой создан без судейских оценок с ID: " + savedFight.getId());
        }
    }

    private void createMultipleFights() {
        System.out.println("🥊 Создание множественных тестовых боев для пагинации...");
        
        // Проверяем, есть ли уже бои в базе
        long existingFights = fightRepository.count();
        if (existingFights >= 25) {
            System.out.println("✅ Достаточно боев уже существует: " + existingFights);
            return;
        }
        
        // Создаем 25 боев для тестирования пагинации
        String[] fighters = {"Иван Петров", "Алексей Сидоров", "Дмитрий Волков", "Сергей Козлов", "Андрей Морозов"};
        String[] opponents = {"Джон Смит", "Майк Джонсон", "Том Уилсон", "Джейк Браун", "Боб Дэвис"};
        
        for (int i = 0; i < 25; i++) {
            try {
                Fight fight = new Fight();
                fight.setFightDate(LocalDate.of(2024, 1, 15 + i).atStartOfDay());
                fight.setFightMode(i % 2 == 0 ? FightMode.STANCE : FightMode.MMA);
                // Распределяем бои по 3 сезонам
                if (i < 8) {
                    fight.setSeason(1); // Первые 8 боев - сезон 1
                } else if (i < 17) {
                    fight.setSeason(2); // Следующие 9 боев - сезон 2
                } else {
                    fight.setSeason(3); // Остальные бои - сезон 3
                }
                fight.setResult(i % 3 == 0 ? FightResult.WIN : (i % 3 == 1 ? FightResult.LOSS : FightResult.DRAW));
                fight.setMethod(i % 4 == 0 ? FightMethod.DECISION : (i % 4 == 1 ? FightMethod.KNOCKOUT : (i % 4 == 2 ? FightMethod.SUBMISSION : FightMethod.EARLY_EXIT)));
                fight.setRoundsPlayed((i % 5) + 1);
                fight.setRatingPoints(1500 + (i * 10));
                fight.setRankingPosition(i + 1);
                fight.setWeightClass(WeightClass.values()[i % WeightClass.values().length]);
                fight.setMyFighter(fighters[i % fighters.length]);
                fight.setOpponent(opponents[i % opponents.length]);
                fight.setNotes("Тестовый бой #" + (i + 1));
                
                fightService.createFight(fight);
                System.out.println("✅ Бой #" + (i + 1) + " создан: " + fight.getMyFighter() + " vs " + fight.getOpponent());
            } catch (Exception e) {
                System.err.println("❌ Ошибка при создании боя #" + (i + 1) + ": " + e.getMessage());
            }
        }
        
        System.out.println("🎉 Создание тестовых боев завершено!");
    }
}
