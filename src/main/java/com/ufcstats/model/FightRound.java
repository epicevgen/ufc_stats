package com.ufcstats.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Статистика раунда боя
 */
@Entity
@Table(name = "fight_rounds")
@Data
@EqualsAndHashCode(exclude = "fight")
@ToString(exclude = "fight")
public class FightRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fight_id", nullable = false)
    @JsonBackReference
    private Fight fight;

    @Column(name = "round_number", nullable = false)
    @Min(value = 1, message = "Номер раунда должен быть больше 0")
    @Max(value = 5, message = "Номер раунда не может быть больше 5")
    @NotNull(message = "Номер раунда обязателен")
    private Integer roundNumber;

    // Статистика моего бойца
    @Column(name = "my_head_damage", nullable = false)
    @Min(value = 0, message = "Повреждения головы не могут быть отрицательными")
    @NotNull(message = "Повреждения головы обязательны")
    private Integer myHeadDamage;

    @Column(name = "my_body_damage", nullable = false)
    @Min(value = 0, message = "Повреждения корпуса не могут быть отрицательными")
    @NotNull(message = "Повреждения корпуса обязательны")
    private Integer myBodyDamage;

    @Column(name = "my_leg_damage", nullable = false)
    @Min(value = 0, message = "Повреждения ног не могут быть отрицательными")
    @NotNull(message = "Повреждения ног обязательны")
    private Integer myLegDamage;

    @Column(name = "my_knockdowns", nullable = false)
    @Min(value = 0, message = "Нокдауны не могут быть отрицательными")
    @NotNull(message = "Нокдауны обязательны")
    private Integer myKnockdowns;

    @Column(name = "my_significant_strikes_landed", nullable = false)
    @Min(value = 0, message = "Значимые удары не могут быть отрицательными")
    @NotNull(message = "Значимые удары обязательны")
    private Integer mySignificantStrikesLanded;

    @Column(name = "my_significant_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки значимых ударов не могут быть отрицательными")
    @NotNull(message = "Попытки значимых ударов обязательны")
    private Integer mySignificantStrikesAttempted;

    @Column(name = "my_total_strikes_landed", nullable = false)
    @Min(value = 0, message = "Все удары не могут быть отрицательными")
    @NotNull(message = "Все удары обязательны")
    private Integer myTotalStrikesLanded;

    @Column(name = "my_total_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки всех ударов не могут быть отрицательными")
    @NotNull(message = "Попытки всех ударов обязательны")
    private Integer myTotalStrikesAttempted;

    @Column(name = "my_takedowns_successful", nullable = false)
    @Min(value = 0, message = "Успешные тейкдауны не могут быть отрицательными")
    @NotNull(message = "Успешные тейкдауны обязательны")
    private Integer myTakedownsSuccessful;

    @Column(name = "my_takedowns_attempted", nullable = false)
    @Min(value = 0, message = "Попытки тейкдаунов не могут быть отрицательными")
    @NotNull(message = "Попытки тейкдаунов обязательны")
    private Integer myTakedownsAttempted;

    @Column(name = "my_control_time", length = 8)
    @Pattern(regexp = "^([0-5]?[0-9]):([0-5][0-9])$", message = "Время контроля должно быть в формате ММ:СС")
    private String myControlTime;

    // Статистика соперника
    @Column(name = "opponent_head_damage", nullable = false)
    @Min(value = 0, message = "Повреждения головы не могут быть отрицательными")
    @NotNull(message = "Повреждения головы обязательны")
    private Integer opponentHeadDamage;

    @Column(name = "opponent_body_damage", nullable = false)
    @Min(value = 0, message = "Повреждения корпуса не могут быть отрицательными")
    @NotNull(message = "Повреждения корпуса обязательны")
    private Integer opponentBodyDamage;

    @Column(name = "opponent_leg_damage", nullable = false)
    @Min(value = 0, message = "Повреждения ног не могут быть отрицательными")
    @NotNull(message = "Повреждения ног обязательны")
    private Integer opponentLegDamage;

    @Column(name = "opponent_knockdowns", nullable = false)
    @Min(value = 0, message = "Нокдауны не могут быть отрицательными")
    @NotNull(message = "Нокдауны обязательны")
    private Integer opponentKnockdowns;

    @Column(name = "opponent_significant_strikes_landed", nullable = false)
    @Min(value = 0, message = "Значимые удары не могут быть отрицательными")
    @NotNull(message = "Значимые удары обязательны")
    private Integer opponentSignificantStrikesLanded;

    @Column(name = "opponent_significant_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки значимых ударов не могут быть отрицательными")
    @NotNull(message = "Попытки значимых ударов обязательны")
    private Integer opponentSignificantStrikesAttempted;

    @Column(name = "opponent_total_strikes_landed", nullable = false)
    @Min(value = 0, message = "Все удары не могут быть отрицательными")
    @NotNull(message = "Все удары обязательны")
    private Integer opponentTotalStrikesLanded;

    @Column(name = "opponent_total_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки всех ударов не могут быть отрицательными")
    @NotNull(message = "Попытки всех ударов обязательны")
    private Integer opponentTotalStrikesAttempted;

    @Column(name = "opponent_takedowns_successful", nullable = false)
    @Min(value = 0, message = "Успешные тейкдауны не могут быть отрицательными")
    @NotNull(message = "Успешные тейкдауны обязательны")
    private Integer opponentTakedownsSuccessful;

    @Column(name = "opponent_takedowns_attempted", nullable = false)
    @Min(value = 0, message = "Попытки тейкдаунов не могут быть отрицательными")
    @NotNull(message = "Попытки тейкдаунов обязательны")
    private Integer opponentTakedownsAttempted;

    @Column(name = "opponent_control_time", length = 8)
    @Pattern(regexp = "^([0-5]?[0-9]):([0-5][0-9])$", message = "Время контроля должно быть в формате ММ:СС")
    private String opponentControlTime;

    // Вспомогательные методы для расчета процентов
    public Double getMySignificantStrikesAccuracy() {
        if (mySignificantStrikesAttempted == 0) return 0.0;
        return (double) mySignificantStrikesLanded / mySignificantStrikesAttempted * 100;
    }

    public Double getMyTotalStrikesAccuracy() {
        if (myTotalStrikesAttempted == 0) return 0.0;
        return (double) myTotalStrikesLanded / myTotalStrikesAttempted * 100;
    }

    public Double getMyTakedownAccuracy() {
        if (myTakedownsAttempted == 0) return 0.0;
        return (double) myTakedownsSuccessful / myTakedownsAttempted * 100;
    }

    public Double getOpponentSignificantStrikesAccuracy() {
        if (opponentSignificantStrikesAttempted == 0) return 0.0;
        return (double) opponentSignificantStrikesLanded / opponentSignificantStrikesAttempted * 100;
    }

    public Double getOpponentTotalStrikesAccuracy() {
        if (opponentTotalStrikesAttempted == 0) return 0.0;
        return (double) opponentTotalStrikesLanded / opponentTotalStrikesAttempted * 100;
    }

    public Double getOpponentTakedownAccuracy() {
        if (opponentTakedownsAttempted == 0) return 0.0;
        return (double) opponentTakedownsSuccessful / opponentTakedownsAttempted * 100;
    }
}
