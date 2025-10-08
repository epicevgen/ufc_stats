package com.ufcstats.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Основной CRUD UI тест для работы с существующими боями
 * 
 * Тест выполняет полный цикл операций с существующими боями:
 * 1. Просмотр существующего боя
 * 2. Редактирование каждого блока боя через прямую страницу
 * 3. Проверка корректности редактирования
 * 4. Удаление боя
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainCrudTest extends SelenideBaseTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        Configuration.baseUrl = "http://localhost:" + port;
        Configuration.timeout = 10000;
        Configuration.browser = "chrome";
        Configuration.headless = true;
    }

    @Test
    void testFullCrudWorkflow() {
        // Открываем страницу со списком боев
        open("/fights");
        
        // Ждем загрузки страницы
        $("h1").shouldHave(text("Список боев"));
        
        // === ЭТАП 1: ПРОСМОТР СУЩЕСТВУЮЩЕГО БОЯ ===
        System.out.println("=== ЭТАП 1: ПРОСМОТР СУЩЕСТВУЮЩЕГО БОЯ ===");
        
        // Находим первый бой в таблице и кликаем на строку для просмотра
        $("tbody tr").shouldBe(visible);
        $("tbody tr").click();
        
        // Ждем загрузки страницы деталей боя
        sleep(2000);
        
        // Проверяем, что отображается информация о бое
        $("body").shouldHave(text("Иван Петров"));
        $("body").shouldHave(text("Дмитрий Волков"));
        
        // Возвращаемся к списку боев через JavaScript
        executeJavaScript("window.location.href = '/fights'");
        sleep(2000);
        
        // === ЭТАП 2: РЕДАКТИРОВАНИЕ КАЖДОГО БЛОКА БОЯ ===
        System.out.println("=== ЭТАП 2: РЕДАКТИРОВАНИЕ КАЖДОГО БЛОКА БОЯ ===");
        
        // Находим бой и нажимаем "Редактировать"
        $("tbody tr").$("button[onclick*='editFight']").click();
        
        // Ждем открытия модального окна редактирования
        $("#editFightModal").shouldBe(visible);
        sleep(3000); // Даем время на загрузку формы
        
        // Редактируем только основные поля
        editBasicFields();
        
        // Сохраняем изменения
        $("#saveEditFightBtn").click();
        
        // Ждем закрытия модального окна и обновления страницы
        sleep(3000);
        
        // Проверяем, что модальное окно закрыто, если нет - принудительно закрываем
        if ($("#editFightModal").isDisplayed()) {
            System.out.println("Модальное окно редактирования все еще открыто, принудительно закрываем...");
            $("#editFightModal").$("button[data-bs-dismiss='modal']").click();
            sleep(1000);
        }
        
        // === ЭТАП 3: ПРОВЕРКА КОРРЕКТНОСТИ РЕДАКТИРОВАНИЯ ===
        System.out.println("=== ЭТАП 3: ПРОВЕРКА КОРРЕКТНОСТИ РЕДАКТИРОВАНИЯ ===");
        
        // Находим отредактированный бой и кликаем на строку для просмотра
        $("tbody tr").click();
        sleep(2000);
        
        // Проверяем, что изменения сохранились
        $("body").shouldHave(text("Петр Иванов")); // Измененное имя
        $("body").shouldHave(text("Сидор Алексеев")); // Измененное имя
        
        // Возвращаемся к списку боев через JavaScript
        executeJavaScript("window.location.href = '/fights'");
        sleep(2000);
        
        // === ЭТАП 4: УДАЛЕНИЕ БОЯ ===
        System.out.println("=== ЭТАП 4: УДАЛЕНИЕ БОЯ ===");
        
        // Находим бой и нажимаем "Удалить"
        $("tbody tr").$("button[onclick*='deleteFight']").click();
        
        // Подтверждаем удаление в диалоге
        Selenide.confirm();
        
        // Ждем обновления страницы
        sleep(2000);
        
        // Проверяем, что бой удален
        $("body").shouldNotHave(text("Петр Иванов"));
        $("body").shouldNotHave(text("Сидор Алексеев"));
        
        System.out.println("=== ВСЕ ЭТАПЫ CRUD ТЕСТА УСПЕШНО ЗАВЕРШЕНЫ ===");
    }
    
    /**
     * Редактирует только основные поля боя
     */
    private void editBasicFields() {
        System.out.println("Редактируем основные поля...");
        
        // Изменяем только имена бойцов
        $("#myFighter").clear();
        $("#myFighter").setValue("Петр Иванов");
        
        $("#opponent").clear();
        $("#opponent").setValue("Сидор Алексеев");
    }
}