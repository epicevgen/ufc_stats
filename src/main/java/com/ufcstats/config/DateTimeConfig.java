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
 * Редактор для LocalDateTime
 */
class LocalDateTimeEditor extends PropertyEditorSupport {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
        } else {
            try {
                setValue(LocalDateTime.parse(text, formatter));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты: " + text + ". Ожидается формат: yyyy-MM-dd'T'HH:mm", e);
            }
        }
    }

    @Override
    public String getAsText() {
        LocalDateTime value = (LocalDateTime) getValue();
        return value != null ? value.format(formatter) : "";
    }
}
