package com.ufcstats.service;

import com.ufcstats.dto.FighterStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.debug("Получение статистики бойцов с фильтрами: fightMode={}, season={}", fightModeFilter, seasonFilter);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter);
        
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
        List<Fight> allFights = fightRepository.findAllOrderByFightDateDesc();
        
        return allFights.stream()
                .filter(fight -> fightModeFilter == null || fight.getFightMode().name().equals(fightModeFilter))
                .filter(fight -> seasonFilter == null || fight.getSeason().equals(seasonFilter))
                .collect(Collectors.toList());
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
                .sorted((a, b) -> Double.compare(b.getWinRate(), a.getWinRate()))
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
                .sorted((a, b) -> Double.compare(a.getWinRate(), b.getWinRate()))
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
