package com.ufcstats.model;

import com.ufcstats.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для моделей данных
 */
class ModelTest {

    private Fight testFight;
    private FightRound testRound;
    private JudgeScore testJudgeScore;

    @BeforeEach
    void setUp() {
        testFight = createTestFight();
        testRound = createTestRound();
        testJudgeScore = createTestJudgeScore();
    }

    @Test
    void fight_ShouldCreateCorrectly() {
        assertNotNull(testFight);
        assertEquals("Тестовый боец", testFight.getMyFighter());
        assertEquals("Тестовый соперник", testFight.getOpponent());
        assertEquals(FightMode.MMA, testFight.getFightMode());
        assertEquals(FightResult.WIN, testFight.getResult());
        assertEquals(FightMethod.KNOCKOUT, testFight.getMethod());
        assertEquals(WeightClass.LIGHTWEIGHT, testFight.getWeightClass());
        assertEquals(2, testFight.getRoundsPlayed());
        assertEquals(150, testFight.getRatingPoints());
        assertEquals(5, testFight.getRankingPosition());
        assertEquals(1, testFight.getSeason());
        assertEquals("Тестовые заметки", testFight.getNotes());
    }

    @Test
    void fight_ShouldSetAndGetRounds() {
        List<FightRound> rounds = new ArrayList<>();
        rounds.add(testRound);
        
        testFight.setRounds(rounds);
        
        assertEquals(1, testFight.getRounds().size());
        assertEquals(testRound, testFight.getRounds().get(0));
    }

    @Test
    void fight_ShouldSetAndGetJudgeScores() {
        List<JudgeScore> judgeScores = new ArrayList<>();
        judgeScores.add(testJudgeScore);
        
        testFight.setJudgeScores(judgeScores);
        
        assertEquals(1, testFight.getJudgeScores().size());
        assertEquals(testJudgeScore, testFight.getJudgeScores().get(0));
    }

    @Test
    void fightRound_ShouldCreateCorrectly() {
        assertNotNull(testRound);
        assertEquals(1, testRound.getRoundNumber());
        assertEquals(15, testRound.getMyHeadDamage());
        assertEquals(8, testRound.getMyBodyDamage());
        assertEquals(3, testRound.getMyLegDamage());
        assertEquals(0, testRound.getMyKnockdowns());
        assertEquals(25, testRound.getMySignificantStrikesLanded());
        assertEquals(45, testRound.getMySignificantStrikesAttempted());
        assertEquals(35, testRound.getMyTotalStrikesLanded());
        assertEquals(60, testRound.getMyTotalStrikesAttempted());
        assertEquals(1, testRound.getMyTakedownsSuccessful());
        assertEquals(2, testRound.getMyTakedownsAttempted());
        assertEquals("02:30", testRound.getMyControlTime());
        assertEquals(20, testRound.getOpponentHeadDamage());
        assertEquals(12, testRound.getOpponentBodyDamage());
        assertEquals(5, testRound.getOpponentLegDamage());
        assertEquals(0, testRound.getOpponentKnockdowns());
        assertEquals(30, testRound.getOpponentSignificantStrikesLanded());
        assertEquals(50, testRound.getOpponentSignificantStrikesAttempted());
        assertEquals(40, testRound.getOpponentTotalStrikesLanded());
        assertEquals(65, testRound.getOpponentTotalStrikesAttempted());
        assertEquals(0, testRound.getOpponentTakedownsSuccessful());
        assertEquals(1, testRound.getOpponentTakedownsAttempted());
        assertEquals("01:45", testRound.getOpponentControlTime());
    }

    @Test
    void fightRound_ShouldSetAndGetFight() {
        testRound.setFight(testFight);
        
        assertEquals(testFight, testRound.getFight());
    }

    @Test
    void judgeScore_ShouldCreateCorrectly() {
        assertNotNull(testJudgeScore);
        assertEquals(1, testJudgeScore.getJudgeNumber());
        assertEquals(10, testJudgeScore.getRound1MyScore());
        assertEquals(9, testJudgeScore.getRound1OpponentScore());
        assertEquals(10, testJudgeScore.getRound2MyScore());
        assertEquals(9, testJudgeScore.getRound2OpponentScore());
        assertEquals(10, testJudgeScore.getRound3MyScore());
        assertEquals(9, testJudgeScore.getRound3OpponentScore());
        assertEquals(10, testJudgeScore.getRound4MyScore());
        assertEquals(9, testJudgeScore.getRound4OpponentScore());
        assertEquals(10, testJudgeScore.getRound5MyScore());
        assertEquals(9, testJudgeScore.getRound5OpponentScore());
    }

    @Test
    void judgeScore_ShouldSetAndGetFight() {
        testJudgeScore.setFight(testFight);
        
        assertEquals(testFight, testJudgeScore.getFight());
    }

    @Test
    void judgeScore_ShouldCalculateTotalScores() {
        assertEquals(50, testJudgeScore.getMyTotalScore());
        assertEquals(45, testJudgeScore.getOpponentTotalScore());
    }

