# UI Тесты для UFC Stats

## Обзор

UI тесты обеспечивают автоматизированное тестирование пользовательского интерфейса приложения UFC Stats. Тесты проверяют корректность создания, просмотра, редактирования и удаления боев через веб-интерфейс.

## Структура тестов

### Базовые классы

- **`BaseUITest`** - базовый класс для всех UI тестов с настройкой WebDriver
- **`FightCRUDUITest`** - тесты CRUD операций с боями
- **`FightFormUITest`** - тесты формы создания/редактирования боя
- **`FightDetailsUITest`** - тесты страницы детального просмотра боя
- **`IntegrationUITest`** - интеграционные тесты с Testcontainers

### Технологии

- **Selenium WebDriver** - автоматизация браузера
- **WebDriverManager** - управление драйверами браузеров
- **JUnit 5** - фреймворк тестирования
- **Testcontainers** - интеграционные тесты с контейнерами
- **AssertJ** - библиотека утверждений

## Запуск тестов

### Локальный запуск

```bash
# Запуск всех UI тестов
./scripts/run-ui-tests.sh

# Запуск конкретного класса тестов
./gradlew test --tests "FightCRUDUITest"

# Запуск с отображением браузера (не headless)
./gradlew test --tests "*UITest" -Dui.test.headless=false
```

### Запуск в CI/CD

UI тесты автоматически запускаются в GitHub Actions при:
- Push в ветки `main` или `develop`
- Создании Pull Request

## Конфигурация

### Переменные окружения

- `CHROME_HEADLESS` - запуск Chrome в headless режиме (по умолчанию: true)
- `UI_TEST_TIMEOUT` - таймаут для ожиданий (по умолчанию: 10 секунд)
- `UI_TEST_SCREENSHOT_ON_FAILURE` - создание скриншотов при ошибках (по умолчанию: true)

### Настройки браузера

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless"); // Headless режим
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--window-size=1920,1080");
```

## Покрытие тестами

### CRUD операции
- ✅ Создание нового боя
- ✅ Просмотр списка боев
- ✅ Просмотр деталей боя
- ✅ Редактирование боя
- ✅ Удаление боя

### Форма боя
- ✅ Отображение формы
- ✅ Заполнение всех полей
- ✅ Валидация обязательных полей
- ✅ Валидация числовых полей
- ✅ Динамическое добавление раундов
- ✅ Динамическое добавление судейских оценок
- ✅ Заполнение статистики раундов
- ✅ Заполнение судейских оценок

### Страница деталей
- ✅ Отображение основной информации
- ✅ Отображение статистики раундов
- ✅ Отображение судейских оценок
- ✅ Отображение графиков и визуализации
- ✅ Переключение между графиками
- ✅ Кнопки действий
- ✅ Навигация

### Интеграционные тесты
- ✅ Полный цикл работы с боем
- ✅ Навигация между страницами
- ✅ Статистика и аналитика
- ✅ Производительность
- ✅ Обработка ошибок

## Отчеты и артефакты

### Отчеты тестов
- **JUnit отчеты**: `build/test-results/test/`
- **HTML отчеты**: `build/reports/tests/test/`
- **Скриншоты ошибок**: `build/screenshots/`

### Логи
- **Детальные логи**: `build/logs/ui-tests.log`
- **Скриншоты**: автоматически создаются при ошибках

## Устранение неполадок

### Частые проблемы

1. **Chrome не найден**
   ```bash
   # Ubuntu/Debian
   sudo apt-get install google-chrome-stable
   
   # macOS
   brew install --cask google-chrome
   ```

2. **Таймауты в тестах**
   - Увеличьте значение `UI_TEST_TIMEOUT`
   - Проверьте производительность приложения

3. **Проблемы с headless режимом**
   ```bash
   # Запуск с отображением браузера
   ./gradlew test --tests "*UITest" -Dui.test.headless=false
   ```

### Отладка

```java
// Добавление паузы для отладки
Thread.sleep(5000);

// Создание скриншота
driver.getScreenshotAs(OutputType.FILE);

// Логирование текущего URL
log.info("Current URL: {}", driver.getCurrentUrl());
```

## Лучшие практики

### Написание тестов

1. **Используйте явные ожидания**
   ```java
   wait.until(ExpectedConditions.elementToBeClickable(By.id("button")));
   ```

2. **Группируйте связанные действия**
   ```java
   private void fillFightForm() {
       // Все действия по заполнению формы
   }
   ```

3. **Используйте Page Object Model для сложных страниц**
   ```java
   public class FightFormPage {
       private WebDriver driver;
       
       public void fillMyFighter(String name) {
           driver.findElement(By.id("myFighter")).sendKeys(name);
       }
   }
   ```

### Поддержка тестов

1. **Регулярно обновляйте селекторы** при изменении UI
2. **Добавляйте новые тесты** при добавлении функциональности
3. **Используйте стабильные селекторы** (ID, data-attributes)
4. **Избегайте хрупких селекторов** (CSS классы, XPath)

## Мониторинг и уведомления

### GitHub Actions
- Автоматический запуск при изменениях
- Отчеты о результатах в Pull Requests
- Уведомления о сбоях

### Локальная разработка
- Запуск тестов перед коммитом
- Проверка регрессий при рефакторинге
- Валидация новых функций

## Расширение тестов

### Добавление новых тестов

1. Создайте новый класс, наследующий от `BaseUITest`
2. Добавьте аннотации `@Test` и `@DisplayName`
3. Используйте существующие утилиты для заполнения форм
4. Добавьте тест в CI/CD pipeline

### Пример нового теста

```java
@Test
@DisplayName("Новый функционал")
void testNewFeature() {
    log.info("Тест: Новый функционал");
    
    navigateTo("/new-feature");
    waitForPageLoad();
    
    // Тестовые действия
    WebElement element = driver.findElement(By.id("new-element"));
    assertThat(element.isDisplayed()).isTrue();
    
    log.info("✓ Новый функционал работает корректно");
}
```

## Контакты

При возникновении вопросов или проблем с UI тестами:
- Создайте Issue в GitHub репозитории
- Обратитесь к команде разработки
- Проверьте документацию по Selenium WebDriver
