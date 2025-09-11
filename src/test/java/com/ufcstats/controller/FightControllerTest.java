package com.ufcstats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.enums.*;
import com.ufcstats.service.FightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для FightController
 */
@WebMvcTest(FightController.class)
class FightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FightService fightService;

    @Autowired
    private ObjectMapper objectMapper;

    private Fight testFight;

    @BeforeEach
    void setUp() {
        testFight = createTestFight();
    }

    @Test
    void createFight_ShouldReturnCreatedFight() throws Exception {
        // Given
        when(fightService.createFight(any(Fight.class))).thenReturn(testFight);

        // When & Then
        mockMvc.perform(post("/api/fights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testFight.getId()))
                .andExpect(jsonPath("$.myFighter").value(testFight.getMyFighter()))
                .andExpect(jsonPath("$.opponent").value(testFight.getOpponent()))
                .andExpect(jsonPath("$.result").value(testFight.getResult().name()));
    }

    @Test
    void getFightById_WhenFightExists_ShouldReturnFight() throws Exception {
        // Given
        Long fightId = 1L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.of(testFight));

        // When & Then
        mockMvc.perform(get("/api/fights/{id}", fightId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testFight.getId()))
                .andExpect(jsonPath("$.myFighter").value(testFight.getMyFighter()))
                .andExpect(jsonPath("$.opponent").value(testFight.getOpponent()));
    }

    @Test
    void getFightById_WhenFightNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long fightId = 999L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/fights/{id}", fightId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllFights_ShouldReturnPageOfFights() throws Exception {
        // Given
        List<Fight> fights = List.of(testFight);
        Page<Fight> page = new PageImpl<>(fights, PageRequest.of(0, 10), 1);
        when(fightService.getAllFights(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/fights")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateFight_WhenFightExists_ShouldReturnUpdatedFight() throws Exception {
        // Given
        Long fightId = 1L;
        Fight updatedFight = createTestFight();
        updatedFight.setMyFighter("Обновленный боец");
        
        when(fightService.updateFight(fightId, updatedFight)).thenReturn(updatedFight);

        // When & Then
        mockMvc.perform(put("/api/fights/{id}", fightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myFighter").value("Обновленный боец"));
    }

    @Test
    void updateFight_WhenFightNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long fightId = 999L;
        when(fightService.updateFight(fightId, testFight)).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/api/fights/{id}", fightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFight)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFight_WhenFightExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long fightId = 1L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.of(testFight));

        // When & Then
        mockMvc.perform(delete("/api/fights/{id}", fightId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFight_WhenFightNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long fightId = 999L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/fights/{id}", fightId))
                .andExpect(status().isNotFound());
    }

    private Fight createTestFight() {
        Fight fight = new Fight();
        fight.setId(1L);
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
        fight.setNotes("Тестовый бой");

        // Добавляем раунды
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

        fight.setRounds(rounds);
        return fight;
    }
}
