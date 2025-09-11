package com.ufcstats.controller;

import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.WeightClass;
import com.ufcstats.service.FightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API контроллер для работы с боями
 */
@RestController
@RequestMapping("/api/fights")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FightController {

    private final FightService fightService;

    /**
     * Получить все бои с пагинацией
     */
    @GetMapping
    public ResponseEntity<Page<Fight>> getAllFights(Pageable pageable) {
        log.debug("Получение всех боев с пагинацией: {}", pageable);
        Page<Fight> fights = fightService.getAllFights(pageable);
        return ResponseEntity.ok(fights);
    }

    /**
     * Получить все бои (без пагинации)
     */
    @GetMapping("/all")
    public ResponseEntity<List<Fight>> getAllFights() {
        log.debug("Получение всех боев");
        List<Fight> fights = fightService.getAllFights();
        return ResponseEntity.ok(fights);
    }

    /**
     * Получить бой по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fight> getFightById(@PathVariable Long id) {
        log.debug("Получение боя по ID: {}", id);
        return fightService.getFightById(id)
                .map(fight -> ResponseEntity.ok(fight))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Создать новый бой
     */
    @PostMapping
    public ResponseEntity<Fight> createFight(@Valid @RequestBody Fight fight) {
        log.info("Создание нового боя: {} vs {}", fight.getMyFighter(), fight.getOpponent());
        try {
            Fight createdFight = fightService.createFight(fight);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFight);
        } catch (Exception e) {
            log.error("Ошибка при создании боя", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Обновить бой
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fight> updateFight(@PathVariable Long id, @Valid @RequestBody Fight fightDetails) {
        log.info("Обновление боя с ID: {}", id);
        try {
            Fight updatedFight = fightService.updateFight(id, fightDetails);
            return ResponseEntity.ok(updatedFight);
        } catch (RuntimeException e) {
            log.error("Ошибка при обновлении боя с ID: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Ошибка при обновлении боя с ID: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Удалить бой
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFight(@PathVariable Long id) {
        log.info("Удаление боя с ID: {}", id);
        try {
            fightService.deleteFight(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Ошибка при удалении боя с ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Поиск боев по критериям
     */
    @GetMapping("/search")
    public ResponseEntity<List<Fight>> searchFights(
            @RequestParam(required = false) String myFighter,
            @RequestParam(required = false) String opponent,
            @RequestParam(required = false) FightResult result,
            @RequestParam(required = false) FightMode fightMode,
            @RequestParam(required = false) WeightClass weightClass,
            @RequestParam(required = false) Integer season) {
        
        log.debug("Поиск боев по критериям: myFighter={}, opponent={}, result={}, fightMode={}, weightClass={}, season={}", 
                 myFighter, opponent, result, fightMode, weightClass, season);
        
        List<Fight> fights = fightService.searchFights(myFighter, opponent, result, fightMode, weightClass, season);
        return ResponseEntity.ok(fights);
    }

    /**
     * Получить статистику боев
     */
    @GetMapping("/statistics")
    public ResponseEntity<FightService.FightStatistics> getFightStatistics() {
        log.debug("Получение статистики боев");
        FightService.FightStatistics statistics = fightService.getFightStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Получить уникальные имена бойцов
     */
    @GetMapping("/fighters/my")
    public ResponseEntity<List<String>> getDistinctMyFighters() {
        log.debug("Получение уникальных имен бойцов");
        List<String> fighters = fightService.getDistinctMyFighters();
        return ResponseEntity.ok(fighters);
    }

    /**
     * Получить уникальные имена соперников
     */
    @GetMapping("/fighters/opponents")
    public ResponseEntity<List<String>> getDistinctOpponents() {
        log.debug("Получение уникальных имен соперников");
        List<String> opponents = fightService.getDistinctOpponents();
        return ResponseEntity.ok(opponents);
    }

    /**
     * Получить уникальные сезоны
     */
    @GetMapping("/seasons")
    public ResponseEntity<List<Integer>> getDistinctSeasons() {
        log.debug("Получение уникальных сезонов");
        List<Integer> seasons = fightService.getDistinctSeasons();
        return ResponseEntity.ok(seasons);
    }

    /**
     * Получить все доступные режимы боя
     */
    @GetMapping("/modes")
    public ResponseEntity<FightMode[]> getFightModes() {
        return ResponseEntity.ok(FightMode.values());
    }

    /**
     * Получить все доступные результаты боя
     */
    @GetMapping("/results")
    public ResponseEntity<FightResult[]> getFightResults() {
        return ResponseEntity.ok(FightResult.values());
    }

    /**
     * Получить все доступные весовые категории
     */
    @GetMapping("/weight-classes")
    public ResponseEntity<WeightClass[]> getWeightClasses() {
        return ResponseEntity.ok(WeightClass.values());
    }
}
