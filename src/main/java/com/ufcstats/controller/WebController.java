package com.ufcstats.controller;

import com.ufcstats.model.Fight;
import com.ufcstats.model.FightRound;
import com.ufcstats.model.JudgeScore;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.WeightClass;
import com.ufcstats.repository.FightRepository;
import com.ufcstats.service.FightService;
import com.ufcstats.service.RatingChangeService;
import com.ufcstats.service.AdvancedStatisticsService;
import com.ufcstats.service.FighterStatisticsService;
import com.ufcstats.service.StrikeMovementService;
import com.ufcstats.service.AchievementStatisticsService;
import com.ufcstats.dto.AdvancedStatisticsDto;
import com.ufcstats.dto.FighterStatisticsDto;
import com.ufcstats.dto.StrikeMovementDto;
import com.ufcstats.dto.AchievementStatisticsDto;
import com.ufcstats.util.FightStatisticsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import com.ufcstats.dto.RatingChangeDto;

/**
 * Контроллер для веб-страниц
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final FightRepository fightRepository;
    private final FightService fightService;
    private final RatingChangeService ratingChangeService;
    private final AdvancedStatisticsService advancedStatisticsService;
    private final FighterStatisticsService fighterStatisticsService;
    private final StrikeMovementService strikeMovementService;
    private final AchievementStatisticsService achievementStatisticsService;


    @GetMapping("/")
    public String index() {
        // Перенаправляем на главную страницу - список боев
        return "redirect:/fights";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/fights")
    public String fights(Model model, 
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String search,
                        @RequestParam(defaultValue = "fightDate") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDir,
                        @RequestParam(defaultValue = "") String resultFilter,
                        @RequestParam(defaultValue = "") String fightModeFilter,
                        @RequestParam(defaultValue = "") String methodFilter) {
        model.addAttribute("title", "Список боев");
        
        // Создаем объект сортировки
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Получаем бои с учетом фильтров и поиска
        Page<Fight> fights = fightService.searchFights(search, resultFilter, fightModeFilter, methodFilter, pageable);
        
        // Получаем статистику боев
        FightService.FightStatistics statistics = fightService.getFightStatistics();
        
        // Рассчитываем изменения рейтинга для отображения индикации
        Map<Long, RatingChangeDto> ratingChanges = ratingChangeService.calculateRatingChanges(fights.getContent());
        
        model.addAttribute("fights", fights);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fights.getTotalPages());
        model.addAttribute("totalElements", fights.getTotalElements());
        
        // Добавляем параметры поиска и фильтрации
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("resultFilter", resultFilter);
        model.addAttribute("fightModeFilter", fightModeFilter);
        model.addAttribute("methodFilter", methodFilter);
        
        // Добавляем статистику
        model.addAttribute("totalFights", statistics.getTotalFights());
        model.addAttribute("wins", statistics.getWins());
        model.addAttribute("losses", statistics.getLosses());
        model.addAttribute("draws", statistics.getDraws());
        model.addAttribute("winRate", statistics.getWinRate());
        
        // Добавляем enum значения для фильтров
        model.addAttribute("fightResults", FightResult.values());
        model.addAttribute("fightModes", FightMode.values());
        model.addAttribute("fightMethods", FightMethod.values());
        
        // Добавляем данные об изменениях рейтинга
        model.addAttribute("ratingChanges", ratingChanges);
        
        return "fights";
    }

    @GetMapping("/fights/new")
    public String newFight(Model model) {
        model.addAttribute("title", "Новый бой");
        
        Fight fight = new Fight();
        fight.setFightDate(LocalDateTime.now()); // Устанавливаем текущую дату и время
        
        // Автоматически проставляем сезон из последнего боя
        Optional<Fight> lastFightOpt = fightRepository.findLatestFight();
        if (lastFightOpt.isPresent()) {
            Fight lastFight = lastFightOpt.get();
            fight.setSeason(lastFight.getSeason());
            log.debug("Автоматически проставлен сезон {} из последнего боя", lastFight.getSeason());
        } else {
            // Если нет предыдущих боев, устанавливаем сезон по умолчанию
            fight.setSeason(1);
            log.debug("Установлен сезон по умолчанию: 1");
        }
        
        model.addAttribute("fight", fight);
        model.addAttribute("fightModes", FightMode.values());
        model.addAttribute("fightResults", FightResult.values());
        model.addAttribute("fightMethods", FightMethod.values());
        model.addAttribute("weightClasses", WeightClass.values());
        
        return "fight-form";
    }

    @GetMapping("/fights/{id}")
    public String viewFight(@PathVariable Long id, Model model) {
        model.addAttribute("title", "Детали боя");
        Optional<Fight> fightOpt = fightService.getFightById(id);
        if (fightOpt.isPresent()) {
            Fight fight = fightOpt.get();
            model.addAttribute("fight", fight);
            
            // Предварительно вычисляем статистику для передачи в шаблон
            if (fight.getRounds() != null && !fight.getRounds().isEmpty()) {
                model.addAttribute("totalMyHeadDamage", FightStatisticsUtil.calculateTotalMyHeadDamage(fight.getRounds()));
                model.addAttribute("totalOpponentHeadDamage", FightStatisticsUtil.calculateTotalOpponentHeadDamage(fight.getRounds()));
                model.addAttribute("totalMyBodyDamage", FightStatisticsUtil.calculateTotalMyBodyDamage(fight.getRounds()));
                model.addAttribute("totalOpponentBodyDamage", FightStatisticsUtil.calculateTotalOpponentBodyDamage(fight.getRounds()));
                model.addAttribute("totalMyLegDamage", FightStatisticsUtil.calculateTotalMyLegDamage(fight.getRounds()));
                model.addAttribute("totalOpponentLegDamage", FightStatisticsUtil.calculateTotalOpponentLegDamage(fight.getRounds()));
                model.addAttribute("totalMySignificantStrikesLanded", FightStatisticsUtil.calculateTotalMySignificantStrikesLanded(fight.getRounds()));
                model.addAttribute("totalMySignificantStrikesAttempted", FightStatisticsUtil.calculateTotalMySignificantStrikesAttempted(fight.getRounds()));
                model.addAttribute("totalOpponentSignificantStrikesLanded", FightStatisticsUtil.calculateTotalOpponentSignificantStrikesLanded(fight.getRounds()));
                model.addAttribute("totalOpponentSignificantStrikesAttempted", FightStatisticsUtil.calculateTotalOpponentSignificantStrikesAttempted(fight.getRounds()));
                model.addAttribute("totalMyTotalStrikesLanded", FightStatisticsUtil.calculateTotalMyTotalStrikesLanded(fight.getRounds()));
                model.addAttribute("totalMyTotalStrikesAttempted", FightStatisticsUtil.calculateTotalMyTotalStrikesAttempted(fight.getRounds()));
                model.addAttribute("totalOpponentTotalStrikesLanded", FightStatisticsUtil.calculateTotalOpponentTotalStrikesLanded(fight.getRounds()));
                model.addAttribute("totalOpponentTotalStrikesAttempted", FightStatisticsUtil.calculateTotalOpponentTotalStrikesAttempted(fight.getRounds()));
                model.addAttribute("totalMyTakedownsSuccessful", FightStatisticsUtil.calculateTotalMyTakedownsSuccessful(fight.getRounds()));
                model.addAttribute("totalMyTakedownsAttempted", FightStatisticsUtil.calculateTotalMyTakedownsAttempted(fight.getRounds()));
                model.addAttribute("totalOpponentTakedownsSuccessful", FightStatisticsUtil.calculateTotalOpponentTakedownsSuccessful(fight.getRounds()));
                model.addAttribute("totalOpponentTakedownsAttempted", FightStatisticsUtil.calculateTotalOpponentTakedownsAttempted(fight.getRounds()));
                model.addAttribute("totalMyKnockdowns", FightStatisticsUtil.calculateTotalMyKnockdowns(fight.getRounds()));
                model.addAttribute("totalOpponentKnockdowns", FightStatisticsUtil.calculateTotalOpponentKnockdowns(fight.getRounds()));
                model.addAttribute("totalMyControlTime", FightStatisticsUtil.calculateTotalMyControlTimeString(fight.getRounds()));
                model.addAttribute("totalOpponentControlTime", FightStatisticsUtil.calculateTotalOpponentControlTimeString(fight.getRounds()));
            }
        } else {
            // Если бой не найден, возвращаем ошибку
            throw new RuntimeException("Бой с ID " + id + " не найден");
        }
        return "fight-details";
    }

    @GetMapping("/fights/{id}/edit")
    public String editFight(@PathVariable Long id, Model model) {
        model.addAttribute("title", "Редактирование боя");
        fightService.getFightById(id).ifPresent(fight -> {
            model.addAttribute("fight", fight);
        });
        
        model.addAttribute("fightModes", FightMode.values());
        model.addAttribute("fightResults", FightResult.values());
        model.addAttribute("fightMethods", FightMethod.values());
        model.addAttribute("weightClasses", WeightClass.values());
        
        return "fight-form";
    }

    @PostMapping("/fights/new")
    public String createFight(Fight fight, Model model) {
        try {
            log.info("Создаем новый бой: {} vs {}", fight.getMyFighter(), fight.getOpponent());
            log.debug("Данные боя: fightDate={}, mode={}, result={}, method={}, roundsPlayed={}", 
                     fight.getFightDate(), fight.getFightMode(), fight.getResult(), 
                     fight.getMethod(), fight.getRoundsPlayed());
            
            // Валидация обязательных полей
            if (fight.getMyFighter() == null || fight.getMyFighter().trim().isEmpty()) {
                throw new IllegalArgumentException("Имя моего бойца обязательно");
            }
            if (fight.getOpponent() == null || fight.getOpponent().trim().isEmpty()) {
                throw new IllegalArgumentException("Имя соперника обязательно");
            }
            if (fight.getFightMode() == null) {
                throw new IllegalArgumentException("Режим боя обязателен");
            }
            if (fight.getResult() == null) {
                throw new IllegalArgumentException("Результат боя обязателен");
            }
            if (fight.getMethod() == null) {
                throw new IllegalArgumentException("Метод боя обязателен");
            }
            if (fight.getWeightClass() == null) {
                throw new IllegalArgumentException("Весовая категория обязательна");
            }
            if (fight.getSeason() == null || fight.getSeason() <= 0) {
                throw new IllegalArgumentException("Номер сезона должен быть положительным числом");
            }
            if (fight.getRoundsPlayed() == null || fight.getRoundsPlayed() < 1 || fight.getRoundsPlayed() > 5) {
                throw new IllegalArgumentException("Количество раундов должно быть от 1 до 5");
            }
            
            // Обрабатываем дату боя
            if (fight.getFightDate() == null) {
                fight.setFightDate(LocalDateTime.of(2024, 1, 15, 20, 0));
                log.warn("Дата боя не задана, установлена по умолчанию: {}", fight.getFightDate());
            } else {
                log.debug("Дата боя получена: {}", fight.getFightDate());
            }
            
            // Исправляем связи между Fight и FightRound/JudgeScore
            fixCascadeRelationships(fight);
            
            log.info("Вызываем fightService.createFight");
            fightService.createFight(fight);
            log.info("Бой успешно создан, перенаправляем на /fights");
            return "redirect:/fights";
        } catch (IllegalArgumentException e) {
            log.error("Ошибка валидации при создании боя: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fight", fight);
            model.addAttribute("fightModes", FightMode.values());
            model.addAttribute("fightResults", FightResult.values());
            model.addAttribute("fightMethods", FightMethod.values());
            model.addAttribute("weightClasses", WeightClass.values());
            return "fight-form";
        } catch (Exception e) {
            log.error("Неожиданная ошибка при создании боя: {}", e.getMessage(), e);
            model.addAttribute("error", "Произошла неожиданная ошибка: " + e.getMessage());
            model.addAttribute("fight", fight);
            model.addAttribute("fightModes", FightMode.values());
            model.addAttribute("fightResults", FightResult.values());
            model.addAttribute("fightMethods", FightMethod.values());
            model.addAttribute("weightClasses", WeightClass.values());
            return "fight-form";
        }
    }

    @PostMapping("/fights/{id}/edit")
    public String updateFight(@PathVariable Long id, Fight fight, Model model) {
        try {
            // Исправляем связи между Fight и FightRound/JudgeScore
            fixCascadeRelationships(fight);
            
            fightService.updateFight(id, fight);
            return "redirect:/fights/" + id;
        } catch (Exception e) {
            // В случае ошибки возвращаемся на форму с сообщением об ошибке
            model.addAttribute("error", "Ошибка при обновлении боя: " + e.getMessage());
            model.addAttribute("fight", fight);
            model.addAttribute("fightModes", FightMode.values());
            model.addAttribute("fightResults", FightResult.values());
            model.addAttribute("fightMethods", FightMethod.values());
            model.addAttribute("weightClasses", WeightClass.values());
            return "fight-form";
        }
    }

    @GetMapping("/statistics")
    public String statistics(Model model,
                           @RequestParam(required = false) String fightModeFilter,
                           @RequestParam(required = false) Integer seasonFilter,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           @RequestParam(required = false) String comparisonFilter,
                           @RequestParam(required = false) Integer periodDays,
                           @RequestParam(required = false) Boolean compareWithAverage,
                           @RequestParam(required = false) Integer displaySeason,
                           @RequestParam(required = false) Integer compareSeason) {
        model.addAttribute("title", "Статистика");
        
        // Если выбран режим сравнения, обрабатываем параметры по-другому
        if (comparisonFilter != null && !comparisonFilter.isEmpty()) {
            if ("periods".equals(comparisonFilter) && periodDays != null && periodDays > 0) {
                // Режим сравнения периодов
                // Используем LocalDate для корректной работы с датами без времени
                LocalDate today = LocalDate.now();
                LocalDate periodEnd = today;
                LocalDate periodStart = today.minusDays(periodDays - 1); // Включаем сегодняшний день
                
                String periodStartStr = periodStart.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                String periodEndStr = periodEnd.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                
                // Получаем статистику за выбранный период
                FightService.FightStatistics stats = fightService.getFightStatistics(fightModeFilter, null, periodStartStr, periodEndStr);
                AdvancedStatisticsDto advancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, null, periodStartStr, periodEndStr);
                FighterStatisticsDto fighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, null, periodStartStr, periodEndStr);
                StrikeMovementDto strikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, null, periodStartStr, periodEndStr);
                AchievementStatisticsDto achievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, null, periodStartStr, periodEndStr);
                
                // Получаем данные для сравнения
                FightService.FightStatistics compareStats = null;
                AdvancedStatisticsDto compareAdvancedStats = null;
                FighterStatisticsDto compareFighterStats = null;
                StrikeMovementDto compareStrikeMovement = null;
                AchievementStatisticsDto compareAchievementStats = null;
                boolean comparisonDataFound = false;
                
                if (compareWithAverage != null && compareWithAverage) {
                    // Сравнение с общим средним (до начала периода)
                    LocalDate compareEndDate = periodStart.minusDays(1);
                    String compareEndStr = compareEndDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                    compareStats = fightService.getFightStatistics(fightModeFilter, null, null, compareEndStr);
                    compareAdvancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, null, null, compareEndStr);
                    compareFighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, null, null, compareEndStr);
                    compareStrikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, null, null, compareEndStr);
                    compareAchievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, null, null, compareEndStr);
                    comparisonDataFound = (compareStats != null && compareStats.getTotalFights() > 0);
                } else {
                    // Сравнение с предыдущим аналогичным периодом
                    // Предыдущий период должен быть строго такого же размера
                    LocalDate comparePeriodEnd = periodStart.minusDays(1); // День перед началом текущего периода
                    LocalDate comparePeriodStart = comparePeriodEnd.minusDays(periodDays - 1); // Начало предыдущего периода (того же размера)
                    
                    String compareStartStr = comparePeriodStart.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                    String compareEndStr = comparePeriodEnd.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                    
                    compareStats = fightService.getFightStatistics(fightModeFilter, null, compareStartStr, compareEndStr);
                    compareAdvancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, null, compareStartStr, compareEndStr);
                    compareFighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, null, compareStartStr, compareEndStr);
                    compareStrikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, null, compareStartStr, compareEndStr);
                    compareAchievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, null, compareStartStr, compareEndStr);
                    comparisonDataFound = (compareStats != null && compareStats.getTotalFights() > 0);
                }
                
                model.addAttribute("statistics", stats);
                model.addAttribute("advancedStatistics", advancedStats);
                model.addAttribute("fighterStatistics", fighterStats);
                model.addAttribute("strikeMovement", strikeMovement);
                model.addAttribute("achievementStatistics", achievementStats);
                
                if (comparisonDataFound) {
                    model.addAttribute("compareStatistics", compareStats);
                    model.addAttribute("compareAdvancedStatistics", compareAdvancedStats);
                    model.addAttribute("compareFighterStatistics", compareFighterStats);
                    model.addAttribute("compareStrikeMovement", compareStrikeMovement);
                    model.addAttribute("compareAchievementStatistics", compareAchievementStats);
                } else {
                    // Проверяем, что есть хотя бы данные для текущего периода
                    if (stats.getTotalFights() == 0) {
                        model.addAttribute("comparisonError", "Не найдено данных за выбранный период");
                    } else {
                        model.addAttribute("comparisonError", "Не найдено данных для сравнения");
                    }
                }
            } else if ("seasons".equals(comparisonFilter) && displaySeason != null && compareSeason != null) {
                // Режим сравнения сезонов
                FightService.FightStatistics stats = fightService.getFightStatistics(fightModeFilter, displaySeason, null, null);
                AdvancedStatisticsDto advancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, displaySeason, null, null);
                FighterStatisticsDto fighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, displaySeason, null, null);
                StrikeMovementDto strikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, displaySeason, null, null);
                AchievementStatisticsDto achievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, displaySeason, null, null);
                
                // Получаем данные для сравнения
                FightService.FightStatistics compareStats = fightService.getFightStatistics(fightModeFilter, compareSeason, null, null);
                AdvancedStatisticsDto compareAdvancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, compareSeason, null, null);
                FighterStatisticsDto compareFighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, compareSeason, null, null);
                StrikeMovementDto compareStrikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, compareSeason, null, null);
                AchievementStatisticsDto compareAchievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, compareSeason, null, null);
                
                model.addAttribute("statistics", stats);
                model.addAttribute("advancedStatistics", advancedStats);
                model.addAttribute("fighterStatistics", fighterStats);
                model.addAttribute("strikeMovement", strikeMovement);
                model.addAttribute("achievementStatistics", achievementStats);
                
                model.addAttribute("compareStatistics", compareStats);
                model.addAttribute("compareAdvancedStatistics", compareAdvancedStats);
                model.addAttribute("compareFighterStatistics", compareFighterStats);
                model.addAttribute("compareStrikeMovement", compareStrikeMovement);
                model.addAttribute("compareAchievementStatistics", compareAchievementStats);
            } else {
                // Недостаточно данных для сравнения - показываем обычную статистику
                FightService.FightStatistics stats = fightService.getFightStatistics(fightModeFilter, seasonFilter, startDate, endDate);
                AdvancedStatisticsDto advancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, seasonFilter, startDate, endDate);
                FighterStatisticsDto fighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, seasonFilter, startDate, endDate);
                StrikeMovementDto strikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, seasonFilter, startDate, endDate);
                AchievementStatisticsDto achievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, seasonFilter, startDate, endDate);
                
                model.addAttribute("statistics", stats);
                model.addAttribute("advancedStatistics", advancedStats);
                model.addAttribute("fighterStatistics", fighterStats);
                model.addAttribute("strikeMovement", strikeMovement);
                model.addAttribute("achievementStatistics", achievementStats);
            }
        } else {
            // Стандартный режим без сравнения
            FightService.FightStatistics stats = fightService.getFightStatistics(fightModeFilter, seasonFilter, startDate, endDate);
            AdvancedStatisticsDto advancedStats = advancedStatisticsService.getAdvancedStatistics(fightModeFilter, seasonFilter, startDate, endDate);
            FighterStatisticsDto fighterStats = fighterStatisticsService.getFighterStatistics(fightModeFilter, seasonFilter, startDate, endDate);
            StrikeMovementDto strikeMovement = strikeMovementService.getStrikeMovementData(fightModeFilter, seasonFilter, startDate, endDate);
            AchievementStatisticsDto achievementStats = achievementStatisticsService.getAchievementStatistics(fightModeFilter, seasonFilter, startDate, endDate);
            
            model.addAttribute("statistics", stats);
            model.addAttribute("advancedStatistics", advancedStats);
            model.addAttribute("fighterStatistics", fighterStats);
            model.addAttribute("strikeMovement", strikeMovement);
            model.addAttribute("achievementStatistics", achievementStats);
        }
        
        // Добавляем данные для фильтров
        model.addAttribute("fightModes", FightMode.values());
        model.addAttribute("seasons", fightService.getDistinctSeasons());
        model.addAttribute("selectedFightMode", fightModeFilter);
        model.addAttribute("selectedSeason", seasonFilter);
        model.addAttribute("selectedStartDate", startDate);
        model.addAttribute("selectedEndDate", endDate);
        model.addAttribute("selectedComparison", comparisonFilter);
        model.addAttribute("selectedPeriodDays", periodDays);
        model.addAttribute("selectedCompareWithAverage", compareWithAverage);
        model.addAttribute("selectedDisplaySeason", displaySeason);
        model.addAttribute("selectedCompareSeason", compareSeason);
        
        return "statistics";
    }

    /**
     * Исправляет каскадные связи между Fight и связанными сущностями
     */
    private void fixCascadeRelationships(Fight fight) {
        // Исправляем связи для раундов
        if (fight.getRounds() != null) {
            int roundNumber = 1;
            for (FightRound round : fight.getRounds()) {
                if (round.getFight() == null) {
                    round.setFight(fight);
                }
                // Устанавливаем номер раунда (1, 2, 3, 4, 5)
                if (round.getRoundNumber() == null) {
                    round.setRoundNumber(roundNumber++);
                }
            }
        }
        
        // Исправляем связи для судейских оценок
        if (fight.getJudgeScores() != null) {
            int judgeNumber = 1;
            for (JudgeScore judgeScore : fight.getJudgeScores()) {
                if (judgeScore.getFight() == null) {
                    judgeScore.setFight(fight);
                }
                // Устанавливаем номер судьи (1, 2, 3)
                if (judgeScore.getJudgeNumber() == null) {
                    judgeScore.setJudgeNumber(judgeNumber++);
                }
            }
        }
    }

    @GetMapping("/test-data")
    public String testDataPage() {
        return "test-data";
    }
}
