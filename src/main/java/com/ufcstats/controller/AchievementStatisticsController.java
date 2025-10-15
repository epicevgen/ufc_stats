package com.ufcstats.controller;

import com.ufcstats.dto.AchievementStatisticsDto;
import com.ufcstats.service.AchievementStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/achievement-statistics")
@RequiredArgsConstructor
@Slf4j
public class AchievementStatisticsController {

    private final AchievementStatisticsService achievementStatisticsService;

    @GetMapping
    public String getAchievementStatisticsPage(Model model) {
        model.addAttribute("title", "Статистика достижений");
        model.addAttribute("achievementStatistics", achievementStatisticsService.getAchievementStatistics());
        return "achievement-statistics"; // Предполагается, что будет отдельный шаблон
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<AchievementStatisticsDto> getAchievementStatisticsApi() {
        log.debug("Получение статистики достижений через API");
        AchievementStatisticsDto achievementStatistics = achievementStatisticsService.getAchievementStatistics();
        return ResponseEntity.ok(achievementStatistics);
    }
}
