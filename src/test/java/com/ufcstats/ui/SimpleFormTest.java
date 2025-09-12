package com.ufcstats.ui;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Простой тест для отладки проблемы с формой
 */
@DisplayName("Простой тест формы")
public class SimpleFormTest extends SelenideBaseTest {

    private static final Logger log = LoggerFactory.getLogger(SimpleFormTest.class);

    @Test
    void testSimpleFormFill() {
        log.info("Начинаем простой тест заполнения формы");
        
        // Переходим на страницу создания боя
        open(getBaseUrl() + "/fights/new");
        
        // Проверяем, что форма загрузилась
        $("form").shouldBe(visible);
        $("h2").shouldHave(text("Новый бой"));
        
        // Ждем загрузки
        sleep(3000);
        
        // Пробуем заполнить одно поле
        log.info("Пробуем заполнить поле myFighter");
        SelenideElement myFighterField = $("#myFighter");
        myFighterField.shouldBe(visible);
        
        log.info("Очищаем поле");
        myFighterField.clear();
        
        log.info("Заполняем поле");
        myFighterField.setValue("Тестовый боец");
        
        sleep(2000);
        
        // Проверяем, что поле заполнилось
        String value = myFighterField.getValue();
        log.info("Значение поля myFighter: '{}'", value);
        
        // Проверяем через JavaScript
        String jsValue = executeJavaScript("return document.getElementById('myFighter').value;");
        log.info("Значение поля myFighter через JavaScript: '{}'", jsValue);
        
        // Проверяем HTML
        String html = myFighterField.getAttribute("value");
        log.info("Значение поля myFighter через HTML атрибут: '{}'", html);
        
        log.info("Простой тест завершен");
    }
}
