package com.ufcstats.model;

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
    private Integer myHeadDamage = 0;

    @Column(name = "my_body_damage", nullable = false)
    @Min(value = 0, message = "Повреждения корпуса не могут быть отрицательными")
    @NotNull(message = "Повреждения корпуса обязательны")
    private Integer myBodyDamage = 0;

    @Column(name = "my_leg_damage", nullable = false)
    @Min(value = 0, message = "Повреждения ног не могут быть отрицательными")
    @NotNull(message = "Повреждения ног обязательны")
    private Integer myLegDamage = 0;

    @Column(name = "my_knockdowns", nullable = false)
    @Min(value = 0, message = "Нокдауны не могут быть отрицательными")
    @NotNull(message = "Нокдауны обязательны")
    private Integer myKnockdowns = 0;

    @Column(name = "my_significant_strikes_landed", nullable = false)
    @Min(value = 0, message = "Значимые удары не могут быть отрицательными")
    @NotNull(message = "Значимые удары обязательны")
    private Integer mySignificantStrikesLanded = 0;

    @Column(name = "my_significant_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки значимых ударов не могут быть отрицательными")
    @NotNull(message = "Попытки значимых ударов обязательны")
    private Integer mySignificantStrikesAttempted = 0;

    @Column(name = "my_total_strikes_landed", nullable = false)
    @Min(value = 0, message = "Все удары не могут быть отрицательными")
    @NotNull(message = "Все удары обязательны")
    private Integer myTotalStrikesLanded = 0;

    @Column(name = "my_total_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки всех ударов не могут быть отрицательными")
    @NotNull(message = "Попытки всех ударов обязательны")
    private Integer myTotalStrikesAttempted = 0;

    @Column(name = "my_takedowns_successful", nullable = false)
    @Min(value = 0, message = "Успешные тейкдауны не могут быть отрицательными")
    @NotNull(message = "Успешные тейкдауны обязательны")
    private Integer myTakedownsSuccessful = 0;

    @Column(name = "my_takedowns_attempted", nullable = false)
    @Min(value = 0, message = "Попытки тейкдаунов не могут быть отрицательными")
    @NotNull(message = "Попытки тейкдаунов обязательны")
    private Integer myTakedownsAttempted = 0;

    @Column(name = "my_control_time", length = 8)
    @Pattern(regexp = "^([0-5]?[0-9]):([0-5][0-9])$", message = "Время контроля должно быть в формате ММ:СС")
    private String myControlTime = "00:00";

    // Статистика соперника
    @Column(name = "opponent_head_damage", nullable = false)
    @Min(value = 0, message = "Повреждения головы не могут быть отрицательными")
    @NotNull(message = "Повреждения головы обязательны")
    private Integer opponentHeadDamage = 0;

    @Column(name = "opponent_body_damage", nullable = false)
    @Min(value = 0, message = "Повреждения корпуса не могут быть отрицательными")
    @NotNull(message = "Повреждения корпуса обязательны")
    private Integer opponentBodyDamage = 0;

    @Column(name = "opponent_leg_damage", nullable = false)
    @Min(value = 0, message = "Повреждения ног не могут быть отрицательными")
    @NotNull(message = "Повреждения ног обязательны")
    private Integer opponentLegDamage = 0;

    @Column(name = "opponent_knockdowns", nullable = false)
    @Min(value = 0, message = "Нокдауны не могут быть отрицательными")
    @NotNull(message = "Нокдауны обязательны")
    private Integer opponentKnockdowns = 0;

    @Column(name = "opponent_significant_strikes_landed", nullable = false)
    @Min(value = 0, message = "Значимые удары не могут быть отрицательными")
    @NotNull(message = "Значимые удары обязательны")
    private Integer opponentSignificantStrikesLanded = 0;

    @Column(name = "opponent_significant_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки значимых ударов не могут быть отрицательными")
    @NotNull(message = "Попытки значимых ударов обязательны")
    private Integer opponentSignificantStrikesAttempted = 0;

    @Column(name = "opponent_total_strikes_landed", nullable = false)
    @Min(value = 0, message = "Все удары не могут быть отрицательными")
    @NotNull(message = "Все удары обязательны")
    private Integer opponentTotalStrikesLanded = 0;

    @Column(name = "opponent_total_strikes_attempted", nullable = false)
    @Min(value = 0, message = "Попытки всех ударов не могут быть отрицательными")
    @NotNull(message = "Попытки всех ударов обязательны")
    private Integer opponentTotalStrikesAttempted = 0;

    @Column(name = "opponent_takedowns_successful", nullable = false)
    @Min(value = 0, message = "Успешные тейкдауны не могут быть отрицательными")
    @NotNull(message = "Успешные тейкдауны обязательны")
    private Integer opponentTakedownsSuccessful = 0;

    @Column(name = "opponent_takedowns_attempted", nullable = false)
    @Min(value = 0, message = "Попытки тейкдаунов не могут быть отрицательными")
    @NotNull(message = "Попытки тейкдаунов обязательны")
    private Integer opponentTakedownsAttempted = 0;

    @Column(name = "opponent_control_time", length = 8)
    @Pattern(regexp = "^([0-5]?[0-9]):([0-5][0-9])$", message = "Время контроля должно быть в формате ММ:СС")
    private String opponentControlTime = "00:00";

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
