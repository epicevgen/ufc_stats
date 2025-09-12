package com.ufcstats.config;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.WeightClass;
import com.ufcstats.service.FightService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Инициализация тестовых данных
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FightService fightService;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (fightService.getFightStatistics().getTotalFights() > 0) {
            return; // Данные уже есть
        }

        createSampleFights();
    }

    private void createSampleFights() {
        // Бой 1: Победа нокаутом
        Fight fight1 = new Fight();
        fight1.setFightDate(LocalDateTime.of(2024, 1, 10, 20, 0));
        fight1.setFightMode(FightMode.MMA);
        fight1.setSeason(1);
        fight1.setResult(FightResult.WIN);
        fight1.setMethod(FightMethod.KNOCKOUT);
        fight1.setRoundsPlayed(2);
        fight1.setRatingPoints(150);
        fight1.setRankingPosition(5);
        fight1.setWeightClass(WeightClass.LIGHTWEIGHT);
        fight1.setMyFighter("Иван Петров");
        fight1.setOpponent("Алексей Сидоров");
        fight1.setNotes("Отличный бой! Нокаут во втором раунде после серии ударов.");

        // Раунды для боя 1
        List<FightRound> rounds1 = new ArrayList<>();
        
        FightRound round1_1 = new FightRound();
        round1_1.setRoundNumber(1);
        round1_1.setMyHeadDamage(15);
        round1_1.setMyBodyDamage(8);
        round1_1.setMyLegDamage(3);
        round1_1.setMyKnockdowns(0);
        round1_1.setMySignificantStrikesLanded(25);
        round1_1.setMySignificantStrikesAttempted(45);
        round1_1.setMyTotalStrikesLanded(35);
        round1_1.setMyTotalStrikesAttempted(60);
        round1_1.setMyTakedownsSuccessful(1);
        round1_1.setMyTakedownsAttempted(2);
        round1_1.setMyControlTime("02:30");
        round1_1.setOpponentHeadDamage(20);
        round1_1.setOpponentBodyDamage(12);
        round1_1.setOpponentLegDamage(5);
        round1_1.setOpponentKnockdowns(0);
        round1_1.setOpponentSignificantStrikesLanded(30);
        round1_1.setOpponentSignificantStrikesAttempted(50);
        round1_1.setOpponentTotalStrikesLanded(40);
        round1_1.setOpponentTotalStrikesAttempted(65);
        round1_1.setOpponentTakedownsSuccessful(0);
        round1_1.setOpponentTakedownsAttempted(1);
        round1_1.setOpponentControlTime("01:45");
        rounds1.add(round1_1);

        FightRound round1_2 = new FightRound();
        round1_2.setRoundNumber(2);
        round1_2.setMyHeadDamage(25);
        round1_2.setMyBodyDamage(15);
        round1_2.setMyLegDamage(5);
        round1_2.setMyKnockdowns(1);
        round1_2.setMySignificantStrikesLanded(35);
        round1_2.setMySignificantStrikesAttempted(50);
        round1_2.setMyTotalStrikesLanded(45);
        round1_2.setMyTotalStrikesAttempted(65);
        round1_2.setMyTakedownsSuccessful(0);
        round1_2.setMyTakedownsAttempted(0);
        round1_2.setMyControlTime("00:00");
        round1_2.setOpponentHeadDamage(45);
        round1_2.setOpponentBodyDamage(20);
        round1_2.setOpponentLegDamage(8);
        round1_2.setOpponentKnockdowns(0);
        round1_2.setOpponentSignificantStrikesLanded(20);
        round1_2.setOpponentSignificantStrikesAttempted(35);
        round1_2.setOpponentTotalStrikesLanded(25);
        round1_2.setOpponentTotalStrikesAttempted(40);
        round1_2.setOpponentTakedownsSuccessful(0);
        round1_2.setOpponentTakedownsAttempted(0);
        round1_2.setOpponentControlTime("00:00");
        rounds1.add(round1_2);

        // Устанавливаем связь между боем и раундами
        for (FightRound round : rounds1) {
            round.setFight(fight1);
        }
        fight1.setRounds(rounds1);
        fightService.createFight(fight1);

        // Бой 2: Поражение решением судей
        Fight fight2 = new Fight();
        fight2.setFightDate(LocalDateTime.of(2024, 1, 12, 20, 0));
        fight2.setFightMode(FightMode.STANCE);
        fight2.setSeason(1);
        fight2.setResult(FightResult.LOSS);
        fight2.setMethod(FightMethod.DECISION);
        fight2.setRoundsPlayed(3);
        fight2.setRatingPoints(120);
        fight2.setRankingPosition(8);
        fight2.setWeightClass(WeightClass.MIDDLEWEIGHT);
        fight2.setMyFighter("Иван Петров");
        fight2.setOpponent("Михаил Козлов");
        fight2.setNotes("Близкий бой, судьи отдали победу сопернику.");

        // Раунды для боя 2
        List<FightRound> rounds2 = new ArrayList<>();
        
        for (int i = 1; i <= 3; i++) {
            FightRound round = new FightRound();
            round.setRoundNumber(i);
            round.setMyHeadDamage(10 + i * 2);
            round.setMyBodyDamage(8 + i);
            round.setMyLegDamage(3);
            round.setMyKnockdowns(0);
            round.setMySignificantStrikesLanded(20 + i * 3);
            round.setMySignificantStrikesAttempted(35 + i * 5);
            round.setMyTotalStrikesLanded(30 + i * 4);
            round.setMyTotalStrikesAttempted(50 + i * 6);
            round.setMyTakedownsSuccessful(0);
            round.setMyTakedownsAttempted(0);
            round.setMyControlTime("00:00");
            round.setOpponentHeadDamage(12 + i * 3);
            round.setOpponentBodyDamage(10 + i * 2);
            round.setOpponentLegDamage(4);
            round.setOpponentKnockdowns(0);
            round.setOpponentSignificantStrikesLanded(22 + i * 4);
            round.setOpponentSignificantStrikesAttempted(38 + i * 6);
            round.setOpponentTotalStrikesLanded(32 + i * 5);
            round.setOpponentTotalStrikesAttempted(52 + i * 7);
            round.setOpponentTakedownsSuccessful(0);
            round.setOpponentTakedownsAttempted(0);
            round.setOpponentControlTime("00:00");
            rounds2.add(round);
        }

        // Устанавливаем связь между боем и раундами
        for (FightRound round : rounds2) {
            round.setFight(fight2);
        }
        fight2.setRounds(rounds2);

        // Судейские оценки для боя 2
        List<JudgeScore> judgeScores2 = new ArrayList<>();
        
        // Судья 1: 28-29 (соперник)
        JudgeScore judge1 = new JudgeScore();
        judge1.setJudgeNumber(1);
        judge1.setRound1MyScore(9);
        judge1.setRound1OpponentScore(10);
        judge1.setRound2MyScore(9);
        judge1.setRound2OpponentScore(10);
        judge1.setRound3MyScore(10);
        judge1.setRound3OpponentScore(9);
        judgeScores2.add(judge1);

        // Судья 2: 29-28 (мой боец)
        JudgeScore judge2 = new JudgeScore();
        judge2.setJudgeNumber(2);
        judge2.setRound1MyScore(10);
        judge2.setRound1OpponentScore(9);
        judge2.setRound2MyScore(9);
        judge2.setRound2OpponentScore(10);
        judge2.setRound3MyScore(10);
        judge2.setRound3OpponentScore(9);
        judgeScores2.add(judge2);

        // Судья 3: 28-29 (соперник)
        JudgeScore judge3 = new JudgeScore();
        judge3.setJudgeNumber(3);
        judge3.setRound1MyScore(9);
        judge3.setRound1OpponentScore(10);
        judge3.setRound2MyScore(9);
        judge3.setRound2OpponentScore(10);
        judge3.setRound3MyScore(10);
        judge3.setRound3OpponentScore(9);
        judgeScores2.add(judge3);

        // Устанавливаем связь между боем и судейскими оценками
        for (JudgeScore judge : judgeScores2) {
            judge.setFight(fight2);
        }
        fight2.setJudgeScores(judgeScores2);
        fightService.createFight(fight2);

        // Бой 3: Победа сабмишеном
        Fight fight3 = new Fight();
        fight3.setFightDate(LocalDateTime.of(2024, 1, 14, 20, 0));
        fight3.setFightMode(FightMode.MMA);
        fight3.setSeason(1);
        fight3.setResult(FightResult.WIN);
        fight3.setMethod(FightMethod.SUBMISSION);
        fight3.setRoundsPlayed(1);
        fight3.setRatingPoints(180);
        fight3.setRankingPosition(3);
        fight3.setWeightClass(WeightClass.WELTERWEIGHT);
        fight3.setMyFighter("Иван Петров");
        fight3.setOpponent("Дмитрий Волков");
        fight3.setNotes("Быстрая победа удушающим приемом в первом раунде!");

        // Раунд для боя 3
        List<FightRound> rounds3 = new ArrayList<>();
        
        FightRound round3_1 = new FightRound();
        round3_1.setRoundNumber(1);
        round3_1.setMyHeadDamage(5);
        round3_1.setMyBodyDamage(3);
        round3_1.setMyLegDamage(2);
        round3_1.setMyKnockdowns(0);
        round3_1.setMySignificantStrikesLanded(8);
        round3_1.setMySignificantStrikesAttempted(15);
        round3_1.setMyTotalStrikesLanded(12);
        round3_1.setMyTotalStrikesAttempted(20);
        round3_1.setMyTakedownsSuccessful(2);
        round3_1.setMyTakedownsAttempted(3);
        round3_1.setMyControlTime("03:45");
        round3_1.setOpponentHeadDamage(8);
        round3_1.setOpponentBodyDamage(5);
        round3_1.setOpponentLegDamage(3);
        round3_1.setOpponentKnockdowns(0);
        round3_1.setOpponentSignificantStrikesLanded(6);
        round3_1.setOpponentSignificantStrikesAttempted(12);
        round3_1.setOpponentTotalStrikesLanded(10);
        round3_1.setOpponentTotalStrikesAttempted(18);
        round3_1.setOpponentTakedownsSuccessful(0);
        round3_1.setOpponentTakedownsAttempted(1);
        round3_1.setOpponentControlTime("00:15");
        rounds3.add(round3_1);

        // Устанавливаем связь между боем и раундами
        for (FightRound round : rounds3) {
            round.setFight(fight3);
        }
        fight3.setRounds(rounds3);
        fightService.createFight(fight3);
    }
}
