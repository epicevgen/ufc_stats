package com.ufcstats.ui;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.*;
import com.ufcstats.service.FightService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для проверки загрузки данных в форму редактирования
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class EditFormDataTest {

    @Autowired
    private FightService fightService;

    @Test
    @Transactional
    void testEditFormDataLoading() {
        log.info("=== Тестируем загрузку данных в форму редактирования ===");
        
        // Создаем бой с данными
        Fight fight = new Fight();
        fight.setMyFighter("Иван Петров");
        fight.setOpponent("Алексей Сидоров");
        fight.setFightDate(LocalDateTime.of(2024, 1, 15, 20, 0));
        fight.setSeason(2024);
        fight.setRoundsPlayed(5);
        fight.setFightMode(FightMode.MMA);
        fight.setResult(FightResult.WIN);
        fight.setMethod(FightMethod.DECISION);
        fight.setWeightClass(WeightClass.LIGHTWEIGHT);
        
        // Добавляем раунды
        for (int i = 1; i <= 5; i++) {
            FightRound round = new FightRound();
            round.setRoundNumber(i);
            round.setMyHeadDamage(10);
            round.setMyBodyDamage(5);
            round.setMyLegDamage(3);
            round.setMyKnockdowns(1);
            round.setMySignificantStrikesLanded(25);
            round.setMySignificantStrikesAttempted(40);
            round.setMyTotalStrikesLanded(35);
            round.setMyTotalStrikesAttempted(50);
            round.setMyTakedownsSuccessful(2);
            round.setMyTakedownsAttempted(3);
            round.setMyControlTime("02:00");
            
            round.setOpponentHeadDamage(8);
            round.setOpponentBodyDamage(4);
            round.setOpponentLegDamage(2);
            round.setOpponentKnockdowns(0);
            round.setOpponentSignificantStrikesLanded(20);
            round.setOpponentSignificantStrikesAttempted(35);
            round.setOpponentTotalStrikesLanded(28);
            round.setOpponentTotalStrikesAttempted(45);
            round.setOpponentTakedownsSuccessful(1);
            round.setOpponentTakedownsAttempted(2);
            round.setOpponentControlTime("01:30");
            
            round.setFight(fight);
            fight.getRounds().add(round);
        }
        
        // Добавляем судейские оценки
        for (int judge = 1; judge <= 3; judge++) {
            JudgeScore score = new JudgeScore();
            score.setJudgeNumber(judge);
            score.setFight(fight);
            
            for (int round = 1; round <= 5; round++) {
                score.setMyScoreForRound(round, 10);
                score.setOpponentScoreForRound(round, 9);
            }
            
            fight.getJudgeScores().add(score);
        }
        
        // Сохраняем бой
        Fight savedFight = fightService.createFight(fight);
        assertNotNull(savedFight.getId(), "Бой должен быть сохранен с ID");
        log.info("Бой сохранен с ID: {}", savedFight.getId());
        
        // Загружаем бой для редактирования
        Optional<Fight> loadedFightOpt = fightService.getFightById(savedFight.getId());
        assertTrue(loadedFightOpt.isPresent(), "Бой должен быть найден");
        
        Fight loadedFight = loadedFightOpt.get();
        
        // Проверяем, что данные загружены
        assertNotNull(loadedFight.getRounds(), "Раунды должны быть загружены");
        assertEquals(5, loadedFight.getRounds().size(), "Должно быть 5 раундов");
        
        assertNotNull(loadedFight.getJudgeScores(), "Судейские оценки должны быть загружены");
        assertEquals(3, loadedFight.getJudgeScores().size(), "Должно быть 3 судейских оценки");
        
        // Проверяем первый раунд
        FightRound firstRound = loadedFight.getRounds().get(0);
        assertEquals(1, firstRound.getRoundNumber(), "Номер первого раунда должен быть 1");
        assertEquals(10, firstRound.getMyHeadDamage(), "Повреждения головы должны быть 10");
        assertEquals(5, firstRound.getMyBodyDamage(), "Повреждения корпуса должны быть 5");
        assertEquals(3, firstRound.getMyLegDamage(), "Повреждения ног должны быть 3");
        assertEquals(1, firstRound.getMyKnockdowns(), "Нокдауны должны быть 1");
        assertEquals(25, firstRound.getMySignificantStrikesLanded(), "Значимые удары (попал) должны быть 25");
        assertEquals(40, firstRound.getMySignificantStrikesAttempted(), "Значимые удары (всего) должны быть 40");
        assertEquals(35, firstRound.getMyTotalStrikesLanded(), "Всего ударов (попал) должны быть 35");
        assertEquals(50, firstRound.getMyTotalStrikesAttempted(), "Всего ударов (всего) должны быть 50");
        assertEquals(2, firstRound.getMyTakedownsSuccessful(), "Тейкдауны (успех) должны быть 2");
        assertEquals(3, firstRound.getMyTakedownsAttempted(), "Тейкдауны (всего) должны быть 3");
        assertEquals("02:00", firstRound.getMyControlTime(), "Время контроля должно быть 02:00");
        
        // Проверяем судейские оценки
        JudgeScore firstJudgeScore = loadedFight.getJudgeScores().get(0);
        assertEquals(1, firstJudgeScore.getJudgeNumber(), "Номер первого судьи должен быть 1");
        assertEquals(10, firstJudgeScore.getMyScoreForRound(1), "Оценка бойца за первый раунд должна быть 10");
        assertEquals(9, firstJudgeScore.getOpponentScoreForRound(1), "Оценка соперника за первый раунд должна быть 9");
        
        log.info("✅ Данные для формы редактирования загружены корректно!");
        log.info("Бой: {} vs {}", loadedFight.getMyFighter(), loadedFight.getOpponent());
        log.info("Раундов: {}", loadedFight.getRounds().size());
        log.info("Судейских оценок: {}", loadedFight.getJudgeScores().size());
        log.info("Первый раунд - повреждения головы: {}", firstRound.getMyHeadDamage());
        log.info("Первый раунд - значимые удары (попал): {}", firstRound.getMySignificantStrikesLanded());
        log.info("Первый судья - оценка за первый раунд: {}", firstJudgeScore.getMyScoreForRound(1));
    }
}
