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
 * Тест для проверки сохранения и загрузки данных боя
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class DataPersistenceTest {

    @Autowired
    private FightService fightService;

    @Test
    @Transactional
    void testFightDataPersistence() {
        log.info("=== Тестируем сохранение и загрузку данных боя ===");
        
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
        
        // Добавляем судейские оценки (по одному объекту на судью)
        for (int judge = 1; judge <= 3; judge++) {
            JudgeScore score = new JudgeScore();
            score.setJudgeNumber(judge);
            score.setFight(fight);
            
            // Устанавливаем оценки для всех раундов
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
        
        // Загружаем бой обратно
        Optional<Fight> loadedFightOpt = fightService.getFightById(savedFight.getId());
        assertTrue(loadedFightOpt.isPresent(), "Бой должен быть найден");
        
        Fight loadedFight = loadedFightOpt.get();
        
        // Проверяем основные поля
        assertEquals("Иван Петров", loadedFight.getMyFighter(), "Имя бойца должно совпадать");
        assertEquals("Алексей Сидоров", loadedFight.getOpponent(), "Имя соперника должно совпадать");
        assertEquals(2024, loadedFight.getSeason(), "Сезон должен совпадать");
        assertEquals(5, loadedFight.getRoundsPlayed(), "Количество раундов должно совпадать");
        
        // Проверяем раунды
        assertNotNull(loadedFight.getRounds(), "Раунды должны быть загружены");
        assertEquals(5, loadedFight.getRounds().size(), "Должно быть 5 раундов");
        
        FightRound firstRound = loadedFight.getRounds().get(0);
        assertEquals(1, firstRound.getRoundNumber(), "Номер первого раунда должен быть 1");
        assertEquals(10, firstRound.getMyHeadDamage(), "Повреждения головы должны совпадать");
        assertEquals(5, firstRound.getMyBodyDamage(), "Повреждения корпуса должны совпадать");
        assertEquals(3, firstRound.getMyLegDamage(), "Повреждения ног должны совпадать");
        assertEquals(1, firstRound.getMyKnockdowns(), "Нокдауны должны совпадать");
        assertEquals(25, firstRound.getMySignificantStrikesLanded(), "Значимые удары (попал) должны совпадать");
        assertEquals(40, firstRound.getMySignificantStrikesAttempted(), "Значимые удары (всего) должны совпадать");
        assertEquals(35, firstRound.getMyTotalStrikesLanded(), "Всего ударов (попал) должны совпадать");
        assertEquals(50, firstRound.getMyTotalStrikesAttempted(), "Всего ударов (всего) должны совпадать");
        assertEquals(2, firstRound.getMyTakedownsSuccessful(), "Тейкдауны (успех) должны совпадать");
        assertEquals(3, firstRound.getMyTakedownsAttempted(), "Тейкдауны (всего) должны совпадать");
        assertEquals("02:00", firstRound.getMyControlTime(), "Время контроля должно совпадать");
        
        // Проверяем судейские оценки
        assertNotNull(loadedFight.getJudgeScores(), "Судейские оценки должны быть загружены");
        assertEquals(3, loadedFight.getJudgeScores().size(), "Должно быть 3 судейских оценки (по одной на судью)");
        
        JudgeScore firstJudgeScore = loadedFight.getJudgeScores().get(0);
        assertEquals(1, firstJudgeScore.getJudgeNumber(), "Номер первого судьи должен быть 1");
        assertEquals(10, firstJudgeScore.getMyScoreForRound(1), "Оценка бойца за первый раунд должна совпадать");
        assertEquals(9, firstJudgeScore.getOpponentScoreForRound(1), "Оценка соперника за первый раунд должна совпадать");
        assertEquals(10, firstJudgeScore.getMyScoreForRound(2), "Оценка бойца за второй раунд должна совпадать");
        assertEquals(9, firstJudgeScore.getOpponentScoreForRound(2), "Оценка соперника за второй раунд должна совпадать");
        
        log.info("✅ Все данные успешно сохранены и загружены!");
    }
}
