package com.ufcstats.service;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.WeightClass;
import com.ufcstats.repository.FightRepository;
import com.ufcstats.repository.FightRoundRepository;
import com.ufcstats.repository.JudgeScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с боями
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FightService {

    private final FightRepository fightRepository;
    private final FightRoundRepository fightRoundRepository;
    private final JudgeScoreRepository judgeScoreRepository;

    /**
     * Получить все бои с пагинацией
     */
    public Page<Fight> getAllFights(Pageable pageable) {
        log.debug("Получение всех боев с пагинацией: {}", pageable);
        return fightRepository.findAllOrderByFightDateDesc(pageable);
    }

    /**
     * Получить все бои
     */
    public List<Fight> getAllFights() {
        log.debug("Получение всех боев");
        return fightRepository.findAllOrderByFightDateDesc();
    }

    /**
     * Получить бой по ID
     */
    public Optional<Fight> getFightById(Long id) {
        log.debug("Получение боя по ID: {}", id);
        return fightRepository.findById(id);
    }

    /**
     * Создать новый бой
     */
    @Transactional
    public Fight createFight(Fight fight) {
        log.info("Создание нового боя: {} vs {}", fight.getMyFighter(), fight.getOpponent());
        
        // Валидация
        validateFight(fight);
        
        // Создание раундов если они не созданы
        if (fight.getRounds().isEmpty()) {
            createDefaultRounds(fight);
        }
        
        // Создание судейских оценок если они не созданы
        if (fight.getJudgeScores().isEmpty()) {
            createDefaultJudgeScores(fight);
        }
        
        Fight savedFight = fightRepository.save(fight);
        log.info("Бой создан с ID: {}", savedFight.getId());
        return savedFight;
    }

    /**
     * Обновить бой
     */
    @Transactional
    public Fight updateFight(Long id, Fight fightDetails) {
        log.info("Обновление боя с ID: {}", id);
        
        Fight fight = fightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бой не найден с ID: " + id));
        
        // Обновление полей
        fight.setFightDate(fightDetails.getFightDate());
        fight.setFightMode(fightDetails.getFightMode());
        fight.setSeason(fightDetails.getSeason());
        fight.setResult(fightDetails.getResult());
        fight.setMethod(fightDetails.getMethod());
        fight.setRoundsPlayed(fightDetails.getRoundsPlayed());
        fight.setRatingPoints(fightDetails.getRatingPoints());
        fight.setRankingPosition(fightDetails.getRankingPosition());
        fight.setWeightClass(fightDetails.getWeightClass());
        fight.setMyFighter(fightDetails.getMyFighter());
        fight.setOpponent(fightDetails.getOpponent());
        fight.setNotes(fightDetails.getNotes());
        
        // Обновление раундов
        updateRounds(fight, fightDetails.getRounds());
        
        // Обновление судейских оценок
        updateJudgeScores(fight, fightDetails.getJudgeScores());
        
        Fight updatedFight = fightRepository.save(fight);
        log.info("Бой обновлен с ID: {}", updatedFight.getId());
        return updatedFight;
    }

    /**
     * Удалить бой
     */
    @Transactional
    public void deleteFight(Long id) {
        log.info("Удаление боя с ID: {}", id);
        
        Fight fight = fightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бой не найден с ID: " + id));
        
        fightRepository.delete(fight);
        log.info("Бой удален с ID: {}", id);
    }

    /**
     * Поиск боев с фильтрацией и пагинацией
     */
    public Page<Fight> searchFights(String search, String resultFilter, String fightModeFilter, 
                                   String methodFilter, Pageable pageable) {
        log.debug("Поиск боев с фильтрами: search={}, resultFilter={}, fightModeFilter={}, methodFilter={}", 
                 search, resultFilter, fightModeFilter, methodFilter);
        
        // Преобразуем строки в enum
        FightResult resultEnum = null;
        if (resultFilter != null && !resultFilter.trim().isEmpty()) {
            try {
                resultEnum = FightResult.valueOf(resultFilter);
            } catch (IllegalArgumentException e) {
                log.warn("Неверное значение для resultFilter: {}", resultFilter);
            }
        }
        
        FightMode fightModeEnum = null;
        if (fightModeFilter != null && !fightModeFilter.trim().isEmpty()) {
            try {
                fightModeEnum = FightMode.valueOf(fightModeFilter);
            } catch (IllegalArgumentException e) {
                log.warn("Неверное значение для fightModeFilter: {}", fightModeFilter);
            }
        }
        
        com.ufcstats.model.enums.FightMethod methodEnum = null;
        if (methodFilter != null && !methodFilter.trim().isEmpty()) {
            try {
                methodEnum = com.ufcstats.model.enums.FightMethod.valueOf(methodFilter);
            } catch (IllegalArgumentException e) {
                log.warn("Неверное значение для methodFilter: {}", methodFilter);
            }
        }
        
        return fightRepository.searchFights(search, resultEnum, fightModeEnum, methodEnum, pageable);
    }

    /**
     * Поиск боев по различным критериям (старый метод для совместимости)
     */
    public List<Fight> searchFights(String myFighter, String opponent, FightResult result, 
                                   FightMode fightMode, WeightClass weightClass, Integer season) {
        log.debug("Поиск боев по критериям: myFighter={}, opponent={}, result={}, fightMode={}, weightClass={}, season={}", 
                 myFighter, opponent, result, fightMode, weightClass, season);
        
        // Здесь можно реализовать более сложную логику поиска
        // Пока используем простые методы репозитория
        if (myFighter != null && !myFighter.trim().isEmpty()) {
            return fightRepository.findByMyFighterContainingIgnoreCase(myFighter);
        }
        if (opponent != null && !opponent.trim().isEmpty()) {
            return fightRepository.findByOpponentContainingIgnoreCase(opponent);
        }
        if (result != null) {
            return fightRepository.findByResult(result);
        }
        if (fightMode != null) {
            return fightRepository.findByFightMode(fightMode);
        }
        if (weightClass != null) {
            return fightRepository.findByWeightClass(weightClass);
        }
        if (season != null) {
            return fightRepository.findBySeason(season);
        }
        
        return getAllFights();
    }

    /**
     * Получить статистику боев
     */
    public FightStatistics getFightStatistics() {
        log.debug("Получение статистики боев");
        
        long totalFights = fightRepository.countAllFights();
        long wins = fightRepository.countWins();
        long losses = fightRepository.countLosses();
        long draws = fightRepository.countDraws();
        
        return FightStatistics.builder()
                .totalFights(totalFights)
                .wins(wins)
                .losses(losses)
                .draws(draws)
                .winRate(totalFights > 0 ? (double) wins / totalFights * 100 : 0.0)
                .build();
    }

    /**
     * Получить уникальные имена бойцов
     */
    public List<String> getDistinctMyFighters() {
        return fightRepository.findDistinctMyFighters();
    }

    /**
     * Получить уникальные имена соперников
     */
    public List<String> getDistinctOpponents() {
        return fightRepository.findDistinctOpponents();
    }

    /**
     * Получить уникальные сезоны
     */
    public List<Integer> getDistinctSeasons() {
        return fightRepository.findDistinctSeasons();
    }

    /**
     * Валидация боя
     */
    private void validateFight(Fight fight) {
        if (fight.getFightDate() == null) {
            throw new IllegalArgumentException("Дата боя обязательна");
        }
        if (fight.getFightDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Дата боя не может быть в будущем");
        }
        if (fight.getRoundsPlayed() == null || fight.getRoundsPlayed() < 1 || fight.getRoundsPlayed() > 5) {
            throw new IllegalArgumentException("Количество раундов должно быть от 1 до 5");
        }
    }

    /**
     * Создание раундов по умолчанию
     */
    private void createDefaultRounds(Fight fight) {
        for (int i = 1; i <= fight.getRoundsPlayed(); i++) {
            FightRound round = new FightRound();
            round.setRoundNumber(i);
            fight.addRound(round);
        }
    }

    /**
     * Создание судейских оценок по умолчанию
     */
    private void createDefaultJudgeScores(Fight fight) {
        for (int i = 1; i <= 3; i++) {
            JudgeScore judgeScore = new JudgeScore();
            judgeScore.setJudgeNumber(i);
            fight.addJudgeScore(judgeScore);
        }
    }

    /**
     * Обновление раундов
     */
    private void updateRounds(Fight fight, List<FightRound> newRounds) {
        // Удаляем старые раунды из базы данных
        List<FightRound> existingRounds = fight.getRounds();
        for (FightRound round : existingRounds) {
            fightRoundRepository.delete(round);
        }
        fight.getRounds().clear();
        
        // Добавляем новые раунды
        if (newRounds != null) {
            for (FightRound round : newRounds) {
                fight.addRound(round);
            }
        }
    }

    /**
     * Обновление судейских оценок
     */
    private void updateJudgeScores(Fight fight, List<JudgeScore> newJudgeScores) {
        // Удаляем старые оценки из базы данных
        List<JudgeScore> existingJudgeScores = fight.getJudgeScores();
        for (JudgeScore judgeScore : existingJudgeScores) {
            judgeScoreRepository.delete(judgeScore);
        }
        fight.getJudgeScores().clear();
        
        // Добавляем новые оценки
        if (newJudgeScores != null) {
            for (JudgeScore judgeScore : newJudgeScores) {
                fight.addJudgeScore(judgeScore);
            }
        }
    }

    /**
     * Класс для статистики боев
     */
    @lombok.Data
    @lombok.Builder
    public static class FightStatistics {
        private long totalFights;
        private long wins;
        private long losses;
        private long draws;
        private double winRate;

        public FightStatistics(long totalFights, long wins, long losses, long draws, double winRate) {
            this.totalFights = totalFights;
            this.wins = wins;
            this.losses = losses;
            this.draws = draws;
            this.winRate = winRate;
        }

        // Getters
        public long getTotalFights() { return totalFights; }
        public long getWins() { return wins; }
        public long getLosses() { return losses; }
        public long getDraws() { return draws; }
        public double getWinRate() { return winRate; }
    }
}
