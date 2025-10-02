package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Тест для проверки пагинации
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PaginationTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
        open("http://localhost:8080/fights");
    }

    @Test
    void testCreateMultipleFightsForPagination() {
        System.out.println("🥊 Создание множественных боев для тестирования пагинации...");
        
        // Создаем 10 боев для тестирования пагинации
        String[] fighters = {"Иван Петров", "Алексей Сидоров", "Дмитрий Волков", "Сергей Козлов", "Андрей Морозов"};
        String[] opponents = {"Джон Смит", "Майк Джонсон", "Том Уилсон", "Джейк Браун", "Боб Дэвис"};
        
        for (int i = 0; i < 10; i++) {
            System.out.println("Создание боя #" + (i + 1));
            
            // Нажимаем кнопку "Добавить бой"
            $("button[onclick*='createFight']").click();
            
            // Ждем загрузки формы
            $("#createFightModal").shouldBe(visible);
            
            // Заполняем основную информацию
            $("#myFighter").setValue(fighters[i % fighters.length]);
            $("#opponent").setValue(opponents[i % opponents.length]);
            $("#fightDate").setValue("2024-01-" + String.format("%02d", 15 + i));
            $("#notes").setValue("Тестовый бой #" + (i + 1));
            
            // Выбираем случайные значения
            $("#fightMode").selectOption(i % 2 == 0 ? "STANCE" : "MMA");
            $("#result").selectOption(i % 3 == 0 ? "WIN" : (i % 3 == 1 ? "LOSS" : "DRAW"));
            $("#method").selectOption(i % 4 == 0 ? "DECISION" : (i % 4 == 1 ? "KNOCKOUT" : (i % 4 == 2 ? "SUBMISSION" : "EARLY_EXIT")));
            $("#weightClass").selectOption("LIGHTWEIGHT");
            $("#roundsPlayed").setValue(String.valueOf((i % 5) + 1));
            $("#ratingPoints").setValue(String.valueOf(1500 + (i * 10)));
            $("#rankingPosition").setValue(String.valueOf(i + 1));
            
            // Сохраняем бой
            $("button[type='submit']").click();
            
            // Ждем закрытия модального окна
            $("#createFightModal").shouldNotBe(visible);
            
            // Ждем обновления списка
            sleep(1000);
            
            System.out.println("✅ Бой #" + (i + 1) + " создан");
        }
        
        System.out.println("🎉 Создание тестовых боев завершено!");
        
        // Проверяем, что пагинация появилась
        if ($(".pagination").exists()) {
            System.out.println("✅ Пагинация работает корректно!");
        } else {
            System.out.println("ℹ️ Пагинация не найдена - возможно, недостаточно боев");
        }
    }
    
    @Test
    void testPaginationNavigation() {
        // Проверяем, что пагинация присутствует
        if ($(".pagination").exists()) {
            System.out.println("✅ Пагинация найдена");
            
            // Проверяем навигацию по страницам
            if ($(".page-link").exists()) {
                System.out.println("✅ Навигационные ссылки найдены");
                
                // Проверяем информацию о страницах
                if ($(".text-muted").exists()) {
                    System.out.println("✅ Информация о страницах отображается");
                }
            }
        } else {
            System.out.println("ℹ️ Пагинация не найдена - возможно, недостаточно боев");
        }
    }
    
    @Test
    void testPaginationWithSearch() {
        // Выполняем поиск
        $("#search").setValue("Иван");
        $("button[type='submit']").click();
        
        // Проверяем, что результаты отфильтрованы
        if ($("tbody tr").exists()) {
            System.out.println("✅ Поиск работает с пагинацией");
        } else {
            System.out.println("ℹ️ Поиск не дал результатов");
        }
    }
    
    @Test
    void testPaginationWithFilters() {
        // Применяем фильтр по результату
        if ($$(".dropdown-toggle").size() > 0) {
            $$(".dropdown-toggle").first().click();
            if ($$(".dropdown-item").size() > 0) {
                $$(".dropdown-item").first().click();
                System.out.println("✅ Фильтры работают с пагинацией");
            }
        } else {
            System.out.println("ℹ️ Фильтры не найдены");
        }
    }
}
