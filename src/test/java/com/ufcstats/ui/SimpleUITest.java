package com.ufcstats.ui;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Простой UI тест для проверки базовой функциональности
 */
@Slf4j
@DisplayName("Простой UI тест")
class SimpleUITest extends BaseUITest {

    @Test
    @DisplayName("Проверка загрузки главной страницы")
    void testHomePageLoads() {
        log.info("Тест: Проверка загрузки главной страницы");
        
        // Переход на главную страницу
        navigateTo("/");
        waitForPageLoad();
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("UFC Stats");
        
        // Проверка наличия основных элементов
        WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        assertThat(title.isDisplayed()).isTrue();
        
        log.info("✓ Главная страница загружается корректно");
    }

    @Test
    @DisplayName("Проверка навигации на страницу боев")
    void testNavigationToFightsPage() {
        log.info("Тест: Проверка навигации на страницу боев");
        
        // Переход на главную страницу
        navigateTo("/");
        waitForPageLoad();
        
        // Клик по ссылке "Бои"
        WebElement fightsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Бои")));
        fightsLink.click();
        
        // Проверка перенаправления
        wait.until(ExpectedConditions.urlContains("/fights"));
        
        // Проверка заголовка страницы
        assertThat(getPageTitle()).contains("Список боев");
        
        log.info("✓ Навигация на страницу боев работает корректно");
    }
}
