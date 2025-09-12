package com.ufcstats.controller;

import com.ufcstats.model.Fight;
import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.WeightClass;
import com.ufcstats.service.FightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для веб-страниц
 */
@Controller
@RequiredArgsConstructor
public class WebController {

    private final FightService fightService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "UFC Stats - Статистика боев");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Панель управления");
        FightService.FightStatistics statistics = fightService.getFightStatistics();
        model.addAttribute("statistics", statistics);
        return "dashboard";
    }

    @GetMapping("/fights")
    public String fights(Model model, 
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("title", "Список боев");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Fight> fights = fightService.getAllFights(pageable);
        
        model.addAttribute("fights", fights);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fights.getTotalPages());
        model.addAttribute("totalElements", fights.getTotalElements());
        
        return "fights";
    }

    @GetMapping("/fights/new")
    public String newFight(Model model) {
        model.addAttribute("title", "Новый бой");
        
        Fight fight = new Fight();
        fight.setRoundsPlayed(3); // По умолчанию 3 раунда
        
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
        fightService.getFightById(id).ifPresent(fight -> {
            model.addAttribute("fight", fight);
        });
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
            // Временно устанавливаем дату по умолчанию, если она не задана
            if (fight.getFightDate() == null) {
                fight.setFightDate(LocalDateTime.of(2024, 1, 15, 20, 0));
            }
            fightService.createFight(fight);
            return "redirect:/fights";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на форму с сообщением об ошибке
            model.addAttribute("error", "Ошибка при создании боя: " + e.getMessage());
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
    public String statistics(Model model) {
        model.addAttribute("title", "Статистика");
        FightService.FightStatistics stats = fightService.getFightStatistics();
        model.addAttribute("statistics", stats);
        return "statistics";
    }
}
