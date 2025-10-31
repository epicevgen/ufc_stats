package com.ufcstats.service;

import com.ufcstats.dto.FighterStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для расчета статистики бойцов
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FighterStatisticsService {
    
    private final FightRepository fightRepository;
    
    /**
     * Получить статистику бойцов
     */
    public FighterStatisticsDto getFighterStatistics() {
        return getFighterStatistics(null, null);
    }
    
    /**
     * Получить статистику бойцов с фильтрацией
     */
    public FighterStatisticsDto getFighterStatistics(String fightModeFilter, Integer seasonFilter) {
        return getFighterStatistics(fightModeFilter, seasonFilter, null, null);
    }
    
    /**
     * Получить статистику бойцов с фильтрацией по дате
     */
    public FighterStatisticsDto getFighterStatistics(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        log.debug("Получение статистики бойцов с фильтрами: fightMode={}, season={}, startDate={}, endDate={}", 
                  fightModeFilter, seasonFilter, startDate, endDate);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter, startDate, endDate);
        
        return FighterStatisticsDto.builder()
                .topFightersByWinRate(calculateTopFightersByWinRate(filteredFights))
                .worstFightersByWinRate(calculateWorstFightersByWinRate(filteredFights))
                .bestFinishers(calculateBestFinishers(filteredFights))
                .toughestOpponents(calculateToughestOpponents(filteredFights))
                .build();
    }
    
    /**
     * Получить отфильтрованные бои
     */
    private List<Fight> getFilteredFights(String fightModeFilter, Integer seasonFilter) {
        return getFilteredFights(fightModeFilter, seasonFilter, null, null);
    }
    
    /**
     * Получить отфильтрованные бои с фильтрацией по дате
     */
    private List<Fight> getFilteredFights(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        List<Fight> allFights = fightRepository.findAllOrderByFightDateDesc();
        
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
    
    /**
     * Топ 5 лучших бойцов по проценту побед
     */
    private List<FighterStatisticsDto.FighterWinRateDto> calculateTopFightersByWinRate(List<Fight> fights) {
        Map<String, List<Fight>> fightsByFighter = fights.stream()
                .collect(Collectors.groupingBy(Fight::getMyFighter));
        
        return fightsByFighter.entrySet().stream()
                .map(entry -> {
                    String fighterName = entry.getKey();
                    List<Fight> fighterFights = entry.getValue();
                    
                    long totalFights = fighterFights.size();
                    long wins = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.WIN ? 1 : 0)
                            .sum();
                    long losses = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.LOSS ? 1 : 0)
                            .sum();
                    long draws = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.DRAW ? 1 : 0)
                            .sum();
                    
                    double winRate = totalFights > 0 ? (double) wins / totalFights * 100 : 0.0;
                    double lossRate = totalFights > 0 ? (double) losses / totalFights * 100 : 0.0;
                    
                    return FighterStatisticsDto.FighterWinRateDto.builder()
                            .fighterName(fighterName)
                            .totalFights(totalFights)
                            .wins(wins)
                            .losses(losses)
                            .draws(draws)
                            .winRate(winRate)
                            .lossRate(lossRate)
                            .build();
                })
                .filter(fighter -> fighter.getTotalFights() >= 3) // Минимум 3 боя для статистики
                .sorted((a, b) -> {
                    // Сначала по проценту побед, затем по количеству побед
                    int winRateComparison = Double.compare(b.getWinRate(), a.getWinRate());
                    if (winRateComparison != 0) {
                        return winRateComparison;
                    }
                    return Long.compare(b.getWins(), a.getWins());
                })
                .limit(5)
                .collect(Collectors.toList());
    }
    
    /**
     * Топ 5 худших бойцов по проценту побед
     */
    private List<FighterStatisticsDto.FighterWinRateDto> calculateWorstFightersByWinRate(List<Fight> fights) {
        Map<String, List<Fight>> fightsByFighter = fights.stream()
                .collect(Collectors.groupingBy(Fight::getMyFighter));
        
        return fightsByFighter.entrySet().stream()
                .map(entry -> {
                    String fighterName = entry.getKey();
                    List<Fight> fighterFights = entry.getValue();
                    
                    long totalFights = fighterFights.size();
                    long wins = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.WIN ? 1 : 0)
                            .sum();
                    long losses = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.LOSS ? 1 : 0)
                            .sum();
                    long draws = fighterFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.DRAW ? 1 : 0)
                            .sum();
                    
                    double winRate = totalFights > 0 ? (double) wins / totalFights * 100 : 0.0;
                    double lossRate = totalFights > 0 ? (double) losses / totalFights * 100 : 0.0;
                    
                    return FighterStatisticsDto.FighterWinRateDto.builder()
                            .fighterName(fighterName)
                            .totalFights(totalFights)
                            .wins(wins)
                            .losses(losses)
                            .draws(draws)
                            .winRate(winRate)
                            .lossRate(lossRate)
                            .build();
                })
                .filter(fighter -> fighter.getTotalFights() >= 3) // Минимум 3 боя для статистики
                .sorted((a, b) -> {
                    // Сначала по проценту побед (по возрастанию - худшие), затем по количеству поражений (по убыванию - больше поражений)
                    int winRateComparison = Double.compare(a.getWinRate(), b.getWinRate());
                    if (winRateComparison != 0) {
                        return winRateComparison;
                    }
                    return Long.compare(b.getLosses(), a.getLosses());
                })
                .limit(5)
                .collect(Collectors.toList());
    }
    
    /**
     * Лучший нокаутер и мастер болевых
     */
    private FighterStatisticsDto.BestFinisherDto calculateBestFinishers(List<Fight> fights) {
        // Группируем бои по бойцам
        Map<String, List<Fight>> fightsByFighter = fights.stream()
                .collect(Collectors.groupingBy(Fight::getMyFighter));
        
        String bestKnockoutArtist = "";
        long maxKnockouts = 0;
        String bestSubmissionSpecialist = "";
        long maxSubmissions = 0;
        
        for (Map.Entry<String, List<Fight>> entry : fightsByFighter.entrySet()) {
            String fighterName = entry.getKey();
            List<Fight> fighterFights = entry.getValue();
            
            // Подсчитываем нокауты
            long knockouts = fighterFights.stream()
                    .filter(f -> f.getResult() == FightResult.WIN && f.getMethod() == FightMethod.KNOCKOUT)
                    .count();
            
            // Подсчитываем сабмишены
            long submissions = fighterFights.stream()
                    .filter(f -> f.getResult() == FightResult.WIN && f.getMethod() == FightMethod.SUBMISSION)
                    .count();
            
            if (knockouts > maxKnockouts) {
                maxKnockouts = knockouts;
                bestKnockoutArtist = fighterName;
            }
            
            if (submissions > maxSubmissions) {
                maxSubmissions = submissions;
                bestSubmissionSpecialist = fighterName;
            }
        }
        
        return FighterStatisticsDto.BestFinisherDto.builder()
                .bestKnockoutArtist(bestKnockoutArtist)
                .knockoutCount(maxKnockouts)
                .bestSubmissionSpecialist(bestSubmissionSpecialist)
                .submissionCount(maxSubmissions)
                .build();
    }
    
    /**
     * Топ 5 неудобных соперников
     */
    private List<FighterStatisticsDto.OpponentLossDto> calculateToughestOpponents(List<Fight> fights) {
        Map<String, List<Fight>> fightsByOpponent = fights.stream()
                .collect(Collectors.groupingBy(Fight::getOpponent));
        
        return fightsByOpponent.entrySet().stream()
                .map(entry -> {
                    String opponentName = entry.getKey();
                    List<Fight> opponentFights = entry.getValue();
                    
                    long totalFights = opponentFights.size();
                    long losses = opponentFights.stream()
                            .mapToLong(f -> f.getResult() == FightResult.LOSS ? 1 : 0)
                            .sum();
                    
                    double lossRate = totalFights > 0 ? (double) losses / totalFights * 100 : 0.0;
                    
                    return FighterStatisticsDto.OpponentLossDto.builder()
                            .opponentName(opponentName)
                            .totalFights(totalFights)
                            .losses(losses)
                            .lossRate(lossRate)
                            .build();
                })
                .filter(opponent -> opponent.getTotalFights() >= 2) // Минимум 2 боя с соперником
                .filter(opponent -> opponent.getLosses() > 0) // Только те, от кого были поражения
                .sorted((a, b) -> {
                    // Сначала по проценту поражений, потом по количеству поражений
                    int lossRateComparison = Double.compare(b.getLossRate(), a.getLossRate());
                    if (lossRateComparison != 0) {
                        return lossRateComparison;
                    }
                    return Long.compare(b.getLosses(), a.getLosses());
                })
                .limit(5)
                .collect(Collectors.toList());
    }
}
