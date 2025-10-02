package com.ufcstats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных об изменениях рейтинга и места в рейтинге
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingChangeDto {
    
    /**
     * Текущее значение рейтинговых очков
     */
    private Integer currentRatingPoints;
    
    /**
     * Предыдущее значение рейтинговых очков
     */
    private Integer previousRatingPoints;
    
    /**
     * Изменение рейтинговых очков (текущее - предыдущее)
     */
    private Integer ratingPointsChange;
    
    /**
     * Текущее место в рейтинге
     */
    private Integer currentRankingPosition;
    
    /**
     * Предыдущее место в рейтинге
     */
    private Integer previousRankingPosition;
    
    /**
     * Изменение места в рейтинге (предыдущее - текущее, так как меньшее место лучше)
     */
    private Integer rankingPositionChange;
    
    /**
     * Режим боя (STANCE или MMA)
     */
    private String fightMode;
    
    /**
     * Получить направление изменения рейтинговых очков
     * @return "up" если рейтинг вырос, "down" если упал, "same" если не изменился
     */
    public String getRatingPointsDirection() {
        if (ratingPointsChange == null || ratingPointsChange == 0) {
            return "same";
        }
        return ratingPointsChange > 0 ? "up" : "down";
    }
    
    /**
     * Получить направление изменения места в рейтинге
     * @return "up" если место улучшилось (уменьшилось), "down" если ухудшилось (увеличилось), "same" если не изменилось
     */
    public String getRankingPositionDirection() {
        if (rankingPositionChange == null || rankingPositionChange == 0) {
            return "same";
        }
        return rankingPositionChange > 0 ? "up" : "down";
    }
    
    /**
     * Получить цвет для отображения изменения рейтинговых очков
     * @return "success" для роста, "danger" для падения, "muted" для отсутствия изменений
     */
    public String getRatingPointsColor() {
        String direction = getRatingPointsDirection();
        return switch (direction) {
            case "up" -> "success";
            case "down" -> "danger";
            default -> "muted";
        };
    }
    
    /**
     * Получить цвет для отображения изменения места в рейтинге
     * @return "success" для улучшения места, "danger" для ухудшения, "muted" для отсутствия изменений
     */
    public String getRankingPositionColor() {
        String direction = getRankingPositionDirection();
        return switch (direction) {
            case "up" -> "success";
            case "down" -> "danger";
            default -> "muted";
        };
    }
    
    /**
     * Получить иконку для отображения изменения рейтинговых очков
     * @return Bootstrap иконка
     */
    public String getRatingPointsIcon() {
        String direction = getRatingPointsDirection();
        return switch (direction) {
            case "up" -> "bi-arrow-up";
            case "down" -> "bi-arrow-down";
            default -> "bi-dash";
        };
    }
    
    /**
     * Получить иконку для отображения изменения места в рейтинге
     * @return Bootstrap иконка
     */
    public String getRankingPositionIcon() {
        String direction = getRankingPositionDirection();
        return switch (direction) {
            case "up" -> "bi-arrow-up";
            case "down" -> "bi-arrow-down";
            default -> "bi-dash";
        };
    }
}
