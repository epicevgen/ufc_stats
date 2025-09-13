package com.ufcstats.config;

import com.ufcstats.model.enums.FightMode;
import com.ufcstats.model.enums.FightResult;
import com.ufcstats.model.enums.FightMethod;
import com.ufcstats.model.enums.WeightClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для приложения
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обработка ошибок валидации для веб-форм
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException ex, HttpServletRequest request) {
        log.error("Ошибка валидации формы: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("errorMessage", "Ошибка валидации данных. Проверьте заполненные поля.");
        
        // Определяем, какую страницу показать в зависимости от URL
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/fights/new") || requestURI.contains("/fights/") && requestURI.contains("/edit")) {
            modelAndView.setViewName("fight-form");
            // Добавляем необходимые атрибуты для формы
            modelAndView.addObject("fightModes", FightMode.values());
            modelAndView.addObject("fightResults", FightResult.values());
            modelAndView.addObject("fightMethods", FightMethod.values());
            modelAndView.addObject("weightClasses", WeightClass.values());
        } else {
            modelAndView.setViewName("error");
        }
        
        return modelAndView;
    }

    /**
     * Обработка ошибок валидации для REST API
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Ошибка валидации REST API: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Обработка ошибок ограничений валидации
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Ошибка ограничений валидации: {}", ex.getMessage());
        
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Обработка общих исключений для веб-страниц
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Неожиданная ошибка: {}", ex.getMessage(), ex);
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Произошла неожиданная ошибка: " + ex.getMessage());
        modelAndView.addObject("timestamp", java.time.LocalDateTime.now());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setViewName("error");
        
        return modelAndView;
    }

    /**
     * Обработка ошибок 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Ошибка 400 Bad Request: {}", ex.getMessage());
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Некорректные данные: " + ex.getMessage());
        modelAndView.addObject("timestamp", java.time.LocalDateTime.now());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setViewName("error");
        
        return modelAndView;
    }
}
