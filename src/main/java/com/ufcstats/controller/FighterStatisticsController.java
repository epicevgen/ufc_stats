package com.ufcstats.controller;

import com.ufcstats.dto.FighterStatisticsDto;
import com.ufcstats.service.FighterStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для статистики бойцов
 */
@Controller
@RequestMapping("/fighter-statistics")
@RequiredArgsConstructor
@Slf4j
public class FighterStatisticsController {
    
    private final FighterStatisticsService fighterStatisticsService;
    
    /**
     * Получить статистику бойцов (JSON API)
     */
    @GetMapping("/api")
    public ResponseEntity<FighterStatisticsDto> getFighterStatisticsApi() {
        log.debug("Получение статистики бойцов (API)");
        FighterStatisticsDto statistics = fighterStatisticsService.getFighterStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Получить страницу статистики бойцов
     */
    @GetMapping
    public String getFighterStatisticsPage(Model model) {
        log.debug("Получение страницы статистики бойцов");
        FighterStatisticsDto statistics = fighterStatisticsService.getFighterStatistics();
        model.addAttribute("fighterStatistics", statistics);
        return "fighter-statistics";
    }
}
