package com.ufcstats.service;

import com.ufcstats.dto.AchievementStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.debug("Получение статистики достижений с фильтрами: fightMode={}, season={}", fightModeFilter, seasonFilter);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter);

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
                .build();
    }
    
    /**
     * Получить отфильтрованные бои
     */
    private List<Fight> getFilteredFights(String fightModeFilter, Integer seasonFilter) {
        List<Fight> allFights = fightRepository.findAll().stream()
                .sorted((f1, f2) -> f1.getFightDate().compareTo(f2.getFightDate()))
                .collect(Collectors.toList());
        
        return allFights.stream()
                .filter(fight -> fightModeFilter == null || fight.getFightMode().name().equals(fightModeFilter))
                .filter(fight -> seasonFilter == null || fight.getSeason().equals(seasonFilter))
                .collect(Collectors.toList());
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
}
