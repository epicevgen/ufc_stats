package com.ufcstats.service;

import com.ufcstats.dto.AdvancedStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для расчета расширенной статистики боев
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdvancedStatisticsService {
    
    private final FightRepository fightRepository;
    
    /**
     * Получить расширенную статистику
     */
    public AdvancedStatisticsDto getAdvancedStatistics() {
        return getAdvancedStatistics(null, null);
    }
    
    /**
     * Получить расширенную статистику с фильтрацией
     */
    public AdvancedStatisticsDto getAdvancedStatistics(String fightModeFilter, Integer seasonFilter) {
        return getAdvancedStatistics(fightModeFilter, seasonFilter, null, null);
    }
    
    /**
     * Получить расширенную статистику с фильтрацией по дате
     */
    public AdvancedStatisticsDto getAdvancedStatistics(String fightModeFilter, Integer seasonFilter, String startDate, String endDate) {
        log.debug("Получение расширенной статистики с фильтрами: fightMode={}, season={}, startDate={}, endDate={}", 
                  fightModeFilter, seasonFilter, startDate, endDate);
        
        List<Fight> filteredFights = getFilteredFights(fightModeFilter, seasonFilter, startDate, endDate);
        
        return AdvancedStatisticsDto.builder()
                .basic(calculateBasicStatistics(filteredFights))
                .byFightMode(calculateStatisticsByFightMode(filteredFights))
                .byWeightClass(calculateStatisticsByWeightClass(filteredFights))
                .winMethods(calculateWinMethods(filteredFights))
                .lossMethods(calculateLossMethods(filteredFights))
                .strikes(calculateStrikeStatistics(filteredFights))
                .takedowns(calculateTakedownStatistics(filteredFights))
                .controlTime(calculateControlTimeStatistics(filteredFights))
                .judgeScores(calculateJudgeScoreStatistics(filteredFights))
                .ratingHistory(calculateRatingHistory(filteredFights))
                .rankingHistory(calculateRankingHistory(filteredFights))
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
     * Расчет базовой статистики
     */
    private AdvancedStatisticsDto.BasicStatisticsDto calculateBasicStatistics(List<Fight> fights) {
        long totalFights = fights.size();
        long wins = fights.stream().mapToLong(f -> f.getResult() == FightResult.WIN ? 1 : 0).sum();
        long losses = fights.stream().mapToLong(f -> f.getResult() == FightResult.LOSS ? 1 : 0).sum();
        long draws = fights.stream().mapToLong(f -> f.getResult() == FightResult.DRAW ? 1 : 0).sum();
        
        return AdvancedStatisticsDto.BasicStatisticsDto.builder()
                .totalFights(totalFights)
                .wins(wins)
                .losses(losses)
                .draws(draws)
                .winRate(totalFights > 0 ? (double) wins / totalFights * 100 : 0.0)
                .lossRate(totalFights > 0 ? (double) losses / totalFights * 100 : 0.0)
                .drawRate(totalFights > 0 ? (double) draws / totalFights * 100 : 0.0)
                .build();
    }
    
    /**
     * Расчет статистики по режимам боев
     */
    private Map<String, AdvancedStatisticsDto.BasicStatisticsDto> calculateStatisticsByFightMode(List<Fight> fights) {
        Map<String, AdvancedStatisticsDto.BasicStatisticsDto> result = new HashMap<>();
        fights.stream()
                .collect(Collectors.groupingBy(fight -> fight.getFightMode().name()))
                .forEach((mode, fightList) -> {
                    result.put(mode, calculateBasicStatistics(fightList));
                });
        return result;
    }
    
    /**
     * Расчет статистики по весовым категориям
     */
    private Map<String, AdvancedStatisticsDto.BasicStatisticsDto> calculateStatisticsByWeightClass(List<Fight> fights) {
        Map<String, AdvancedStatisticsDto.BasicStatisticsDto> result = new HashMap<>();
        fights.stream()
                .collect(Collectors.groupingBy(fight -> fight.getWeightClass().name()))
                .forEach((weightClass, fightList) -> {
                    result.put(weightClass, calculateBasicStatistics(fightList));
                });
        
        // Сортируем по убыванию процента побед
        return result.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(
                    entry2.getValue().getWinRate(), 
                    entry1.getValue().getWinRate()
                ))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
    
    /**
     * Расчет методов побед
     */
    private Map<String, Long> calculateWinMethods(List<Fight> fights) {
        Map<String, Long> counts = fights.stream()
                .filter(fight -> fight.getResult() == FightResult.WIN)
                .collect(Collectors.groupingBy(
                        fight -> fight.getMethod().name(),
                        Collectors.counting()
                ));

        // Сортировка по убыванию количества (эквивалентно убыванию процента)
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    /**
     * Расчет методов поражений
     */
    private Map<String, Long> calculateLossMethods(List<Fight> fights) {
        Map<String, Long> counts = fights.stream()
                .filter(fight -> fight.getResult() == FightResult.LOSS)
                .collect(Collectors.groupingBy(
                        fight -> fight.getMethod().name(),
                        Collectors.counting()
                ));

        // Сортировка по убыванию количества (эквивалентно убыванию процента)
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    /**
     * Расчет статистики по ударам
     */
    private AdvancedStatisticsDto.StrikeStatisticsDto calculateStrikeStatistics(List<Fight> fights) {
        List<FightRound> allRounds = fights.stream()
                .flatMap(fight -> fight.getRounds().stream())
                .collect(Collectors.toList());
        
        if (allRounds.isEmpty()) {
            return AdvancedStatisticsDto.StrikeStatisticsDto.builder()
                    .mySignificantStrikesAccuracy(0.0)
                    .myTotalStrikesAccuracy(0.0)
                    .mySignificantStrikesLanded(0.0)
                    .myTotalStrikesLanded(0.0)
                    .opponentSignificantStrikesAccuracy(0.0)
                    .opponentTotalStrikesAccuracy(0.0)
                    .opponentSignificantStrikesLanded(0.0)
                    .opponentTotalStrikesLanded(0.0)
                    .avgMySignificantStrikesPerFight(0.0)
                    .avgOpponentSignificantStrikesPerFight(0.0)
                    .avgMyTotalStrikesPerFight(0.0)
                    .avgOpponentTotalStrikesPerFight(0.0)
                    .build();
        }
        
        // Мои удары
        double mySignificantLanded = allRounds.stream().mapToDouble(FightRound::getMySignificantStrikesLanded).sum();
        double mySignificantAttempted = allRounds.stream().mapToDouble(FightRound::getMySignificantStrikesAttempted).sum();
        double myTotalLanded = allRounds.stream().mapToDouble(FightRound::getMyTotalStrikesLanded).sum();
        double myTotalAttempted = allRounds.stream().mapToDouble(FightRound::getMyTotalStrikesAttempted).sum();
        
        // Удары соперника
        double opponentSignificantLanded = allRounds.stream().mapToDouble(FightRound::getOpponentSignificantStrikesLanded).sum();
        double opponentSignificantAttempted = allRounds.stream().mapToDouble(FightRound::getOpponentSignificantStrikesAttempted).sum();
        double opponentTotalLanded = allRounds.stream().mapToDouble(FightRound::getOpponentTotalStrikesLanded).sum();
        double opponentTotalAttempted = allRounds.stream().mapToDouble(FightRound::getOpponentTotalStrikesAttempted).sum();
        
        return AdvancedStatisticsDto.StrikeStatisticsDto.builder()
                .mySignificantStrikesAccuracy(mySignificantAttempted > 0 ? mySignificantLanded / mySignificantAttempted * 100 : 0)
                .myTotalStrikesAccuracy(myTotalAttempted > 0 ? myTotalLanded / myTotalAttempted * 100 : 0)
                .mySignificantStrikesLanded(mySignificantLanded)
                .myTotalStrikesLanded(myTotalLanded)
                .opponentSignificantStrikesAccuracy(opponentSignificantAttempted > 0 ? opponentSignificantLanded / opponentSignificantAttempted * 100 : 0)
                .opponentTotalStrikesAccuracy(opponentTotalAttempted > 0 ? opponentTotalLanded / opponentTotalAttempted * 100 : 0)
                .opponentSignificantStrikesLanded(opponentSignificantLanded)
                .opponentTotalStrikesLanded(opponentTotalLanded)
                .avgMySignificantStrikesPerFight(fights.size() > 0 ? mySignificantLanded / fights.size() : 0)
                .avgOpponentSignificantStrikesPerFight(fights.size() > 0 ? opponentSignificantLanded / fights.size() : 0)
                .avgMyTotalStrikesPerFight(fights.size() > 0 ? myTotalLanded / fights.size() : 0)
                .avgOpponentTotalStrikesPerFight(fights.size() > 0 ? opponentTotalLanded / fights.size() : 0)
                .build();
    }
    
    /**
     * Расчет статистики по тейкдаунам (только для боев в режиме ММА)
     */
    private AdvancedStatisticsDto.TakedownStatisticsDto calculateTakedownStatistics(List<Fight> fights) {
        // Фильтруем только бои в режиме ММА
        List<Fight> mmaFights = fights.stream()
                .filter(fight -> fight.getFightMode() != null && fight.getFightMode().name().equals("MMA"))
                .collect(Collectors.toList());
        
        List<FightRound> allRounds = mmaFights.stream()
                .flatMap(fight -> fight.getRounds().stream())
                .collect(Collectors.toList());
        
        if (allRounds.isEmpty()) {
            return AdvancedStatisticsDto.TakedownStatisticsDto.builder()
                    .myTakedownAccuracy(0.0)
                    .myTakedownsSuccessful(0.0)
                    .myTakedownsAttempted(0.0)
                    .opponentTakedownAccuracy(0.0)
                    .opponentTakedownsSuccessful(0.0)
                    .opponentTakedownsAttempted(0.0)
                    .avgMyTakedownsPerFight(0.0)
                    .avgOpponentTakedownsPerFight(0.0)
                    .build();
        }
        
        // Мои тейкдауны
        double myTakedownsSuccessful = allRounds.stream().mapToDouble(FightRound::getMyTakedownsSuccessful).sum();
        double myTakedownsAttempted = allRounds.stream().mapToDouble(FightRound::getMyTakedownsAttempted).sum();
        
        // Тейкдауны соперника
        double opponentTakedownsSuccessful = allRounds.stream().mapToDouble(FightRound::getOpponentTakedownsSuccessful).sum();
        double opponentTakedownsAttempted = allRounds.stream().mapToDouble(FightRound::getOpponentTakedownsAttempted).sum();
        
        return AdvancedStatisticsDto.TakedownStatisticsDto.builder()
                .myTakedownAccuracy(myTakedownsAttempted > 0 ? myTakedownsSuccessful / myTakedownsAttempted * 100 : 0)
                .myTakedownsSuccessful(myTakedownsSuccessful)
                .myTakedownsAttempted(myTakedownsAttempted)
                .opponentTakedownAccuracy(opponentTakedownsAttempted > 0 ? opponentTakedownsSuccessful / opponentTakedownsAttempted * 100 : 0)
                .opponentTakedownsSuccessful(opponentTakedownsSuccessful)
                .opponentTakedownsAttempted(opponentTakedownsAttempted)
                .avgMyTakedownsPerFight(mmaFights.size() > 0 ? myTakedownsSuccessful / mmaFights.size() : 0)
                .avgOpponentTakedownsPerFight(mmaFights.size() > 0 ? opponentTakedownsSuccessful / mmaFights.size() : 0)
                .build();
    }
    
    /**
     * Расчет статистики по времени контроля
     */
    private AdvancedStatisticsDto.ControlTimeStatisticsDto calculateControlTimeStatistics(List<Fight> fights) {
        List<FightRound> allRounds = fights.stream()
                .flatMap(fight -> fight.getRounds().stream())
                .collect(Collectors.toList());
        
        if (allRounds.isEmpty()) {
            return AdvancedStatisticsDto.ControlTimeStatisticsDto.builder()
                    .avgMyControlTimeSeconds(0.0)
                    .avgOpponentControlTimeSeconds(0.0)
                    .totalMyControlTimeSeconds(0.0)
                    .totalOpponentControlTimeSeconds(0.0)
                    .avgMyControlTimeFormatted("00:00")
                    .avgOpponentControlTimeFormatted("00:00")
                    .build();
        }
        
        // Парсинг времени контроля
        double totalMyControlTimeSeconds = allRounds.stream()
                .mapToDouble(round -> parseControlTime(round.getMyControlTime()))
                .sum();
        
        double totalOpponentControlTimeSeconds = allRounds.stream()
                .mapToDouble(round -> parseControlTime(round.getOpponentControlTime()))
                .sum();
        
        double avgMyControlTimeSeconds = totalMyControlTimeSeconds / allRounds.size();
        double avgOpponentControlTimeSeconds = totalOpponentControlTimeSeconds / allRounds.size();
        
        return AdvancedStatisticsDto.ControlTimeStatisticsDto.builder()
                .avgMyControlTimeSeconds(avgMyControlTimeSeconds)
                .avgOpponentControlTimeSeconds(avgOpponentControlTimeSeconds)
                .totalMyControlTimeSeconds(totalMyControlTimeSeconds)
                .totalOpponentControlTimeSeconds(totalOpponentControlTimeSeconds)
                .avgMyControlTimeFormatted(formatControlTime(avgMyControlTimeSeconds))
                .avgOpponentControlTimeFormatted(formatControlTime(avgOpponentControlTimeSeconds))
                .build();
    }
    
    /**
     * Расчет статистики по судейским оценкам
     */
    private AdvancedStatisticsDto.JudgeScoreStatisticsDto calculateJudgeScoreStatistics(List<Fight> fights) {
        List<JudgeScore> allJudgeScores = fights.stream()
                .flatMap(fight -> fight.getJudgeScores().stream())
                .collect(Collectors.toList());
        
        if (allJudgeScores.isEmpty()) {
            return AdvancedStatisticsDto.JudgeScoreStatisticsDto.builder()
                    .avgMyScore(0.0)
                    .avgOpponentScore(0.0)
                    .avgScoreDifference(0.0)
                    .fightsWonByJudges(0L)
                    .fightsLostByJudges(0L)
                    .fightsDrawByJudges(0L)
                    .judgeWinRate(0.0)
                    .build();
        }
        
        double totalMyScore = allJudgeScores.stream().mapToDouble(JudgeScore::getMyTotalScore).sum();
        double totalOpponentScore = allJudgeScores.stream().mapToDouble(JudgeScore::getOpponentTotalScore).sum();
        
        long fightsWonByJudges = fights.stream()
                .mapToLong(fight -> {
                    long myWins = fight.getJudgeScores().stream()
                            .mapToLong(score -> "MY_FIGHTER".equals(score.getWinnerByJudge()) ? 1 : 0)
                            .sum();
                    return myWins > fight.getJudgeScores().size() / 2 ? 1 : 0;
                })
                .sum();
        
        long fightsLostByJudges = fights.stream()
                .mapToLong(fight -> {
                    long opponentWins = fight.getJudgeScores().stream()
                            .mapToLong(score -> "OPPONENT".equals(score.getWinnerByJudge()) ? 1 : 0)
                            .sum();
                    return opponentWins > fight.getJudgeScores().size() / 2 ? 1 : 0;
                })
                .sum();
        
        long fightsDrawByJudges = fights.size() - fightsWonByJudges - fightsLostByJudges;
        
        return AdvancedStatisticsDto.JudgeScoreStatisticsDto.builder()
                .avgMyScore(allJudgeScores.size() > 0 ? totalMyScore / allJudgeScores.size() : 0)
                .avgOpponentScore(allJudgeScores.size() > 0 ? totalOpponentScore / allJudgeScores.size() : 0)
                .avgScoreDifference(allJudgeScores.size() > 0 ? (totalMyScore - totalOpponentScore) / allJudgeScores.size() : 0)
                .fightsWonByJudges(fightsWonByJudges)
                .fightsLostByJudges(fightsLostByJudges)
                .fightsDrawByJudges(fightsDrawByJudges)
                .judgeWinRate(fights.size() > 0 ? (double) fightsWonByJudges / fights.size() * 100 : 0)
                .build();
    }
    
    /**
     * Расчет истории рейтинга
     */
    private List<AdvancedStatisticsDto.RatingHistoryDto> calculateRatingHistory(List<Fight> fights) {
        return fights.stream()
                .sorted(Comparator.comparing(Fight::getFightDate))
                .map(fight -> {
                    int previousRating = getPreviousRating(fights, fight);
                    int change = fight.getRatingPoints() - previousRating;
                    
                    return AdvancedStatisticsDto.RatingHistoryDto.builder()
                            .date(fight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                            .rating(fight.getRatingPoints())
                            .change(change)
                            .fightResult(fight.getResult().name())
                            .opponent(fight.getOpponent())
                            .weightClass(fight.getWeightClass() != null ? fight.getWeightClass().name() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Расчет истории места в рейтинге
     */
    private List<AdvancedStatisticsDto.RankingHistoryDto> calculateRankingHistory(List<Fight> fights) {
        return fights.stream()
                .sorted(Comparator.comparing(Fight::getFightDate))
                .map(fight -> {
                    int previousRanking = getPreviousRanking(fights, fight);
                    int change = previousRanking - fight.getRankingPosition(); // Обратный порядок - меньше место = лучше
                    
                    return AdvancedStatisticsDto.RankingHistoryDto.builder()
                            .date(fight.getFightDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                            .ranking(fight.getRankingPosition())
                            .change(change)
                            .fightResult(fight.getResult().name())
                            .opponent(fight.getOpponent())
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Парсинг времени контроля из строки MM:SS
     */
    private double parseControlTime(String controlTime) {
        if (controlTime == null || controlTime.isEmpty()) {
            return 0.0;
        }
        try {
            String[] parts = controlTime.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60 + seconds;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Форматирование времени контроля
     */
    private String formatControlTime(double seconds) {
        int minutes = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d", minutes, secs);
    }
    
    /**
     * Получение предыдущего рейтинга
     */
    private int getPreviousRating(List<Fight> fights, Fight currentFight) {
        return fights.stream()
                .filter(fight -> fight.getFightDate().isBefore(currentFight.getFightDate()))
                .max(Comparator.comparing(Fight::getFightDate))
                .map(Fight::getRatingPoints)
                .orElse(100); // Начальный рейтинг
    }
    
    /**
     * Получение предыдущего места в рейтинге
     */
    private int getPreviousRanking(List<Fight> fights, Fight currentFight) {
        return fights.stream()
                .filter(fight -> fight.getFightDate().isBefore(currentFight.getFightDate()))
                .max(Comparator.comparing(Fight::getFightDate))
                .map(Fight::getRankingPosition)
                .orElse(50); // Начальное место
    }
}
