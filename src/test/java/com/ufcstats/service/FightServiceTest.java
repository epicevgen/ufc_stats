package com.ufcstats.service;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.*;
import com.ufcstats.repository.FightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit тесты для FightService
 */
@ExtendWith(MockitoExtension.class)
class FightServiceTest {

    @Mock
    private FightRepository fightRepository;

    @InjectMocks
    private FightService fightService;

    private Fight testFight;
    private List<Fight> testFights;

    @BeforeEach
    void setUp() {
        testFight = createTestFight();
        testFights = List.of(testFight);
    }

    @Test
    void createFight_ShouldSaveAndReturnFight() {
        // Given
        when(fightRepository.save(any(Fight.class))).thenReturn(testFight);

        // When
        Fight result = fightService.createFight(testFight);

        // Then
        assertNotNull(result);
        assertEquals(testFight.getMyFighter(), result.getMyFighter());
        assertEquals(testFight.getOpponent(), result.getOpponent());
        verify(fightRepository, times(1)).save(testFight);
    }

    @Test
    void getFightById_WhenFightExists_ShouldReturnFight() {
        // Given
        Long fightId = 1L;
        when(fightRepository.findById(fightId)).thenReturn(Optional.of(testFight));

        // When
        Optional<Fight> result = fightService.getFightById(fightId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testFight, result.get());
        verify(fightRepository, times(1)).findById(fightId);
    }

    @Test
    void getFightById_WhenFightNotExists_ShouldReturnEmpty() {
        // Given
        Long fightId = 999L;
        when(fightRepository.findById(fightId)).thenReturn(Optional.empty());

        // When
        Optional<Fight> result = fightService.getFightById(fightId);

        // Then
        assertFalse(result.isPresent());
        verify(fightRepository, times(1)).findById(fightId);
    }

    @Test
    void getAllFights_ShouldReturnPageOfFights() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Fight> expectedPage = new PageImpl<>(testFights, pageable, 1);
        when(fightRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Fight> result = fightService.getAllFights(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testFights, result.getContent());
        verify(fightRepository, times(1)).findAll(pageable);
    }

    @Test
    void updateFight_WhenFightExists_ShouldUpdateAndReturnFight() {
        // Given
        Long fightId = 1L;
        Fight updatedFight = createTestFight();
        updatedFight.setMyFighter("Обновленный боец");
        
        when(fightRepository.existsById(fightId)).thenReturn(true);
        when(fightRepository.save(any(Fight.class))).thenReturn(updatedFight);

        // When
        Fight result = fightService.updateFight(fightId, updatedFight);

        // Then
        assertNotNull(result);
        assertEquals("Обновленный боец", result.getMyFighter());
        verify(fightRepository, times(1)).existsById(fightId);
        verify(fightRepository, times(1)).save(updatedFight);
    }

    @Test
    void updateFight_WhenFightNotExists_ShouldReturnNull() {
        // Given
        Long fightId = 999L;
        when(fightRepository.existsById(fightId)).thenReturn(false);

        // When
        Fight result = fightService.updateFight(fightId, testFight);

        // Then
        assertNull(result);
        verify(fightRepository, times(1)).existsById(fightId);
        verify(fightRepository, never()).save(any(Fight.class));
    }

    @Test
    void deleteFight_WhenFightExists_ShouldDeleteFight() {
        // Given
        Long fightId = 1L;
        when(fightRepository.existsById(fightId)).thenReturn(true);

        // When
        fightService.deleteFight(fightId);

        // Then
        verify(fightRepository, times(1)).existsById(fightId);
        verify(fightRepository, times(1)).deleteById(fightId);
    }

    @Test
    void deleteFight_WhenFightNotExists_ShouldNotDelete() {
        // Given
        Long fightId = 999L;
        when(fightRepository.existsById(fightId)).thenReturn(false);

        // When
        fightService.deleteFight(fightId);

        // Then
        verify(fightRepository, times(1)).existsById(fightId);
        verify(fightRepository, never()).deleteById(anyLong());
    }

    @Test
    void getFightStatistics_ShouldCalculateCorrectStatistics() {
        // Given
        List<Fight> fights = createMultipleTestFights();
        when(fightRepository.findAll()).thenReturn(fights);

        // When
        FightService.FightStatistics statistics = fightService.getFightStatistics();

        // Then
        assertNotNull(statistics);
        assertEquals(3, statistics.getTotalFights());
        assertEquals(2, statistics.getWins());
        assertEquals(1, statistics.getLosses());
        assertEquals(0, statistics.getDraws());
        assertEquals(66.67, statistics.getWinRate(), 0.01);
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

    private List<Fight> createMultipleTestFights() {
        List<Fight> fights = new ArrayList<>();
        
        // Бой 1: Победа
        Fight fight1 = createTestFight();
        fight1.setId(1L);
        fight1.setResult(FightResult.WIN);
        fights.add(fight1);
        
        // Бой 2: Победа
        Fight fight2 = createTestFight();
        fight2.setId(2L);
        fight2.setResult(FightResult.WIN);
        fights.add(fight2);
        
        // Бой 3: Поражение
        Fight fight3 = createTestFight();
        fight3.setId(3L);
        fight3.setResult(FightResult.LOSS);
        fights.add(fight3);
        
        return fights;
    }
}
