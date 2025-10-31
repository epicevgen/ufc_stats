package com.ufcstats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementStatisticsDto {
    
    // Максимальные значения
    private Integer maxRatingPoints;
    private Integer maxRankingPosition;
    private Integer maxWinStreak;
    private Integer maxLossStreak;
    
    // Минимальные значения
    private Integer minRatingPoints;
    private Integer minRankingPosition;
    
    // Дополнительная информация
    private String maxRatingPointsDate;
    private String maxRankingPositionDate;
    private String maxWinStreakStartDate;
    private String maxWinStreakEndDate;
    private String maxLossStreakStartDate;
    private String maxLossStreakEndDate;
    private String minRatingPointsDate;
    private String minRankingPositionDate;
    
    // Статистика серий
    private Integer currentWinStreak;
    private Integer currentLossStreak;
    private String currentWinStreakStartDate;
    private String currentLossStreakStartDate;
    
    // Средние значения рейтинга
    private Double avgRatingPoints;
    private Integer ratingPointsQ1;
    private Integer ratingPointsQ3;
    
    // Средние значения места
    private Double avgRankingPosition;
    private Integer rankingPositionQ1;
    private Integer rankingPositionQ3;
}
