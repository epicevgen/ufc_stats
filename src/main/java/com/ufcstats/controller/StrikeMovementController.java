package com.ufcstats.controller;

import com.ufcstats.dto.StrikeMovementDto;
import com.ufcstats.service.StrikeMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/strike-movement")
@RequiredArgsConstructor
@Slf4j
public class StrikeMovementController {

    private final StrikeMovementService strikeMovementService;

    @GetMapping
    public String getStrikeMovementPage(Model model) {
        model.addAttribute("title", "Движение ударов");
        model.addAttribute("strikeMovement", strikeMovementService.getStrikeMovementData());
        return "strike-movement"; // Предполагается, что будет отдельный шаблон
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<StrikeMovementDto> getStrikeMovementApi() {
        log.debug("Получение данных о движении ударов через API");
        StrikeMovementDto strikeMovement = strikeMovementService.getStrikeMovementData();
        return ResponseEntity.ok(strikeMovement);
    }
}
