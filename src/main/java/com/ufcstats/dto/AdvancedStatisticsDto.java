package com.ufcstats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO для расширенной статистики боев
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedStatisticsDto {
    
    // Основная статистика
    private BasicStatisticsDto basic;
    
    // Статистика по режимам боев
    private Map<String, BasicStatisticsDto> byFightMode;
    
    // Статистика по весовым категориям
    private Map<String, BasicStatisticsDto> byWeightClass;
    
    // Статистика по методам побед/поражений
    private Map<String, Long> winMethods;
    private Map<String, Long> lossMethods;
    
    // Статистика по ударам
    private StrikeStatisticsDto strikes;
    
    // Статистика по тейкдаунам
    private TakedownStatisticsDto takedowns;
    
    // Статистика по времени контроля
    private ControlTimeStatisticsDto controlTime;
    
    // Статистика по судейским оценкам
    private JudgeScoreStatisticsDto judgeScores;
    
    // Движение рейтинга
    private List<RatingHistoryDto> ratingHistory;
    
    // Движение по рейтингу
    private List<RankingHistoryDto> rankingHistory;
    
    /**
     * Базовая статистика
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicStatisticsDto {
        private long totalFights;
        private long wins;
        private long losses;
        private long draws;
        private double winRate;
        private double lossRate;
        private double drawRate;
    }
    
    /**
     * Статистика по ударам
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrikeStatisticsDto {
        // Мои удары
        private double mySignificantStrikesAccuracy;
        private double myTotalStrikesAccuracy;
        private double mySignificantStrikesLanded;
        private double myTotalStrikesLanded;
        
        // Удары соперника
        private double opponentSignificantStrikesAccuracy;
        private double opponentTotalStrikesAccuracy;
        private double opponentSignificantStrikesLanded;
        private double opponentTotalStrikesLanded;
        
        // Средние значения за бой
        private double avgMySignificantStrikesPerFight;
        private double avgOpponentSignificantStrikesPerFight;
        private double avgMyTotalStrikesPerFight;
        private double avgOpponentTotalStrikesPerFight;
    }
    
    /**
     * Статистика по тейкдаунам
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TakedownStatisticsDto {
        // Мои тейкдауны
        private double myTakedownAccuracy;
        private double myTakedownsSuccessful;
        private double myTakedownsAttempted;
        
        // Тейкдауны соперника
        private double opponentTakedownAccuracy;
        private double opponentTakedownsSuccessful;
        private double opponentTakedownsAttempted;
        
        // Средние значения за бой
        private double avgMyTakedownsPerFight;
        private double avgOpponentTakedownsPerFight;
    }
    
    /**
     * Статистика по времени контроля
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ControlTimeStatisticsDto {
        private double avgMyControlTimeSeconds;
        private double avgOpponentControlTimeSeconds;
        private double totalMyControlTimeSeconds;
        private double totalOpponentControlTimeSeconds;
        private String avgMyControlTimeFormatted;
        private String avgOpponentControlTimeFormatted;
    }
    
    /**
     * Статистика по судейским оценкам
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JudgeScoreStatisticsDto {
        private double avgMyScore;
        private double avgOpponentScore;
        private double avgScoreDifference;
        private long fightsWonByJudges;
        private long fightsLostByJudges;
        private long fightsDrawByJudges;
        private double judgeWinRate;
    }
    
    /**
     * История рейтинга
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingHistoryDto {
        private String date;
        private int rating;
        private int change;
        private String fightResult;
        private String opponent;
    }
    
    /**
     * История места в рейтинге
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingHistoryDto {
        private String date;
        private int ranking;
        private int change;
        private String fightResult;
        private String opponent;
    }
}
