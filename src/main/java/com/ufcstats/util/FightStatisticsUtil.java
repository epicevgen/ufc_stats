package com.ufcstats.util;

import com.ufcstats.model.FightRound;
import java.time.Duration;
import java.util.Collection;

/**
 * Утилитарный класс для расчета статистики боев
 */
public class FightStatisticsUtil {

    /**
     * Рассчитать общий урон по голове моего бойца
     */
    public static int calculateTotalMyHeadDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyHeadDamage() != null ? r.getMyHeadDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общий урон по голове соперника
     */
    public static int calculateTotalOpponentHeadDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentHeadDamage() != null ? r.getOpponentHeadDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общий урон по корпусу моего бойца
     */
    public static int calculateTotalMyBodyDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyBodyDamage() != null ? r.getMyBodyDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общий урон по корпусу соперника
     */
    public static int calculateTotalOpponentBodyDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentBodyDamage() != null ? r.getOpponentBodyDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общий урон по ногам моего бойца
     */
    public static int calculateTotalMyLegDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyLegDamage() != null ? r.getMyLegDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общий урон по ногам соперника
     */
    public static int calculateTotalOpponentLegDamage(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentLegDamage() != null ? r.getOpponentLegDamage() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество значимых ударов моего бойца
     */
    public static int calculateTotalMySignificantStrikesLanded(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMySignificantStrikesLanded() != null ? r.getMySignificantStrikesLanded() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток значимых ударов моего бойца
     */
    public static int calculateTotalMySignificantStrikesAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMySignificantStrikesAttempted() != null ? r.getMySignificantStrikesAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество значимых ударов соперника
     */
    public static int calculateTotalOpponentSignificantStrikesLanded(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentSignificantStrikesLanded() != null ? r.getOpponentSignificantStrikesLanded() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток значимых ударов соперника
     */
    public static int calculateTotalOpponentSignificantStrikesAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentSignificantStrikesAttempted() != null ? r.getOpponentSignificantStrikesAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество ударов моего бойца
     */
    public static int calculateTotalMyTotalStrikesLanded(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyTotalStrikesLanded() != null ? r.getMyTotalStrikesLanded() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток ударов моего бойца
     */
    public static int calculateTotalMyTotalStrikesAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyTotalStrikesAttempted() != null ? r.getMyTotalStrikesAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество ударов соперника
     */
    public static int calculateTotalOpponentTotalStrikesLanded(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentTotalStrikesLanded() != null ? r.getOpponentTotalStrikesLanded() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток ударов соперника
     */
    public static int calculateTotalOpponentTotalStrikesAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentTotalStrikesAttempted() != null ? r.getOpponentTotalStrikesAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество успешных тейкдаунов моего бойца
     */
    public static int calculateTotalMyTakedownsSuccessful(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyTakedownsSuccessful() != null ? r.getMyTakedownsSuccessful() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток тейкдаунов моего бойца
     */
    public static int calculateTotalMyTakedownsAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyTakedownsAttempted() != null ? r.getMyTakedownsAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество успешных тейкдаунов соперника
     */
    public static int calculateTotalOpponentTakedownsSuccessful(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentTakedownsSuccessful() != null ? r.getOpponentTakedownsSuccessful() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество попыток тейкдаунов соперника
     */
    public static int calculateTotalOpponentTakedownsAttempted(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentTakedownsAttempted() != null ? r.getOpponentTakedownsAttempted() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество нокдаунов моего бойца
     */
    public static int calculateTotalMyKnockdowns(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getMyKnockdowns() != null ? r.getMyKnockdowns() : 0)
                .sum();
    }

    /**
     * Рассчитать общее количество нокдаунов соперника
     */
    public static int calculateTotalOpponentKnockdowns(Collection<FightRound> rounds) {
        if (rounds == null) return 0;
        return rounds.stream()
                .mapToInt(r -> r.getOpponentKnockdowns() != null ? r.getOpponentKnockdowns() : 0)
                .sum();
    }

    /**
     * Рассчитать общее время контроля моего бойца
     */
    public static Duration calculateTotalMyControlTime(Collection<FightRound> rounds) {
        if (rounds == null || rounds.isEmpty()) return Duration.ZERO;
        return rounds.stream()
                .filter(r -> r.getMyControlTime() != null)
                .map(r -> parseDuration(r.getMyControlTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Рассчитать общее время контроля соперника
     */
    public static Duration calculateTotalOpponentControlTime(Collection<FightRound> rounds) {
        if (rounds == null || rounds.isEmpty()) return Duration.ZERO;
        return rounds.stream()
                .filter(r -> r.getOpponentControlTime() != null)
                .map(r -> parseDuration(r.getOpponentControlTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Рассчитать общее время контроля моего бойца в формате строки мм:сс
     */
    public static String calculateTotalMyControlTimeString(Collection<FightRound> rounds) {
        Duration total = calculateTotalMyControlTime(rounds);
        return formatDuration(total);
    }

    /**
     * Рассчитать общее время контроля соперника в формате строки мм:сс
     */
    public static String calculateTotalOpponentControlTimeString(Collection<FightRound> rounds) {
        Duration total = calculateTotalOpponentControlTime(rounds);
        return formatDuration(total);
    }

    /**
     * Парсинг строки времени в Duration
     */
    private static Duration parseDuration(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return Duration.ZERO;
        }
        try {
            String[] parts = timeString.split(":");
            if (parts.length == 2) {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return Duration.ofMinutes(minutes).plusSeconds(seconds);
            }
        } catch (NumberFormatException e) {
            // Если не удается распарсить, возвращаем ноль
        }
        return Duration.ZERO;
    }

    /**
     * Сравнение времени контроля (строки в формате мм:сс)
     * Возвращает: положительное число если time1 > time2, отрицательное если time1 < time2, 0 если равны
     */
    public static int compareControlTimes(String time1, String time2) {
        Duration duration1 = parseDuration(time1);
        Duration duration2 = parseDuration(time2);
        return duration1.compareTo(duration2);
    }

    /**
     * Форматирование Duration в строку мм:сс
     */
    private static String formatDuration(Duration duration) {
        if (duration == null || duration.isZero()) {
            return "00:00";
        }
        long totalSeconds = duration.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
