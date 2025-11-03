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
    
    // Статистика по повреждениям
    private DamageStatisticsDto damage;
    
    // Статистика по судейским оценкам
    private JudgeScoreStatisticsDto judgeScores;
    
    // Статистика боев с высокой точностью ударов
    private HighAccuracyStrikesStatisticsDto highAccuracyStrikes;
    
    // Статистика боев с преимуществом по ударам
    private StrikeAdvantageStatisticsDto strikeAdvantage;
    
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
        private String weightClass;
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
    
    /**
     * Статистика боев с точностью ударов 50% и выше
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HighAccuracyStrikesStatisticsDto {
        // Общие удары
        private long myTotalStrikesHighAccuracyCount;
        private long opponentTotalStrikesHighAccuracyCount;
        private double myTotalStrikesHighAccuracyPercent;
        private double opponentTotalStrikesHighAccuracyPercent;
        
        // Значимые удары
        private long mySignificantStrikesHighAccuracyCount;
        private long opponentSignificantStrikesHighAccuracyCount;
        private double mySignificantStrikesHighAccuracyPercent;
        private double opponentSignificantStrikesHighAccuracyPercent;
        
        // Общее количество боев для расчета процентов
        private long totalFights;
    }
    
    /**
     * Статистика боев с преимуществом по ударам
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrikeAdvantageStatisticsDto {
        // Бои где выброшено общих ударов больше чем у противника
        private long myAttemptedTotalStrikesAdvantageCount;
        private long opponentAttemptedTotalStrikesAdvantageCount;
        private double myAttemptedTotalStrikesAdvantagePercent;
        private double opponentAttemptedTotalStrikesAdvantagePercent;
        
        // Бои где выброшено значимых ударов больше чем у противника
        private long myAttemptedSignificantStrikesAdvantageCount;
        private long opponentAttemptedSignificantStrikesAdvantageCount;
        private double myAttemptedSignificantStrikesAdvantagePercent;
        private double opponentAttemptedSignificantStrikesAdvantagePercent;
        
        // Бои где донесено общих ударов больше чем у противника
        private long myLandedTotalStrikesAdvantageCount;
        private long opponentLandedTotalStrikesAdvantageCount;
        private double myLandedTotalStrikesAdvantagePercent;
        private double opponentLandedTotalStrikesAdvantagePercent;
        
        // Бои где донесено значимых ударов больше чем у противника
        private long myLandedSignificantStrikesAdvantageCount;
        private long opponentLandedSignificantStrikesAdvantageCount;
        private double myLandedSignificantStrikesAdvantagePercent;
        private double opponentLandedSignificantStrikesAdvantagePercent;
        
        // Бои где донесено общих И значимых ударов больше чем у противника
        private long myLandedBothStrikesAdvantageCount;
        private long opponentLandedBothStrikesAdvantageCount;
        private double myLandedBothStrikesAdvantagePercent;
        private double opponentLandedBothStrikesAdvantagePercent;
        
        // Бои где процент попадания общих ударов больше чем у противника
        private long myAccuracyTotalStrikesAdvantageCount;
        private long opponentAccuracyTotalStrikesAdvantageCount;
        private double myAccuracyTotalStrikesAdvantagePercent;
        private double opponentAccuracyTotalStrikesAdvantagePercent;
        
        // Бои где процент попадания значимых ударов больше чем у противника
        private long myAccuracySignificantStrikesAdvantageCount;
        private long opponentAccuracySignificantStrikesAdvantageCount;
        private double myAccuracySignificantStrikesAdvantagePercent;
        private double opponentAccuracySignificantStrikesAdvantagePercent;
        
        // Общее количество боев для расчета процентов
        private long totalFights;
    }
    
    /**
     * Статистика по повреждениям
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DamageStatisticsDto {
        // Мои повреждения
        private double avgMyHeadDamagePerFight;
        private double avgMyBodyDamagePerFight;
        private double avgMyLegDamagePerFight;
        private double avgMyKnockdownsPerFight;
        
        // Повреждения соперника
        private double avgOpponentHeadDamagePerFight;
        private double avgOpponentBodyDamagePerFight;
        private double avgOpponentLegDamagePerFight;
        private double avgOpponentKnockdownsPerFight;
        
        // Общие показатели
        private double totalMyHeadDamage;
        private double totalMyBodyDamage;
        private double totalMyLegDamage;
        private double totalMyKnockdowns;
        private double totalOpponentHeadDamage;
        private double totalOpponentBodyDamage;
        private double totalOpponentLegDamage;
        private double totalOpponentKnockdowns;
    }
}
