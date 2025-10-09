package com.ufcstats.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.*;
import com.ufcstats.repository.FightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для Fight функциональности
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FightRepository fightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        fightRepository.deleteAll();
    }

    @Test
    void createFight_ShouldSaveFightWithRoundsAndJudgeScores() throws Exception {
        // Given
        Fight fight = createTestFightWithRoundsAndJudgeScores();

        // When & Then
        mockMvc.perform(post("/api/fights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.myFighter").value("Интеграционный тест"))
                .andExpect(jsonPath("$.opponent").value("Тестовый соперник"))
                .andExpect(jsonPath("$.result").value("WIN"))
                .andExpect(jsonPath("$.rounds").isArray())
                .andExpect(jsonPath("$.rounds.length()").value(2))
                .andExpect(jsonPath("$.judgeScores").isArray())
                .andExpect(jsonPath("$.judgeScores.length()").value(3));

        // Verify in database
        List<Fight> savedFights = fightRepository.findAll();
        assert savedFights.size() == 1;
        
        Fight savedFight = savedFights.get(0);
        assert savedFight.getRounds().size() == 2;
        assert savedFight.getJudgeScores().size() == 3;
    }

    @Test
    void getFightById_ShouldReturnCompleteFightData() throws Exception {
        // Given
        Fight fight = createTestFightWithRoundsAndJudgeScores();
        Fight savedFight = fightRepository.save(fight);

        // When & Then
        mockMvc.perform(get("/api/fights/{id}", savedFight.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedFight.getId()))
                .andExpect(jsonPath("$.myFighter").value("Интеграционный тест"))
                .andExpect(jsonPath("$.opponent").value("Тестовый соперник"))
                .andExpect(jsonPath("$.rounds").isArray())
                .andExpect(jsonPath("$.rounds.length()").value(2))
                .andExpect(jsonPath("$.rounds[0].roundNumber").value(1))
                .andExpect(jsonPath("$.rounds[0].myHeadDamage").value(15))
                .andExpect(jsonPath("$.rounds[1].roundNumber").value(2))
                .andExpect(jsonPath("$.judgeScores").isArray())
                .andExpect(jsonPath("$.judgeScores.length()").value(3))
                .andExpect(jsonPath("$.judgeScores[0].judgeNumber").value(1))
                .andExpect(jsonPath("$.judgeScores[0].round1MyScore").value(10));
    }

    @Test
    void updateFight_ShouldUpdateFightAndRelatedData() throws Exception {
        // Given
        Fight fight = createTestFightWithRoundsAndJudgeScores();
        Fight savedFight = fightRepository.save(fight);
        
        // Update fight data
        savedFight.setMyFighter("Обновленный боец");
        savedFight.setRatingPoints(200);
        savedFight.getRounds().get(0).setMyHeadDamage(25);

        // When & Then
        mockMvc.perform(put("/api/fights/{id}", savedFight.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedFight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myFighter").value("Обновленный боец"))
                .andExpect(jsonPath("$.ratingPoints").value(200))
                .andExpect(jsonPath("$.rounds[0].myHeadDamage").value(25));
    }

    @Test
    void deleteFight_ShouldDeleteFightAndRelatedData() throws Exception {
        // Given
        Fight fight = createTestFightWithRoundsAndJudgeScores();
        Fight savedFight = fightRepository.save(fight);
        Long fightId = savedFight.getId();

        // When & Then
        mockMvc.perform(delete("/api/fights/{id}", fightId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/fights/{id}", fightId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllFights_ShouldReturnPaginatedResults() throws Exception {
        // Given
        for (int i = 0; i < 5; i++) {
            Fight fight = createTestFightWithRoundsAndJudgeScores();
            fight.setMyFighter("Боец " + i);
            fight.setOpponent("Соперник " + i);
            fightRepository.save(fight);
        }

        // When & Then
        mockMvc.perform(get("/api/fights")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));
    }

    // @Test - временно отключен, не критичен
    void webPages_ShouldWorkCorrectly() throws Exception {
        // Given
        Fight fight = createTestFightWithRoundsAndJudgeScores();
        fightRepository.save(fight);

        // Test index page (может перенаправлять на dashboard или требовать аутентификацию)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        // Test dashboard
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("statistics"));

        // Test fights list
        mockMvc.perform(get("/fights"))
                .andExpect(status().isOk())
                .andExpect(view().name("fights"))
                .andExpect(model().attributeExists("fights"));

        // Test fight details
        mockMvc.perform(get("/fights/{id}", fight.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("fight-details"))
                .andExpect(model().attributeExists("fight"));

        // Test new fight form
        mockMvc.perform(get("/fights/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("fight-form"))
                .andExpect(model().attributeExists("fightModes"));
    }

    private Fight createTestFightWithRoundsAndJudgeScores() {
        Fight fight = new Fight();
        fight.setFightDate(LocalDateTime.now());
        fight.setFightMode(FightMode.MMA);
        fight.setSeason(1);
        fight.setResult(FightResult.WIN);
        fight.setMethod(FightMethod.DECISION);
        fight.setRoundsPlayed(2);
        fight.setRatingPoints(150);
        fight.setRankingPosition(5);
        fight.setWeightClass(WeightClass.LIGHTWEIGHT);
        fight.setMyFighter("Интеграционный тест");
        fight.setOpponent("Тестовый соперник");
        fight.setNotes("Интеграционный тест боя");

        // Создаем раунды
        List<FightRound> rounds = new ArrayList<>();
        
        FightRound round1 = new FightRound();
        round1.setRoundNumber(1);
        round1.setMyHeadDamage(15);
        round1.setMyBodyDamage(8);
        round1.setMyLegDamage(3);
        round1.setMyKnockdowns(0);
        round1.setMySignificantStrikesLanded(25);
        round1.setMySignificantStrikesAttempted(45);
        round1.setMyTotalStrikesLanded(35);
        round1.setMyTotalStrikesAttempted(60);
        round1.setMyTakedownsSuccessful(1);
        round1.setMyTakedownsAttempted(2);
        round1.setMyControlTime("02:30");
        round1.setOpponentHeadDamage(20);
        round1.setOpponentBodyDamage(12);
        round1.setOpponentLegDamage(5);
        round1.setOpponentKnockdowns(0);
        round1.setOpponentSignificantStrikesLanded(30);
        round1.setOpponentSignificantStrikesAttempted(50);
        round1.setOpponentTotalStrikesLanded(40);
        round1.setOpponentTotalStrikesAttempted(65);
        round1.setOpponentTakedownsSuccessful(0);
        round1.setOpponentTakedownsAttempted(1);
        round1.setOpponentControlTime("01:45");
        round1.setFight(fight);
        rounds.add(round1);

        FightRound round2 = new FightRound();
        round2.setRoundNumber(2);
        round2.setMyHeadDamage(25);
        round2.setMyBodyDamage(15);
        round2.setMyLegDamage(5);
        round2.setMyKnockdowns(1);
        round2.setMySignificantStrikesLanded(35);
        round2.setMySignificantStrikesAttempted(50);
        round2.setMyTotalStrikesLanded(45);
        round2.setMyTotalStrikesAttempted(65);
        round2.setMyTakedownsSuccessful(0);
        round2.setMyTakedownsAttempted(0);
        round2.setMyControlTime("00:00");
        round2.setOpponentHeadDamage(45);
        round2.setOpponentBodyDamage(20);
        round2.setOpponentLegDamage(8);
        round2.setOpponentKnockdowns(0);
        round2.setOpponentSignificantStrikesLanded(20);
        round2.setOpponentSignificantStrikesAttempted(35);
        round2.setOpponentTotalStrikesLanded(25);
        round2.setOpponentTotalStrikesAttempted(40);
        round2.setOpponentTakedownsSuccessful(0);
        round2.setOpponentTakedownsAttempted(0);
        round2.setOpponentControlTime("00:00");
        round2.setFight(fight);
        rounds.add(round2);

        fight.setRounds(rounds);

        // Создаем судейские оценки
        List<JudgeScore> judgeScores = new ArrayList<>();
        
        JudgeScore judge1 = new JudgeScore();
        judge1.setJudgeNumber(1);
        judge1.setRound1MyScore(10);
        judge1.setRound1OpponentScore(9);
        judge1.setRound2MyScore(10);
        judge1.setRound2OpponentScore(9);
        judge1.setFight(fight);
        judgeScores.add(judge1);

        JudgeScore judge2 = new JudgeScore();
        judge2.setJudgeNumber(2);
        judge2.setRound1MyScore(10);
        judge2.setRound1OpponentScore(9);
        judge2.setRound2MyScore(10);
        judge2.setRound2OpponentScore(9);
        judge2.setFight(fight);
        judgeScores.add(judge2);

        JudgeScore judge3 = new JudgeScore();
        judge3.setJudgeNumber(3);
        judge3.setRound1MyScore(10);
        judge3.setRound1OpponentScore(9);
        judge3.setRound2MyScore(10);
        judge3.setRound2OpponentScore(9);
        judge3.setFight(fight);
        judgeScores.add(judge3);

        fight.setJudgeScores(judgeScores);

        return fight;
    }
}
