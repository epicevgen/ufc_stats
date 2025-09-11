package com.ufcstats.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Судейские оценки за бой
 */
@Entity
@Table(name = "judge_scores")
@Data
@EqualsAndHashCode(exclude = "fight")
@ToString(exclude = "fight")
public class JudgeScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fight_id", nullable = false)
    @JsonBackReference
    private Fight fight;

    @Column(name = "judge_number", nullable = false)
    @Min(value = 1, message = "Номер судьи должен быть от 1 до 3")
    @Max(value = 3, message = "Номер судьи должен быть от 1 до 3")
    @NotNull(message = "Номер судьи обязателен")
    private Integer judgeNumber;

    // Оценки за раунды (1-5)
    @Column(name = "round_1_my_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round1MyScore = 0;

    @Column(name = "round_1_opponent_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round1OpponentScore = 0;

    @Column(name = "round_2_my_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round2MyScore = 0;

    @Column(name = "round_2_opponent_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round2OpponentScore = 0;

    @Column(name = "round_3_my_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round3MyScore = 0;

    @Column(name = "round_3_opponent_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round3OpponentScore = 0;

    @Column(name = "round_4_my_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round4MyScore = 0;

    @Column(name = "round_4_opponent_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round4OpponentScore = 0;

    @Column(name = "round_5_my_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round5MyScore = 0;

    @Column(name = "round_5_opponent_score")
    @Min(value = 0, message = "Оценка не может быть отрицательной")
    @Max(value = 10, message = "Оценка не может быть больше 10")
    private Integer round5OpponentScore = 0;

    // Вспомогательные методы для получения оценок по раундам
    public Integer getMyScoreForRound(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> round1MyScore;
            case 2 -> round2MyScore;
            case 3 -> round3MyScore;
            case 4 -> round4MyScore;
            case 5 -> round5MyScore;
            default -> 0;
        };
    }

    public Integer getOpponentScoreForRound(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> round1OpponentScore;
            case 2 -> round2OpponentScore;
            case 3 -> round3OpponentScore;
            case 4 -> round4OpponentScore;
            case 5 -> round5OpponentScore;
            default -> 0;
        };
    }

    public void setMyScoreForRound(int roundNumber, Integer score) {
        switch (roundNumber) {
            case 1 -> this.round1MyScore = score;
            case 2 -> this.round2MyScore = score;
            case 3 -> this.round3MyScore = score;
            case 4 -> this.round4MyScore = score;
            case 5 -> this.round5MyScore = score;
        }
    }

    public void setOpponentScoreForRound(int roundNumber, Integer score) {
        switch (roundNumber) {
            case 1 -> this.round1OpponentScore = score;
            case 2 -> this.round2OpponentScore = score;
            case 3 -> this.round3OpponentScore = score;
            case 4 -> this.round4OpponentScore = score;
            case 5 -> this.round5OpponentScore = score;
        }
    }

    // Методы для расчета общих оценок
    public Integer getMyTotalScore() {
        return (round1MyScore != null ? round1MyScore : 0) +
               (round2MyScore != null ? round2MyScore : 0) +
               (round3MyScore != null ? round3MyScore : 0) +
               (round4MyScore != null ? round4MyScore : 0) +
               (round5MyScore != null ? round5MyScore : 0);
    }

    public Integer getOpponentTotalScore() {
        return (round1OpponentScore != null ? round1OpponentScore : 0) +
               (round2OpponentScore != null ? round2OpponentScore : 0) +
               (round3OpponentScore != null ? round3OpponentScore : 0) +
               (round4OpponentScore != null ? round4OpponentScore : 0) +
               (round5OpponentScore != null ? round5OpponentScore : 0);
    }

    // Метод для определения победителя по оценкам этого судьи
    public String getWinnerByJudge() {
        int myTotal = getMyTotalScore();
        int opponentTotal = getOpponentTotalScore();
        
        if (myTotal > opponentTotal) {
            return "MY_FIGHTER";
        } else if (opponentTotal > myTotal) {
            return "OPPONENT";
        } else {
            return "DRAW";
        }
    }
}
