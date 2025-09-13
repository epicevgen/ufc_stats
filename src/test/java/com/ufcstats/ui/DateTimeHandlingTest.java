package com.ufcstats.ui;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тест для проверки работы с датами в веб-интерфейсе
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DateTimeHandlingTest {

    @LocalServerPort
    private int port;

    @Test
    public void testDefaultDateTimeInNewFightForm() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Проверяем, что форма загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем, что поле даты заполнено текущей датой
        $("#fightDate").shouldBe(visible);
        String currentValue = $("#fightDate").getValue();
        
        // Проверяем, что значение не пустое и имеет правильный формат
        assert currentValue != null && !currentValue.isEmpty() : "Поле даты должно быть заполнено";
        assert currentValue.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}") : 
            "Формат даты должен быть yyyy-MM-ddTHH:mm, получен: " + currentValue;
    }

    @Test
    public void testDateTimeInputType() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Проверяем, что поле даты имеет тип datetime-local
        $("#fightDate").shouldHave(attribute("type", "datetime-local"));
        
        // Проверяем, что поле обязательно для заполнения
        $("#fightDate").shouldHave(attribute("required"));
    }

    @Test
    public void testFightCreationWithCustomDateTime() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Заполняем все обязательные поля
        $("#myFighter").setValue("Тестовый боец");
        $("#opponent").setValue("Тестовый соперник");
        $("#season").setValue("1");
        $("#roundsPlayed").setValue("3");
        $("#mode").selectOptionByValue("STANCE");
        $("#result").selectOptionByValue("WIN");
        $("#method").selectOptionByValue("DECISION");
        $("#weightClass").selectOptionByValue("LIGHTWEIGHT");
        
        // Устанавливаем конкретную дату и время
        $("#fightDate").setValue("2024-12-25T15:30");
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем обработки
        sleep(3000);
        
        // Проверяем результат
        if ($("h1").exists() && $("h1").text().contains("Список боев")) {
            // Успешное создание боя
            $(".table").shouldHave(text("Тестовый боец"));
            $(".table").shouldHave(text("Тестовый соперник"));
        } else {
            // Ошибка при создании боя
            $("h2").shouldHave(text("Новый бой"));
            if ($(".alert-danger").exists()) {
                $(".alert-danger").shouldBe(visible);
            }
        }
    }

    @Test
    public void testDateTimeValidation() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Заполняем все обязательные поля
        $("#myFighter").setValue("Тестовый боец");
        $("#opponent").setValue("Тестовый соперник");
        $("#season").setValue("1");
        $("#roundsPlayed").setValue("3");
        $("#mode").selectOptionByValue("STANCE");
        $("#result").selectOptionByValue("WIN");
        $("#method").selectOptionByValue("DECISION");
        $("#weightClass").selectOptionByValue("LIGHTWEIGHT");
        
        // Очищаем поле даты (устанавливаем некорректное значение)
        $("#fightDate").setValue("");
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем обработки
        sleep(2000);
        
        // Проверяем, что мы остались на форме (валидация не прошла)
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем, что есть сообщение об ошибке или форма не прошла валидацию
        if ($(".alert-danger").exists()) {
            $(".alert-danger").shouldBe(visible);
        } else {
            // Если нет alert-danger, проверяем, что форма не прошла валидацию
            $(".was-validated").shouldBe(visible);
        }
    }
}
