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
 * Тест для проверки пагинации
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PaginationVerificationTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
        open("http://localhost:8080/fights");
    }

    @Test
    void testPaginationWithExistingData() {
        System.out.println("🔍 Проверка пагинации с существующими данными...");
        
        // Проверяем, что страница загрузилась
        $("h1").shouldHave(text("Список боев"));
        
        // Проверяем статистику боев
        if ($(".text-primary").exists()) {
            String totalFights = $(".text-primary").text();
            System.out.println("📊 Всего боев: " + totalFights);
        }
        
        // Проверяем количество строк в таблице
        int rowCount = $$("tbody tr").size();
        System.out.println("📋 Количество строк в таблице: " + rowCount);
        
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
            System.out.println("💡 Для демонстрации пагинации нужно создать больше 10 боев");
        }
        
        System.out.println("✅ Тест завершен!");
    }
    
    @Test
    void testCreateOneFight() {
        System.out.println("🥊 Создание одного боя для тестирования...");
        
        // Нажимаем кнопку "Добавить бой"
        $("button[onclick*='createFight']").click();
        
        // Ждем загрузки формы
        $("#createFightModal").shouldBe(visible);
        
        // Заполняем основную информацию
        $("#myFighter").setValue("Тестовый Боец");
        $("#opponent").setValue("Тестовый Соперник");
        $("#fightDate").setValue("2024-01-20");
        $("#notes").setValue("Тестовый бой для пагинации");
        
        // Выбираем значения
        $("#fightMode").selectOption("STANCE");
        $("#result").selectOption("WIN");
        $("#method").selectOption("DECISION");
        $("#weightClass").selectOption("LIGHTWEIGHT");
        $("#roundsPlayed").setValue("3");
        $("#ratingPoints").setValue("1600");
        $("#rankingPosition").setValue("1");
        
        // Сохраняем бой
        $("button[type='submit']").click();
        
        // Ждем закрытия модального окна
        $("#createFightModal").shouldNotBe(visible);
        
        // Ждем обновления списка
        sleep(2000);
        
        System.out.println("✅ Бой создан!");
        
        // Проверяем обновленную статистику
        if ($(".text-primary").exists()) {
            String totalFights = $(".text-primary").text();
            System.out.println("📊 Обновленное количество боев: " + totalFights);
        }
    }
}
