package com.ufcstats.service;

import com.ufcstats.dto.AchievementStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AchievementStatisticsService {

    private final FightRepository fightRepository;

    public AchievementStatisticsDto getAchievementStatistics() {
        return getAchievementStatistics(null, null);
    }
    
    public AchievementStatisticsDto getAchievementStatistics(String fightModeFilter, Integer seasonFilter) {
        return getAchievementStatistics(fightModeFilter, seasonFilter, null, null);
    }
    
    public AchievementStatisticsDto getAchievementStatistics(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        log.debug("Получение статистики достижений с фильтрами: fightMode={}, season={}, startDate={}, endDate={}", 
                  fightModeFilter, seasonFilter, startDate, endDate);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter, startDate, endDate);

        return AchievementStatisticsDto.builder()
                .maxRatingPoints(calculateMaxRatingPoints(filteredFights))
                .maxRankingPosition(calculateMaxRankingPosition(filteredFights))
                .maxWinStreak(calculateMaxWinStreak(filteredFights))
                .maxLossStreak(calculateMaxLossStreak(filteredFights))
                .minRatingPoints(calculateMinRatingPoints(filteredFights))
                .minRankingPosition(calculateMinRankingPosition(filteredFights))
                .maxRatingPointsDate(findMaxRatingPointsDate(filteredFights))
                .maxRankingPositionDate(findMaxRankingPositionDate(filteredFights))
                .maxWinStreakStartDate(findMaxWinStreakStartDate(filteredFights))
                .maxWinStreakEndDate(findMaxWinStreakEndDate(filteredFights))
                .maxLossStreakStartDate(findMaxLossStreakStartDate(filteredFights))
                .maxLossStreakEndDate(findMaxLossStreakEndDate(filteredFights))
                .minRatingPointsDate(findMinRatingPointsDate(filteredFights))
                .minRankingPositionDate(findMinRankingPositionDate(filteredFights))
                .currentWinStreak(calculateCurrentWinStreak(filteredFights))
                .currentLossStreak(calculateCurrentLossStreak(filteredFights))
                .currentWinStreakStartDate(findCurrentWinStreakStartDate(filteredFights))
                .currentLossStreakStartDate(findCurrentLossStreakStartDate(filteredFights))
                .avgRatingPoints(calculateAvgRatingPoints(filteredFights))
                .ratingPointsQ1(calculateRatingPointsQ1(filteredFights))
                .ratingPointsQ3(calculateRatingPointsQ3(filteredFights))
                .avgRankingPosition(calculateAvgRankingPosition(filteredFights))
                .rankingPositionQ1(calculateRankingPositionQ1(filteredFights))
                .rankingPositionQ3(calculateRankingPositionQ3(filteredFights))
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

    private Integer calculateMaxRatingPoints(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRatingPoints() != null)
                .mapToInt(Fight::getRatingPoints)
                .max()
                .orElse(0);
    }

    private Integer calculateMaxRankingPosition(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRankingPosition() != null)
                .mapToInt(Fight::getRankingPosition)
                .max()
                .orElse(0);
    }

    private Integer calculateMinRatingPoints(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRatingPoints() != null)
                .mapToInt(Fight::getRatingPoints)
                .min()
                .orElse(0);
    }

    private Integer calculateMinRankingPosition(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRankingPosition() != null)
                .mapToInt(Fight::getRankingPosition)
                .min()
                .orElse(0);
    }

    private Integer calculateMaxWinStreak(List<Fight> fights) {
        int maxStreak = 0;
        int currentStreak = 0;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.WIN) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
        }
        
        return maxStreak;
    }

    private Integer calculateMaxLossStreak(List<Fight> fights) {
        int maxStreak = 0;
        int currentStreak = 0;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.LOSS) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
        }
        
        return maxStreak;
    }

    private Integer calculateCurrentWinStreak(List<Fight> fights) {
        int currentStreak = 0;
        
        // Идем с конца списка (последние бои)
        for (int i = fights.size() - 1; i >= 0; i--) {
            Fight fight = fights.get(i);
            if (fight.getResult() == FightResult.WIN) {
                currentStreak++;
            } else {
                break;
            }
        }
        
        return currentStreak;
    }

    private Integer calculateCurrentLossStreak(List<Fight> fights) {
        int currentStreak = 0;
        
        // Идем с конца списка (последние бои)
        for (int i = fights.size() - 1; i >= 0; i--) {
            Fight fight = fights.get(i);
            if (fight.getResult() == FightResult.LOSS) {
                currentStreak++;
            } else {
                break;
            }
        }
        
        return currentStreak;
    }

    private String findMaxRatingPointsDate(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRatingPoints() != null)
                .max((f1, f2) -> Integer.compare(f1.getRatingPoints(), f2.getRatingPoints()))
                .map(f -> f.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .orElse("N/A");
    }

    private String findMaxRankingPositionDate(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRankingPosition() != null)
                .max((f1, f2) -> Integer.compare(f1.getRankingPosition(), f2.getRankingPosition()))
                .map(f -> f.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .orElse("N/A");
    }

    private String findMinRatingPointsDate(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRatingPoints() != null)
                .min((f1, f2) -> Integer.compare(f1.getRatingPoints(), f2.getRatingPoints()))
                .map(f -> f.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .orElse("N/A");
    }

    private String findMinRankingPositionDate(List<Fight> fights) {
        return fights.stream()
                .filter(f -> f.getRankingPosition() != null)
                .min((f1, f2) -> Integer.compare(f1.getRankingPosition(), f2.getRankingPosition()))
                .map(f -> f.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .orElse("N/A");
    }

    private String findMaxWinStreakStartDate(List<Fight> fights) {
        int maxStreak = calculateMaxWinStreak(fights);
        if (maxStreak == 0) return "N/A";
        
        int currentStreak = 0;
        Fight startFight = null;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.WIN) {
                currentStreak++;
                if (currentStreak == maxStreak) {
                    startFight = fight;
                    break;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        return startFight != null ? 
                startFight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A";
    }

    private String findMaxWinStreakEndDate(List<Fight> fights) {
        int maxStreak = calculateMaxWinStreak(fights);
        if (maxStreak == 0) return "N/A";
        
        int currentStreak = 0;
        Fight endFight = null;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.WIN) {
                currentStreak++;
                if (currentStreak == maxStreak) {
                    endFight = fight;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        return endFight != null ? 
                endFight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A";
    }

    private String findMaxLossStreakStartDate(List<Fight> fights) {
        int maxStreak = calculateMaxLossStreak(fights);
        if (maxStreak == 0) return "N/A";
        
        int currentStreak = 0;
        Fight startFight = null;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.LOSS) {
                currentStreak++;
                if (currentStreak == maxStreak) {
                    startFight = fight;
                    break;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        return startFight != null ? 
                startFight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A";
    }

    private String findMaxLossStreakEndDate(List<Fight> fights) {
        int maxStreak = calculateMaxLossStreak(fights);
        if (maxStreak == 0) return "N/A";
        
        int currentStreak = 0;
        Fight endFight = null;
        
        for (Fight fight : fights) {
            if (fight.getResult() == FightResult.LOSS) {
                currentStreak++;
                if (currentStreak == maxStreak) {
                    endFight = fight;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        return endFight != null ? 
                endFight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A";
    }

    private String findCurrentWinStreakStartDate(List<Fight> fights) {
        int currentStreak = calculateCurrentWinStreak(fights);
        if (currentStreak == 0) return "N/A";
        
        // Идем с конца списка (последние бои)
        for (int i = fights.size() - 1; i >= 0; i--) {
            Fight fight = fights.get(i);
            if (fight.getResult() == FightResult.WIN) {
                if (i == 0 || fights.get(i - 1).getResult() != FightResult.WIN) {
                    return fight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }
            } else {
                break;
            }
        }
        
        return "N/A";
    }

    private String findCurrentLossStreakStartDate(List<Fight> fights) {
        int currentStreak = calculateCurrentLossStreak(fights);
        if (currentStreak == 0) return "N/A";
        
        // Идем с конца списка (последние бои)
        for (int i = fights.size() - 1; i >= 0; i--) {
            Fight fight = fights.get(i);
            if (fight.getResult() == FightResult.LOSS) {
                if (i == 0 || fights.get(i - 1).getResult() != FightResult.LOSS) {
                    return fight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }
            } else {
                break;
            }
        }
        
        return "N/A";
    }
    
    /**
     * Расчет среднего значения рейтинга
     */
    private Double calculateAvgRatingPoints(List<Fight> fights) {
        List<Integer> ratingValues = fights.stream()
                .filter(f -> f.getRatingPoints() != null && f.getRatingPoints() > 0)
                .map(Fight::getRatingPoints)
                .collect(Collectors.toList());
        
        if (ratingValues.isEmpty()) {
            return 0.0;
        }
        
        return ratingValues.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Расчет первого квартиля для рейтинга (Q1)
     */
    private Integer calculateRatingPointsQ1(List<Fight> fights) {
        List<Integer> ratingValues = fights.stream()
                .filter(f -> f.getRatingPoints() != null && f.getRatingPoints() > 0)
                .map(Fight::getRatingPoints)
                .sorted()
                .collect(Collectors.toList());
        
        if (ratingValues.isEmpty()) {
            return 0;
        }
        
        int q1Index = (int) Math.floor(ratingValues.size() * 0.25);
        return ratingValues.get(q1Index);
    }
    
    /**
     * Расчет третьего квартиля для рейтинга (Q3)
     */
    private Integer calculateRatingPointsQ3(List<Fight> fights) {
        List<Integer> ratingValues = fights.stream()
                .filter(f -> f.getRatingPoints() != null && f.getRatingPoints() > 0)
                .map(Fight::getRatingPoints)
                .sorted()
                .collect(Collectors.toList());
        
        if (ratingValues.isEmpty()) {
            return 0;
        }
        
        int q3Index = (int) Math.floor(ratingValues.size() * 0.75);
        return ratingValues.get(q3Index);
    }
    
    /**
     * Расчет среднего значения места в рейтинге
     */
    private Double calculateAvgRankingPosition(List<Fight> fights) {
        List<Integer> rankingValues = fights.stream()
                .filter(f -> f.getRankingPosition() != null && f.getRankingPosition() > 0)
                .map(Fight::getRankingPosition)
                .collect(Collectors.toList());
        
        if (rankingValues.isEmpty()) {
            return 0.0;
        }
        
        return rankingValues.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Расчет первого квартиля для места в рейтинге (Q1)
     */
    private Integer calculateRankingPositionQ1(List<Fight> fights) {
        List<Integer> rankingValues = fights.stream()
                .filter(f -> f.getRankingPosition() != null && f.getRankingPosition() > 0)
                .map(Fight::getRankingPosition)
                .sorted()
                .collect(Collectors.toList());
        
        if (rankingValues.isEmpty()) {
            return 0;
        }
        
        int q1Index = (int) Math.floor(rankingValues.size() * 0.25);
        return rankingValues.get(q1Index);
    }
    
    /**
     * Расчет третьего квартиля для места в рейтинге (Q3)
     */
    private Integer calculateRankingPositionQ3(List<Fight> fights) {
        List<Integer> rankingValues = fights.stream()
                .filter(f -> f.getRankingPosition() != null && f.getRankingPosition() > 0)
                .map(Fight::getRankingPosition)
                .sorted()
                .collect(Collectors.toList());
        
        if (rankingValues.isEmpty()) {
            return 0;
        }
        
        int q3Index = (int) Math.floor(rankingValues.size() * 0.75);
        return rankingValues.get(q3Index);
    }
}
