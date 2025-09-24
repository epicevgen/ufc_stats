package com.ufcstats.util;

import com.ufcstats.model.JudgeScore;
import java.util.List;

/**
 * Утилитный класс для работы с судейскими оценками
 */
public class JudgeScoreUtil {

    /**
     * Проверяет, есть ли хотя бы одна заполненная оценка в списке судейских оценок
     */
    public static boolean hasAnyScores(List<JudgeScore> judgeScores) {
        if (judgeScores == null || judgeScores.isEmpty()) {
            return false;
        }
        
        for (JudgeScore judge : judgeScores) {
            if (hasAnyRoundScore(judge)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Проверяет, есть ли заполненные оценки для конкретного раунда
     */
    public static boolean hasRoundScore(List<JudgeScore> judgeScores, int roundNumber) {
        if (judgeScores == null || judgeScores.isEmpty()) {
            return false;
        }
        
        for (JudgeScore judge : judgeScores) {
            if (hasRoundScore(judge, roundNumber)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Проверяет, есть ли хотя бы одна заполненная оценка у конкретного судьи
     */
    private static boolean hasAnyRoundScore(JudgeScore judge) {
        if (judge == null) {
            return false;
        }
        
        return hasRoundScore(judge, 1) || hasRoundScore(judge, 2) || 
               hasRoundScore(judge, 3) || hasRoundScore(judge, 4) || 
               hasRoundScore(judge, 5);
    }

    /**
     * Проверяет, есть ли заполненные оценки для конкретного раунда у конкретного судьи
     */
    private static boolean hasRoundScore(JudgeScore judge, int roundNumber) {
        if (judge == null) {
            return false;
        }
        
        switch (roundNumber) {
            case 1:
                return judge.getRound1MyScore() != null && judge.getRound1OpponentScore() != null;
            case 2:
                return judge.getRound2MyScore() != null && judge.getRound2OpponentScore() != null;
            case 3:
                return judge.getRound3MyScore() != null && judge.getRound3OpponentScore() != null;
            case 4:
                return judge.getRound4MyScore() != null && judge.getRound4OpponentScore() != null;
            case 5:
                return judge.getRound5MyScore() != null && judge.getRound5OpponentScore() != null;
            default:
                return false;
        }
    }
}
