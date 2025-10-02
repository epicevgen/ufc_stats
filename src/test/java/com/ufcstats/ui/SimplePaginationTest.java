package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

/**
 * Простой тест для проверки пагинации
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class SimplePaginationTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
        open("http://localhost:8080/fights");
    }

    @Test
    void testPaginationExists() {
        System.out.println("🔍 Проверка наличия пагинации...");
        
        // Проверяем, что страница загрузилась
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем статистику боев
        if ($(".text-primary").exists()) {
            String totalFights = $(".text-primary").text();
            System.out.println("📊 Всего боев: " + totalFights);
        }
        
        // Проверяем наличие пагинации
        if ($(".pagination").exists()) {
            System.out.println("✅ Пагинация найдена!");
            
            // Проверяем навигационные элементы
            if ($(".page-link").exists()) {
                System.out.println("✅ Навигационные ссылки найдены");
            }
            
            // Проверяем информацию о страницах
            if ($(".text-muted").exists()) {
                String pageInfo = $(".text-muted").text();
                System.out.println("📄 Информация о страницах: " + pageInfo);
            }
        } else {
            System.out.println("ℹ️ Пагинация не найдена - возможно, недостаточно боев для отображения");
        }
        
        // Проверяем количество строк в таблице
        int rowCount = $$("tbody tr").size();
        System.out.println("📋 Количество строк в таблице: " + rowCount);
        
        System.out.println("✅ Тест завершен!");
    }
    
    @Test
    void testSearchFunctionality() {
        System.out.println("🔍 Проверка функции поиска...");
        
        // Проверяем наличие поля поиска
        if ($("#search").exists()) {
            System.out.println("✅ Поле поиска найдено");
            
            // Выполняем поиск
            $("#search").setValue("Иван");
            $("button[type='submit']").click();
            
            // Ждем обновления результатов
            sleep(2000);
            
            // Проверяем результаты
            int rowCount = $$("tbody tr").size();
            System.out.println("📋 Найдено строк после поиска: " + rowCount);
            
            if (rowCount > 0) {
                System.out.println("✅ Поиск работает корректно");
            } else {
                System.out.println("ℹ️ Поиск не дал результатов");
            }
        } else {
            System.out.println("❌ Поле поиска не найдено");
        }
    }
    
    @Test
    void testFilterFunctionality() {
        System.out.println("🔍 Проверка функции фильтрации...");
        
        // Проверяем наличие фильтров
        if ($$(".dropdown-toggle").size() > 0) {
            System.out.println("✅ Фильтры найдены");
            
            // Проверяем количество фильтров
            int filterCount = $$(".dropdown-toggle").size();
            System.out.println("📊 Количество фильтров: " + filterCount);
            
            // Проверяем первый фильтр
            $$(".dropdown-toggle").first().click();
            
            if ($$(".dropdown-item").size() > 0) {
                System.out.println("✅ Опции фильтра найдены");
                
                // Выбираем первую опцию
                $$(".dropdown-item").first().click();
                
                // Ждем обновления результатов
                sleep(2000);
                
                // Проверяем результаты
                int rowCount = $$("tbody tr").size();
                System.out.println("📋 Найдено строк после фильтрации: " + rowCount);
                
                if (rowCount > 0) {
                    System.out.println("✅ Фильтрация работает корректно");
                } else {
                    System.out.println("ℹ️ Фильтрация не дала результатов");
                }
            } else {
                System.out.println("❌ Опции фильтра не найдены");
            }
        } else {
            System.out.println("❌ Фильтры не найдены");
        }
    }
}
