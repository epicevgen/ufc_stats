package com.ufcstats.service;

import com.ufcstats.dto.StrikeMovementDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StrikeMovementService {

    private final FightRepository fightRepository;

    public StrikeMovementDto getStrikeMovementData() {
        return getStrikeMovementData(null, null);
    }
    
    public StrikeMovementDto getStrikeMovementData(String fightModeFilter, Integer seasonFilter) {
        log.debug("Получение данных о движении ударов с фильтрами: fightMode={}, season={}", fightModeFilter, seasonFilter);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter);
        
        List<StrikeMovementDto.StrikeDataPoint> totalStrikesHistory = new ArrayList<>();
        List<StrikeMovementDto.StrikeDataPoint> significantStrikesHistory = new ArrayList<>();
        
        for (int i = 0; i < filteredFights.size(); i++) {
            Fight fight = filteredFights.get(i);
            StrikeMovementDto.StrikeDataPoint dataPoint = calculateStrikeDataPoint(fight, i + 1);
            
            totalStrikesHistory.add(dataPoint);
            significantStrikesHistory.add(dataPoint);
        }
        
        return StrikeMovementDto.builder()
                .totalStrikesHistory(totalStrikesHistory)
                .significantStrikesHistory(significantStrikesHistory)
                .build();
    }
    
    /**
     * Получить отфильтрованные бои
     */
    private List<Fight> getFilteredFights(String fightModeFilter, Integer seasonFilter) {
        List<Fight> allFights = fightRepository.findAll().stream()
                .sorted((f1, f2) -> f1.getFightDate().compareTo(f2.getFightDate()))
                .collect(Collectors.toList());
        
        return allFights.stream()
                .filter(fight -> fightModeFilter == null || fight.getFightMode().name().equals(fightModeFilter))
                .filter(fight -> seasonFilter == null || fight.getSeason().equals(seasonFilter))
                .collect(Collectors.toList());
    }
    
    private StrikeMovementDto.StrikeDataPoint calculateStrikeDataPoint(Fight fight, int fightNumber) {
        // Суммируем удары по всем раундам боя
        int totalStrikesLanded = fight.getRounds().stream()
                .mapToInt(FightRound::getMyTotalStrikesLanded)
                .sum();
        
        int totalStrikesAttempted = fight.getRounds().stream()
                .mapToInt(FightRound::getMyTotalStrikesAttempted)
                .sum();
        
        int significantStrikesLanded = fight.getRounds().stream()
                .mapToInt(FightRound::getMySignificantStrikesLanded)
                .sum();
        
        int significantStrikesAttempted = fight.getRounds().stream()
                .mapToInt(FightRound::getMySignificantStrikesAttempted)
                .sum();
        
        // Рассчитываем точность
        double totalStrikesAccuracy = totalStrikesAttempted > 0 ? 
                (double) totalStrikesLanded / totalStrikesAttempted * 100 : 0.0;
        
        double significantStrikesAccuracy = significantStrikesAttempted > 0 ? 
                (double) significantStrikesLanded / significantStrikesAttempted * 100 : 0.0;
        
        return StrikeMovementDto.StrikeDataPoint.builder()
                .fightNumber(fightNumber)
                .fightDate(fight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .opponent(fight.getOpponent())
                .result(fight.getResult().getDisplayName())
                .totalStrikesLanded(totalStrikesLanded)
                .totalStrikesAttempted(totalStrikesAttempted)
                .significantStrikesLanded(significantStrikesLanded)
                .significantStrikesAttempted(significantStrikesAttempted)
                .totalStrikesAccuracy(totalStrikesAccuracy)
                .significantStrikesAccuracy(significantStrikesAccuracy)
                .build();
    }
}
