package com.ufcstats.repository;

import com.ufcstats.model.FightRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с раундами боев
 */
@Repository
public interface FightRoundRepository extends JpaRepository<FightRound, Long> {

    /**
     * Найти все раунды для конкретного боя
     */
    List<FightRound> findByFightIdOrderByRoundNumber(Long fightId);

    /**
     * Найти конкретный раунд боя
     */
    Optional<FightRound> findByFightIdAndRoundNumber(Long fightId, Integer roundNumber);

    /**
     * Подсчитать количество раундов для боя
     */
    @Query("SELECT COUNT(fr) FROM FightRound fr WHERE fr.fight.id = :fightId")
    long countRoundsByFightId(@Param("fightId") Long fightId);

    /**
     * Найти раунды с наибольшим количеством значимых ударов (мой боец)
     */
    @Query("SELECT fr FROM FightRound fr ORDER BY fr.mySignificantStrikesLanded DESC")
    List<FightRound> findTopRoundsByMySignificantStrikes();

    /**
     * Найти раунды с наибольшим количеством значимых ударов (соперник)
     */
    @Query("SELECT fr FROM FightRound fr ORDER BY fr.opponentSignificantStrikesLanded DESC")
    List<FightRound> findTopRoundsByOpponentSignificantStrikes();

    /**
     * Найти раунды с нокдаунами (мой боец)
     */
    @Query("SELECT fr FROM FightRound fr WHERE fr.myKnockdowns > 0 ORDER BY fr.myKnockdowns DESC")
    List<FightRound> findRoundsWithMyKnockdowns();

    /**
     * Найти раунды с нокдаунами (соперник)
     */
    @Query("SELECT fr FROM FightRound fr WHERE fr.opponentKnockdowns > 0 ORDER BY fr.opponentKnockdowns DESC")
    List<FightRound> findRoundsWithOpponentKnockdowns();

    /**
     * Найти раунды с тейкдаунами (мой боец)
     */
    @Query("SELECT fr FROM FightRound fr WHERE fr.myTakedownsSuccessful > 0 ORDER BY fr.myTakedownsSuccessful DESC")
    List<FightRound> findRoundsWithMyTakedowns();

    /**
     * Найти раунды с тейкдаунами (соперник)
     */
    @Query("SELECT fr FROM FightRound fr WHERE fr.opponentTakedownsSuccessful > 0 ORDER BY fr.opponentTakedownsSuccessful DESC")
    List<FightRound> findRoundsWithOpponentTakedowns();

    /**
     * Подсчитать общую статистику по всем раундам (мой боец)
     */
    @Query("SELECT " +
           "SUM(fr.myHeadDamage), " +
           "SUM(fr.myBodyDamage), " +
           "SUM(fr.myLegDamage), " +
           "SUM(fr.myKnockdowns), " +
           "SUM(fr.mySignificantStrikesLanded), " +
           "SUM(fr.mySignificantStrikesAttempted), " +
           "SUM(fr.myTotalStrikesLanded), " +
           "SUM(fr.myTotalStrikesAttempted), " +
           "SUM(fr.myTakedownsSuccessful), " +
           "SUM(fr.myTakedownsAttempted) " +
           "FROM FightRound fr WHERE fr.fight.id = :fightId")
    Object[] getMyTotalStatsByFightId(@Param("fightId") Long fightId);

    /**
     * Подсчитать общую статистику по всем раундам (соперник)
     */
    @Query("SELECT " +
           "SUM(fr.opponentHeadDamage), " +
           "SUM(fr.opponentBodyDamage), " +
           "SUM(fr.opponentLegDamage), " +
           "SUM(fr.opponentKnockdowns), " +
           "SUM(fr.opponentSignificantStrikesLanded), " +
           "SUM(fr.opponentSignificantStrikesAttempted), " +
           "SUM(fr.opponentTotalStrikesLanded), " +
           "SUM(fr.opponentTotalStrikesAttempted), " +
           "SUM(fr.opponentTakedownsSuccessful), " +
           "SUM(fr.opponentTakedownsAttempted) " +
           "FROM FightRound fr WHERE fr.fight.id = :fightId")
    Object[] getOpponentTotalStatsByFightId(@Param("fightId") Long fightId);

    /**
     * Найти среднюю статистику по всем боям (мой боец)
     */
    @Query("SELECT " +
           "AVG(fr.myHeadDamage), " +
           "AVG(fr.myBodyDamage), " +
           "AVG(fr.myLegDamage), " +
           "AVG(fr.myKnockdowns), " +
           "AVG(fr.mySignificantStrikesLanded), " +
           "AVG(fr.mySignificantStrikesAttempted), " +
           "AVG(fr.myTotalStrikesLanded), " +
           "AVG(fr.myTotalStrikesAttempted), " +
           "AVG(fr.myTakedownsSuccessful), " +
           "AVG(fr.myTakedownsAttempted) " +
           "FROM FightRound fr")
    Object[] getMyAverageStats();

    /**
     * Найти среднюю статистику по всем боям (соперник)
     */
    @Query("SELECT " +
           "AVG(fr.opponentHeadDamage), " +
           "AVG(fr.opponentBodyDamage), " +
           "AVG(fr.opponentLegDamage), " +
           "AVG(fr.opponentKnockdowns), " +
           "AVG(fr.opponentSignificantStrikesLanded), " +
           "AVG(fr.opponentSignificantStrikesAttempted), " +
           "AVG(fr.opponentTotalStrikesLanded), " +
           "AVG(fr.opponentTotalStrikesAttempted), " +
           "AVG(fr.opponentTakedownsSuccessful), " +
           "AVG(fr.opponentTakedownsAttempted) " +
           "FROM FightRound fr")
    Object[] getOpponentAverageStats();
}
