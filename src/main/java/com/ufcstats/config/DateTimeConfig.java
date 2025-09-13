package com.ufcstats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Конфигурация для форматирования дат и времени
 */
@Configuration
public class DateTimeConfig {

    @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
        registrar.registerFormatters(conversionService);
        
        return conversionService;
    }
}

/**
 * Глобальный контроллер для настройки биндинга дат
 */
@ControllerAdvice
class DateTimeControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());
    }
}

/**
 * Редактор для LocalDateTime с поддержкой различных форматов
 */
class LocalDateTimeEditor extends PropertyEditorSupport {
    private final DateTimeFormatter[] formatters = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),     // datetime-local format
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // with seconds
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),      // space instead of T
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),   // space with seconds
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),      // European format
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")       // US format
    };

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
            return;
        }
        
        // Очищаем текст от лишних символов
        String cleanText = text.trim();
        
        // Пытаемся распарсить с помощью различных форматов
        for (DateTimeFormatter formatter : formatters) {
            try {
                setValue(LocalDateTime.parse(cleanText, formatter));
                return;
            } catch (DateTimeParseException e) {
                // Продолжаем с следующим форматом
            }
        }
        
        // Если ни один формат не подошел, пробуем ISO формат
        try {
            setValue(LocalDateTime.parse(cleanText));
            return;
        } catch (DateTimeParseException e) {
            // Последняя попытка
        }
        
        throw new IllegalArgumentException("Неверный формат даты: " + text + 
            ". Поддерживаемые форматы: yyyy-MM-dd'T'HH:mm, yyyy-MM-dd HH:mm, dd.MM.yyyy HH:mm", 
            new DateTimeParseException("Не удалось распарсить дату", text, 0));
    }

    @Override
    public String getAsText() {
        LocalDateTime value = (LocalDateTime) getValue();
        return value != null ? value.format(formatters[0]) : "";
    }
}

