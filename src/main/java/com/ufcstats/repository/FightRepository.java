package com.ufcstats.repository;

import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.WeightClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с боями
 */
@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {

    /**
     * Найти все бои, отсортированные по дате (новые сначала)
     */
    @Query("SELECT f FROM Fight f ORDER BY f.fightDate DESC")
    List<Fight> findAllOrderByFightDateDesc();

    /**
     * Найти все бои с пагинацией, отсортированные по дате (новые сначала)
     */
    @Query("SELECT f FROM Fight f ORDER BY f.fightDate DESC")
    Page<Fight> findAllOrderByFightDateDesc(Pageable pageable);

    /**
     * Найти бои по результату
     */
    List<Fight> findByResult(FightResult result);

    /**
     * Найти бои по режиму
     */
    List<Fight> findByFightMode(FightMode fightMode);

    /**
     * Найти бои по весовой категории
     */
    List<Fight> findByWeightClass(WeightClass weightClass);

    /**
     * Найти бои по сезону
     */
    List<Fight> findBySeason(Integer season);

    /**
     * Найти бои по имени бойца (мой боец)
     */
    @Query("SELECT f FROM Fight f WHERE LOWER(f.myFighter) LIKE LOWER(CONCAT('%', :fighterName, '%'))")
    List<Fight> findByMyFighterContainingIgnoreCase(@Param("fighterName") String fighterName);

    /**
     * Найти бои по имени соперника
     */
    @Query("SELECT f FROM Fight f WHERE LOWER(f.opponent) LIKE LOWER(CONCAT('%', :opponentName, '%'))")
    List<Fight> findByOpponentContainingIgnoreCase(@Param("opponentName") String opponentName);

    /**
     * Найти бои в диапазоне дат
     */
    @Query("SELECT f FROM Fight f WHERE f.fightDate BETWEEN :startDate AND :endDate ORDER BY f.fightDate DESC")
    List<Fight> findByFightDateBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Найти последний бой
     */
    @Query("SELECT f FROM Fight f ORDER BY f.fightDate DESC LIMIT 1")
    Optional<Fight> findLatestFight();

    /**
     * Найти все уникальные имена бойцов (мой боец)
     */
    @Query("SELECT DISTINCT f.myFighter FROM Fight f ORDER BY f.myFighter")
    List<String> findDistinctMyFighters();

    /**
     * Найти все уникальные имена соперников
     */
    @Query("SELECT DISTINCT f.opponent FROM Fight f ORDER BY f.opponent")
    List<String> findDistinctOpponents();

    /**
     * Найти все уникальные сезоны
     */
    @Query("SELECT DISTINCT f.season FROM Fight f ORDER BY f.season DESC")
    List<Integer> findDistinctSeasons();

    /**
     * Подсчитать общее количество боев
     */
    @Query("SELECT COUNT(f) FROM Fight f")
    long countAllFights();

    /**
     * Подсчитать количество побед
     */
    @Query("SELECT COUNT(f) FROM Fight f WHERE f.result = 'WIN'")
    long countWins();

    /**
     * Подсчитать количество поражений
     */
    @Query("SELECT COUNT(f) FROM Fight f WHERE f.result = 'LOSS'")
    long countLosses();

    /**
     * Подсчитать количество ничьих
     */
    @Query("SELECT COUNT(f) FROM Fight f WHERE f.result = 'DRAW'")
    long countDraws();

    /**
     * Найти бои с рейтингом (не null)
     */
    @Query("SELECT f FROM Fight f WHERE f.ratingPoints IS NOT NULL ORDER BY f.ratingPoints DESC")
    List<Fight> findFightsWithRating();

    /**
     * Найти бои с позицией в рейтинге (не null)
     */
    @Query("SELECT f FROM Fight f WHERE f.rankingPosition IS NOT NULL ORDER BY f.rankingPosition ASC")
    List<Fight> findFightsWithRanking();

    /**
     * Найти лучший рейтинг
     */
    @Query("SELECT MAX(f.ratingPoints) FROM Fight f WHERE f.ratingPoints IS NOT NULL")
    Optional<Integer> findMaxRating();

    /**
     * Найти лучшую позицию в рейтинге
     */
    @Query("SELECT MIN(f.rankingPosition) FROM Fight f WHERE f.rankingPosition IS NOT NULL")
    Optional<Integer> findBestRanking();

    /**
     * Найти статистику по весовым категориям
     */
    @Query("SELECT f.weightClass, COUNT(f) FROM Fight f GROUP BY f.weightClass ORDER BY COUNT(f) DESC")
    List<Object[]> findFightCountByWeightClass();

    /**
     * Найти статистику по режимам боя
     */
    @Query("SELECT f.fightMode, COUNT(f) FROM Fight f GROUP BY f.fightMode ORDER BY COUNT(f) DESC")
    List<Object[]> findFightCountByMode();

    /**
     * Найти статистику по методам завершения
     */
    @Query("SELECT f.method, COUNT(f) FROM Fight f GROUP BY f.method ORDER BY COUNT(f) DESC")
    List<Object[]> findFightCountByMethod();
}