    @Test
    void judgeScore_ShouldDetermineWinner() {
        assertEquals("MY_FIGHTER", testJudgeScore.getWinnerByJudge());
        
        // Изменяем оценки так, чтобы соперник выиграл
        testJudgeScore.setRound1MyScore(9);
        testJudgeScore.setRound1OpponentScore(10);
        testJudgeScore.setRound2MyScore(9);
        testJudgeScore.setRound2OpponentScore(10);
        testJudgeScore.setRound3MyScore(9);
        testJudgeScore.setRound3OpponentScore(10);
        testJudgeScore.setRound4MyScore(9);
        testJudgeScore.setRound4OpponentScore(10);
        testJudgeScore.setRound5MyScore(9);
        testJudgeScore.setRound5OpponentScore(10);
        
        assertEquals("OPPONENT", testJudgeScore.getWinnerByJudge());
    }

    @Test
    void fight_ShouldHandleNullValues() {
        Fight fight = new Fight();
        
        assertNull(fight.getMyFighter());
        assertNull(fight.getOpponent());
        assertNull(fight.getFightMode());
        assertNull(fight.getResult());
        assertNull(fight.getMethod());
        assertNull(fight.getWeightClass());
        assertNull(fight.getNotes());
        assertNull(fight.getFightDate());
        // rounds и judgeScores инициализируются пустыми списками, а не null
        assertNotNull(fight.getRounds());
        assertTrue(fight.getRounds().isEmpty());
        assertNotNull(fight.getJudgeScores());
        assertTrue(fight.getJudgeScores().isEmpty());
    }

    @Test
    void fightRound_ShouldHandleNullValues() {
        FightRound round = new FightRound();
        
        assertNull(round.getMyControlTime());
        assertNull(round.getOpponentControlTime());
        assertNull(round.getFight());
    }

    @Test
    void judgeScore_ShouldHandleNullValues() {
        JudgeScore judgeScore = new JudgeScore();
        
        assertNull(judgeScore.getFight());
    }

    @Test
    void fight_ShouldBeImmutableAfterSetting() {
        Fight fight = new Fight();
        fight.setMyFighter("Изначальный боец");
        fight.setOpponent("Изначальный соперник");
        
        assertEquals("Изначальный боец", fight.getMyFighter());
        assertEquals("Изначальный соперник", fight.getOpponent());
        
        // Изменяем значения
        fight.setMyFighter("Новый боец");
        fight.setOpponent("Новый соперник");
        
        assertEquals("Новый боец", fight.getMyFighter());
        assertEquals("Новый соперник", fight.getOpponent());
    }

    private Fight createTestFight() {
        Fight fight = new Fight();
        fight.setFightDate(LocalDateTime.now());
        fight.setFightMode(FightMode.MMA);
        fight.setSeason(1);
        fight.setResult(FightResult.WIN);
        fight.setMethod(FightMethod.KNOCKOUT);
        fight.setRoundsPlayed(2);
        fight.setRatingPoints(150);
        fight.setRankingPosition(5);
        fight.setWeightClass(WeightClass.LIGHTWEIGHT);
        fight.setMyFighter("Тестовый боец");
        fight.setOpponent("Тестовый соперник");
        fight.setNotes("Тестовые заметки");
        return fight;
    }

    private FightRound createTestRound() {
        FightRound round = new FightRound();
        round.setRoundNumber(1);
        round.setMyHeadDamage(15);
        round.setMyBodyDamage(8);
        round.setMyLegDamage(3);
        round.setMyKnockdowns(0);
        round.setMySignificantStrikesLanded(25);
        round.setMySignificantStrikesAttempted(45);
        round.setMyTotalStrikesLanded(35);
        round.setMyTotalStrikesAttempted(60);
        round.setMyTakedownsSuccessful(1);
        round.setMyTakedownsAttempted(2);
        round.setMyControlTime("02:30");
        round.setOpponentHeadDamage(20);
        round.setOpponentBodyDamage(12);
        round.setOpponentLegDamage(5);
        round.setOpponentKnockdowns(0);
        round.setOpponentSignificantStrikesLanded(30);
        round.setOpponentSignificantStrikesAttempted(50);
        round.setOpponentTotalStrikesLanded(40);
        round.setOpponentTotalStrikesAttempted(65);
        round.setOpponentTakedownsSuccessful(0);
        round.setOpponentTakedownsAttempted(1);
        round.setOpponentControlTime("01:45");
        return round;
    }

    private JudgeScore createTestJudgeScore() {
        JudgeScore judgeScore = new JudgeScore();
        judgeScore.setJudgeNumber(1);
        judgeScore.setRound1MyScore(10);
        judgeScore.setRound1OpponentScore(9);
        judgeScore.setRound2MyScore(10);
        judgeScore.setRound2OpponentScore(9);
        judgeScore.setRound3MyScore(10);
        judgeScore.setRound3OpponentScore(9);
        judgeScore.setRound4MyScore(10);
        judgeScore.setRound4OpponentScore(9);
        judgeScore.setRound5MyScore(10);
        judgeScore.setRound5OpponentScore(9);
        return judgeScore;
    }
}
