package com.ufcstats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO для статистики бойцов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FighterStatisticsDto {
    
    // Топ 5 лучших бойцов по проценту побед
    private List<FighterWinRateDto> topFightersByWinRate;
    
    // Топ 5 худших бойцов по проценту побед
    private List<FighterWinRateDto> worstFightersByWinRate;
    
    // Лучший нокаутер и мастер болевых
    private BestFinisherDto bestFinishers;
    
    // Топ 5 неудобных соперников
    private List<OpponentLossDto> toughestOpponents;
    
    /**
     * Статистика бойца по проценту побед
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FighterWinRateDto {
        private String fighterName;
        private long totalFights;
        private long wins;
        private long losses;
        private long draws;
        private double winRate;
        private double lossRate;
    }
    
    /**
     * Лучшие финишеры
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BestFinisherDto {
        private String bestKnockoutArtist;
        private long knockoutCount;
        private String bestSubmissionSpecialist;
        private long submissionCount;
    }
    
    /**
     * Статистика по неудобным соперникам
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpponentLossDto {
        private String opponentName;
        private long totalFights;
        private long losses;
        private double lossRate;
    }
}
