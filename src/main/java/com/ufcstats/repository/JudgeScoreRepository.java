package com.ufcstats.repository;

import com.ufcstats.model.JudgeScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с судейскими оценками
 */
@Repository
public interface JudgeScoreRepository extends JpaRepository<JudgeScore, Long> {

    /**
     * Найти все оценки для конкретного боя
     */
    List<JudgeScore> findByFightIdOrderByJudgeNumber(Long fightId);

    /**
     * Найти оценку конкретного судьи для боя
     */
    Optional<JudgeScore> findByFightIdAndJudgeNumber(Long fightId, Integer judgeNumber);

    /**
     * Подсчитать количество судей для боя
     */
    @Query("SELECT COUNT(js) FROM JudgeScore js WHERE js.fight.id = :fightId")
    long countJudgesByFightId(@Param("fightId") Long fightId);

    /**
     * Найти бои с единогласным решением (все судьи за одного бойца)
     */
    @Query("SELECT js.fight.id FROM JudgeScore js " +
           "WHERE js.fight.id IN (" +
           "    SELECT f.id FROM Fight f WHERE f.method = 'DECISION'" +
           ") " +
           "GROUP BY js.fight.id " +
           "HAVING COUNT(DISTINCT js.winnerByJudge) = 1")
    List<Long> findFightsWithUnanimousDecision();

    /**
     * Найти бои с разделенным решением (судьи разделились)
     */
    @Query("SELECT js.fight.id FROM JudgeScore js " +
           "WHERE js.fight.id IN (" +
           "    SELECT f.id FROM Fight f WHERE f.method = 'DECISION'" +
           ") " +
           "GROUP BY js.fight.id " +
           "HAVING COUNT(DISTINCT js.winnerByJudge) > 1")
    List<Long> findFightsWithSplitDecision();

    /**
     * Найти бои с большинством голосов (2 из 3 судей)
     */
    @Query("SELECT js.fight.id FROM JudgeScore js " +
           "WHERE js.fight.id IN (" +
           "    SELECT f.id FROM Fight f WHERE f.method = 'DECISION'" +
           ") " +
           "GROUP BY js.fight.id " +
           "HAVING COUNT(CASE WHEN js.winnerByJudge = 'MY_FIGHTER' THEN 1 END) = 2 " +
           "   OR COUNT(CASE WHEN js.winnerByJudge = 'OPPONENT' THEN 1 END) = 2")
    List<Long> findFightsWithMajorityDecision();

    /**
     * Найти средние оценки по раундам (мой боец)
     */
    @Query("SELECT " +
           "AVG(js.round1MyScore), " +
           "AVG(js.round2MyScore), " +
           "AVG(js.round3MyScore), " +
           "AVG(js.round4MyScore), " +
           "AVG(js.round5MyScore) " +
           "FROM JudgeScore js")
    Object[] getMyAverageScoresByRound();

    /**
     * Найти средние оценки по раундам (соперник)
     */
    @Query("SELECT " +
           "AVG(js.round1OpponentScore), " +
           "AVG(js.round2OpponentScore), " +
           "AVG(js.round3OpponentScore), " +
           "AVG(js.round4OpponentScore), " +
           "AVG(js.round5OpponentScore) " +
           "FROM JudgeScore js")
    Object[] getOpponentAverageScoresByRound();

    /**
     * Найти лучшие оценки по раундам (мой боец)
     */
    @Query("SELECT " +
           "MAX(js.round1MyScore), " +
           "MAX(js.round2MyScore), " +
           "MAX(js.round3MyScore), " +
           "MAX(js.round4MyScore), " +
           "MAX(js.round5MyScore) " +
           "FROM JudgeScore js")
    Object[] getMyBestScoresByRound();

    /**
     * Найти лучшие оценки по раундам (соперник)
     */
    @Query("SELECT " +
           "MAX(js.round1OpponentScore), " +
           "MAX(js.round2OpponentScore), " +
           "MAX(js.round3OpponentScore), " +
           "MAX(js.round4OpponentScore), " +
           "MAX(js.round5OpponentScore) " +
           "FROM JudgeScore js")
    Object[] getOpponentBestScoresByRound();

    /**
     * Найти бои с максимальными общими оценками (мой боец)
     */
    @Query("SELECT js FROM JudgeScore js ORDER BY " +
           "(js.round1MyScore + js.round2MyScore + js.round3MyScore + " +
           "js.round4MyScore + js.round5MyScore) DESC")
    List<JudgeScore> findTopScoresByMyTotal();

    /**
     * Найти бои с максимальными общими оценками (соперник)
     */
    @Query("SELECT js FROM JudgeScore js ORDER BY " +
           "(js.round1OpponentScore + js.round2OpponentScore + js.round3OpponentScore + " +
           "js.round4OpponentScore + js.round5OpponentScore) DESC")
    List<JudgeScore> findTopScoresByOpponentTotal();

    /**
     * Найти статистику по судьям (кто чаще голосует за победу моего бойца)
     */
    @Query("SELECT js.judgeNumber, " +
           "COUNT(CASE WHEN js.winnerByJudge = 'MY_FIGHTER' THEN 1 END) as myWins, " +
           "COUNT(CASE WHEN js.winnerByJudge = 'OPPONENT' THEN 1 END) as opponentWins, " +
           "COUNT(CASE WHEN js.winnerByJudge = 'DRAW' THEN 1 END) as draws " +
           "FROM JudgeScore js " +
           "GROUP BY js.judgeNumber " +
           "ORDER BY js.judgeNumber")
    List<Object[]> getJudgeStatistics();

    /**
     * Найти бои с ничьими по судейским оценкам
     */
    @Query("SELECT js.fight.id FROM JudgeScore js " +
           "WHERE js.fight.id IN (" +
           "    SELECT f.id FROM Fight f WHERE f.method = 'DECISION'" +
           ") " +
           "GROUP BY js.fight.id " +
           "HAVING COUNT(CASE WHEN js.winnerByJudge = 'DRAW' THEN 1 END) >= 2")
    List<Long> findFightsWithDrawDecision();
}
