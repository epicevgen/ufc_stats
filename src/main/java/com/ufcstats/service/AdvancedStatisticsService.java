package com.ufcstats.service;

import com.ufcstats.dto.AdvancedStatisticsDto;
import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.repository.FightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
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
        log.debug("Получение расширенной статистики");
        
        List<Fight> allFights = fightRepository.findAllOrderByFightDateDesc();
        
        return AdvancedStatisticsDto.builder()
                .basic(calculateBasicStatistics(allFights))
                .byFightMode(calculateStatisticsByFightMode(allFights))
                .byWeightClass(calculateStatisticsByWeightClass(allFights))
                .winMethods(calculateWinMethods(allFights))
                .lossMethods(calculateLossMethods(allFights))
                .strikes(calculateStrikeStatistics(allFights))
                .takedowns(calculateTakedownStatistics(allFights))
                .controlTime(calculateControlTimeStatistics(allFights))
                .judgeScores(calculateJudgeScoreStatistics(allFights))
                .ratingHistory(calculateRatingHistory(allFights))
                .rankingHistory(calculateRankingHistory(allFights))
                .build();
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
        return fights.stream()
                .collect(Collectors.groupingBy(fight -> fight.getFightMode().name()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateBasicStatistics(entry.getValue())
                ));
    }
    
    /**
     * Расчет статистики по весовым категориям
     */
    private Map<String, AdvancedStatisticsDto.BasicStatisticsDto> calculateStatisticsByWeightClass(List<Fight> fights) {
        return fights.stream()
                .collect(Collectors.groupingBy(fight -> fight.getWeightClass().name()))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateBasicStatistics(entry.getValue())
                ));
    }
    
    /**
     * Расчет методов побед
     */
    private Map<String, Long> calculateWinMethods(List<Fight> fights) {
        return fights.stream()
                .filter(fight -> fight.getResult() == FightResult.WIN)
                .collect(Collectors.groupingBy(
                        fight -> fight.getMethod().name(),
                        Collectors.counting()
                ));
    }
    
    /**
     * Расчет методов поражений
     */
    private Map<String, Long> calculateLossMethods(List<Fight> fights) {
        return fights.stream()
                .filter(fight -> fight.getResult() == FightResult.LOSS)
                .collect(Collectors.groupingBy(
                        fight -> fight.getMethod().name(),
                        Collectors.counting()
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
            return AdvancedStatisticsDto.StrikeStatisticsDto.builder().build();
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
     * Расчет статистики по тейкдаунам
     */
    private AdvancedStatisticsDto.TakedownStatisticsDto calculateTakedownStatistics(List<Fight> fights) {
        List<FightRound> allRounds = fights.stream()
                .flatMap(fight -> fight.getRounds().stream())
                .collect(Collectors.toList());
        
        if (allRounds.isEmpty()) {
            return AdvancedStatisticsDto.TakedownStatisticsDto.builder().build();
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
                .avgMyTakedownsPerFight(fights.size() > 0 ? myTakedownsSuccessful / fights.size() : 0)
                .avgOpponentTakedownsPerFight(fights.size() > 0 ? opponentTakedownsSuccessful / fights.size() : 0)
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
            return AdvancedStatisticsDto.ControlTimeStatisticsDto.builder().build();
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
            return AdvancedStatisticsDto.JudgeScoreStatisticsDto.builder().build();
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
