package com.ufcstats.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ufcstats.model.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель боя в UFC 5
 */
@Entity
@Table(name = "fights")
@Data
@EqualsAndHashCode(exclude = {"rounds", "judgeScores"})
@ToString(exclude = {"rounds", "judgeScores"})
public class Fight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Общая информация о бое
    @Column(name = "fight_date", nullable = false)
    // @NotNull(message = "Дата боя обязательна") // Временно отключено для тестирования
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fightDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "fight_mode", nullable = false)
    @NotNull(message = "Режим боя обязателен")
    private FightMode fightMode;

    @Column(name = "season", nullable = false)
    @Min(value = 1, message = "Сезон должен быть больше 0")
    @NotNull(message = "Сезон обязателен")
    private Integer season;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    @NotNull(message = "Результат боя обязателен")
    private FightResult result;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    @NotNull(message = "Метод завершения боя обязателен")
    private FightMethod method;

    @Column(name = "rounds_played", nullable = false)
    @Min(value = 1, message = "Количество раундов должно быть больше 0")
    @Max(value = 5, message = "Количество раундов не может быть больше 5")
    @NotNull(message = "Количество раундов обязательно")
    private Integer roundsPlayed;

    @Column(name = "rating_points")
    @Min(value = 0, message = "Очки рейтинга не могут быть отрицательными")
    private Integer ratingPoints;

    @Column(name = "ranking_position")
    @Min(value = 1, message = "Место в рейтинге должно быть больше 0")
    private Integer rankingPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_class", nullable = false)
    @NotNull(message = "Весовая категория обязательна")
    private WeightClass weightClass;

    @Column(name = "my_fighter", nullable = false, length = 100)
    @NotBlank(message = "Имя вашего бойца обязательно")
    @Size(max = 100, message = "Имя бойца не может быть длиннее 100 символов")
    private String myFighter;

    @Column(name = "opponent", nullable = false, length = 100)
    @NotBlank(message = "Имя соперника обязательно")
    @Size(max = 100, message = "Имя соперника не может быть длиннее 100 символов")
    private String opponent;

    // Примечания
    @Column(name = "notes", columnDefinition = "TEXT")
    @Size(max = 2000, message = "Примечания не могут быть длиннее 2000 символов")
    private String notes;

    // Связи
    @OneToMany(mappedBy = "fight", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("roundNumber ASC")
    @JsonManagedReference
    private List<FightRound> rounds = new ArrayList<>();

    @OneToMany(mappedBy = "fight", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<JudgeScore> judgeScores = new ArrayList<>();

    // Метаданные
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Вспомогательные методы
    public void addRound(FightRound round) {
        rounds.add(round);
        round.setFight(this);
    }

    public void removeRound(FightRound round) {
        rounds.remove(round);
        round.setFight(null);
    }

    public void addJudgeScore(JudgeScore judgeScore) {
        judgeScores.add(judgeScore);
        judgeScore.setFight(this);
    }

    public void removeJudgeScore(JudgeScore judgeScore) {
        judgeScores.remove(judgeScore);
        judgeScore.setFight(null);
    }

    // Метод для получения статистики по всем раундам
    public FightRound getRoundByNumber(int roundNumber) {
        return rounds.stream()
                .filter(round -> round.getRoundNumber().equals(roundNumber))
                .findFirst()
                .orElse(null);
    }
}
