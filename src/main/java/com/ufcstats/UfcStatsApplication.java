package com.ufcstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения UFC Stats
 * 
 * Веб-приложение для хранения и анализа статистики боев UFC 5
 */
@SpringBootApplication
public class UfcStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UfcStatsApplication.class, args);
    }
}
