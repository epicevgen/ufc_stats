package com.ufcstats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrikeMovementDto {
    private List<StrikeDataPoint> totalStrikesHistory;
    private List<StrikeDataPoint> significantStrikesHistory;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrikeDataPoint {
        private int fightNumber;
        private String fightDate;
        private String opponent;
        private String result;
        
        // Количество ударов
        private int totalStrikesLanded;
        private int totalStrikesAttempted;
        private int significantStrikesLanded;
        private int significantStrikesAttempted;
        
        // Процент попаданий
        private double totalStrikesAccuracy;
        private double significantStrikesAccuracy;
    }
}
