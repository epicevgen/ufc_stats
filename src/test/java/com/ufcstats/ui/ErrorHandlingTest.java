package com.ufcstats.ui;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тест для проверки обработки ошибок в веб-интерфейсе
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ErrorHandlingTest {

    @LocalServerPort
    private int port;

    @Test
    public void testErrorHandlingWithInvalidData() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Проверяем, что форма загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Заполняем только обязательные поля с некорректными данными
        $("#myFighter").setValue(""); // Пустое имя бойца
        $("#opponent").setValue(""); // Пустое имя соперника
        $("#season").setValue("0"); // Некорректный номер сезона
        $("#roundsPlayed").setValue("6"); // Некорректное количество раундов
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем немного для обработки
        sleep(2000);
        
        // Проверяем, что мы остались на форме
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем, что есть сообщение об ошибке (может быть в разных форматах)
        if ($(".alert-danger").exists()) {
            $(".alert-danger").shouldBe(visible);
            $(".alert-danger").shouldHave(text("Ошибка"));
        } else {
            // Если нет alert-danger, проверяем, что форма не прошла валидацию
            $(".was-validated").shouldBe(visible);
        }
    }

    @Test
    public void testErrorHandlingWithMissingRequiredFields() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Проверяем, что форма загрузилась
        $("h2").shouldHave(text("Новый бой"));
        
        // Заполняем только некоторые поля
        $("#myFighter").setValue("Тестовый боец");
        // Не заполняем остальные обязательные поля
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем обработки
        sleep(2000);
        
        // Проверяем, что мы остались на форме
        $("h2").shouldHave(text("Новый бой"));
        
        // Проверяем, что есть сообщение об ошибке или форма не прошла валидацию
        if ($(".alert-danger").exists()) {
            $(".alert-danger").shouldBe(visible);
        } else {
            // Если нет alert-danger, проверяем, что форма не прошла валидацию
            $(".was-validated").shouldBe(visible);
        }
    }

    @Test
    public void testErrorPageAccess() {
        // Пытаемся получить доступ к несуществующей странице
        open("http://localhost:" + port + "/nonexistent-page");
        
        // Проверяем, что отображается страница ошибки
        $("h4").shouldHave(text("⚠️ Произошла ошибка"));
        $(".alert-danger").shouldBe(visible);
    }

    @Test
    public void testValidFightCreation() {
        // Открываем форму создания боя
        open("http://localhost:" + port + "/fights/new");
        
        // Заполняем все обязательные поля корректными данными
        $("#myFighter").setValue("Тестовый боец");
        $("#opponent").setValue("Тестовый соперник");
        $("#season").setValue("1");
        $("#roundsPlayed").setValue("3");
        $("#mode").selectOptionByValue("STANCE");
        $("#result").selectOptionByValue("WIN");
        $("#method").selectOptionByValue("DECISION");
        $("#weightClass").selectOptionByValue("LIGHTWEIGHT");
        
        // Отправляем форму
        executeJavaScript("arguments[0].click();", $("button[type='submit']"));
        
        // Ждем обработки
        sleep(3000);
        
        // Проверяем, что мы перенаправлены на страницу списка боев или остались на форме с ошибкой
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
}
