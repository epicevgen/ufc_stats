package com.ufcstats.service;

import com.ufcstats.dto.StrikeMovementDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.FightRound;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        return getStrikeMovementData(fightModeFilter, seasonFilter, null, null);
    }
    
    public StrikeMovementDto getStrikeMovementData(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        log.debug("Получение данных о движении ударов с фильтрами: fightMode={}, season={}, startDate={}, endDate={}", 
                  fightModeFilter, seasonFilter, startDate, endDate);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter, startDate, endDate);
        
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
        return getFilteredFights(fightModeFilter, seasonFilter, null, null);
    }
    
    private List<Fight> getFilteredFights(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        List<Fight> allFights = fightRepository.findAll().stream()
                .sorted((f1, f2) -> f1.getFightDate().compareTo(f2.getFightDate()))
                .collect(Collectors.toList());
        
        // Конвертируем строку в enum
        final FightMode fightMode = parseFightMode(fightModeFilter);
        
        // Парсим даты
        final LocalDateTime startDateTime = (startDate != null && !startDate.isEmpty()) ? 
            parseDateTime(startDate + "T00:00:00", "начальной") : null;
        final LocalDateTime endDateTime = (endDate != null && !endDate.isEmpty()) ? 
            parseDateTime(endDate + "T23:59:59", "конечной") : null;
        
        return allFights.stream()
                .filter(fight -> fightMode == null || fight.getFightMode() == fightMode)
                .filter(fight -> seasonFilter == null || fight.getSeason().equals(seasonFilter))
                .filter(fight -> startDateTime == null || fight.getFightDate().isAfter(startDateTime) || fight.getFightDate().isEqual(startDateTime))
                .filter(fight -> endDateTime == null || fight.getFightDate().isBefore(endDateTime) || fight.getFightDate().isEqual(endDateTime))
                .collect(Collectors.toList());
    }
    
    /**
     * Парсинг строки в enum FightMode
     */
    private FightMode parseFightMode(String fightModeFilter) {
        if (fightModeFilter == null || fightModeFilter.isEmpty()) {
            return null;
        }
        try {
            return FightMode.valueOf(fightModeFilter);
        } catch (IllegalArgumentException e) {
            log.warn("Неверный режим боя: {}", fightModeFilter);
            return null;
        }
    }
    
    /**
     * Парсинг даты с обработкой ошибок
     */
    private LocalDateTime parseDateTime(String dateTimeString, String dateType) {
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (Exception e) {
            log.warn("Неверный формат {} даты: {}", dateType, dateTimeString);
            return null;
        }
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
