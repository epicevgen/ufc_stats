package com.ufcstats.controller;

import com.ufcstats.model.Fight;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для WebController
 */
@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FightService fightService;

    private Fight testFight;

    @BeforeEach
    void setUp() {
        testFight = createTestFight();
    }

    @Test
    void index_ShouldReturnIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("title"));
    }

    @Test
    void dashboard_ShouldReturnDashboardWithStatistics() throws Exception {
        // Given
        FightService.FightStatistics statistics = new FightService.FightStatistics(5, 3, 2, 0, 60.0);
        
        when(fightService.getFightStatistics()).thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("statistics"))
                .andExpect(model().attribute("statistics", statistics));
    }

    @Test
    void fights_ShouldReturnFightsPageWithPagination() throws Exception {
        // Given
        List<Fight> fights = List.of(testFight);
        Page<Fight> page = new PageImpl<>(fights, PageRequest.of(0, 10), 1);
        when(fightService.getAllFights(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/fights"))
                .andExpect(status().isOk())
                .andExpect(view().name("fights"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("fights"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void newFight_ShouldReturnFightFormWithEnums() throws Exception {
        mockMvc.perform(get("/fights/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("fight-form"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("fight"))
                .andExpect(model().attributeExists("fightModes"))
                .andExpect(model().attributeExists("fightResults"))
                .andExpect(model().attributeExists("fightMethods"))
                .andExpect(model().attributeExists("weightClasses"));
    }

    @Test
    void viewFight_WhenFightExists_ShouldReturnFightDetails() throws Exception {
        // Given
        Long fightId = 1L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.of(testFight));

        // When & Then
        mockMvc.perform(get("/fights/{id}", fightId))
                .andExpect(status().isOk())
                .andExpect(view().name("fight-details"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("fight"))
                .andExpect(model().attribute("fight", testFight));
    }

    @Test
    void viewFight_WhenFightNotExists_ShouldReturnError() throws Exception {
        // Given
        Long fightId = 999L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/fights/{id}", fightId))
                .andExpect(status().isNotFound());
    }

    @Test
    void editFight_WhenFightExists_ShouldReturnFightForm() throws Exception {
        // Given
        Long fightId = 1L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.of(testFight));

        // When & Then
        mockMvc.perform(get("/fights/{id}/edit", fightId))
                .andExpect(status().isOk())
                .andExpect(view().name("fight-form"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("fight"))
                .andExpect(model().attributeExists("fightModes"))
                .andExpect(model().attributeExists("fightResults"))
                .andExpect(model().attributeExists("fightMethods"))
                .andExpect(model().attributeExists("weightClasses"));
    }

    @Test
    void editFight_WhenFightNotExists_ShouldReturnError() throws Exception {
        // Given
        Long fightId = 999L;
        when(fightService.getFightById(fightId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/fights/{id}/edit", fightId))
                .andExpect(status().isNotFound());
    }

    @Test
    void statistics_ShouldReturnStatisticsPage() throws Exception {
        // Given
        FightService.FightStatistics statistics = new FightService.FightStatistics(10, 7, 3, 0, 70.0);
        
        when(fightService.getFightStatistics()).thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("statistics"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("statistics"))
                .andExpect(model().attribute("statistics", statistics));
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
        return fight;
    }
}
