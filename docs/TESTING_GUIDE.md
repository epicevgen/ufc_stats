# Руководство по тестированию UFC Stats

Это руководство описывает, как создавать и поддерживать различные типы тестов в проекте UFC Stats.

## 📋 Содержание

1. [Обзор тестирования](#обзор-тестирования)
2. [UI тесты (Selenide)](#ui-тесты-selenide)
3. [Unit тесты (JUnit 5)](#unit-тесты-junit-5)
4. [API тесты (REST Assured)](#api-тесты-rest-assured)
5. [Интеграционные тесты](#интеграционные-тесты)
6. [Запуск тестов](#запуск-тестов)
7. [Лучшие практики](#лучшие-практики)

## 🎯 Обзор тестирования

Проект использует многоуровневую стратегию тестирования:

| **Тип теста** | **Технология** | **Покрытие** | **Скорость** |
|---------------|-----------------|--------------|--------------|
| **UI тесты** | Selenide | Пользовательский интерфейс | Медленно |
| **API тесты** | REST Assured | HTTP API | Быстро |
| **Unit тесты** | JUnit 5 + Mockito | Бизнес-логика | Очень быстро |
| **Интеграционные** | Spring Boot Test | Полная интеграция | Средне |

## 🎭 UI тесты (Selenide)

### Создание UI теста

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MyUITest extends SelenideBaseTest {

    @Test
    void testUserWorkflow() {
        // Открываем страницу
        open("/fights");
        
        // Проверяем элементы
        $("#fights-table").shouldBe(visible);
        
        // Выполняем действия
        $("#add-fight-btn").click();
        
        // Проверяем результат
        $(".success-message").shouldBe(visible);
    }
}
```

### Основные команды Selenide

```java
// Навигация
open("/fights");
open("http://localhost:8080/statistics");

// Поиск элементов
$("#element-id").shouldBe(visible);
$(".css-class").shouldHave(text("Ожидаемый текст"));
$("input[name='field']").setValue("Значение");

// Действия
$("#button").click();
$("#form").submit();

// Ожидания
$("#loading").should(disappear, Duration.ofSeconds(10));
$("#result").shouldHave(text("Успех"));

// Проверки
$("#table tbody tr").shouldHave(size(5));
$("#status").shouldHave(cssClass("success"));
```

### Конфигурация UI тестов

```java
// В SelenideBaseTest
@BeforeEach
void setUp() {
    Configuration.browser = "chrome";
    Configuration.headless = System.getProperty("selenide.headless", "false").equals("true");
    Configuration.timeout = 10000;
    Configuration.screenshots = true;
    Configuration.savePageSource = true;
}
```

## ⚙️ Unit тесты (JUnit 5)

### Создание Unit теста

```java
@ExtendWith(MockitoExtension.class)
class FightServiceTest {

    @Mock
    private FightRepository fightRepository;
    
    @InjectMocks
    private FightService fightService;

    @Test
    void testCreateFight() {
        // Arrange
        Fight fight = new Fight();
        fight.setMyFighter("Тест Боец");
        when(fightRepository.save(any(Fight.class))).thenReturn(fight);
        
        // Act
        Fight result = fightService.createFight(fight);
        
        // Assert
        assertNotNull(result);
        assertEquals("Тест Боец", result.getMyFighter());
        verify(fightRepository).save(fight);
    }
}
```

### Основные аннотации JUnit 5

```java
@BeforeEach  // Выполняется перед каждым тестом
@AfterEach   // Выполняется после каждого теста
@BeforeAll   // Выполняется один раз перед всеми тестами
@AfterAll    // Выполняется один раз после всех тестов
@DisplayName("Описание теста")  // Понятное имя теста
@ParameterizedTest  // Параметризованный тест
@ValueSource(strings = {"value1", "value2"})  // Источник параметров
```

### Мокирование с Mockito

```java
// Создание мока
@Mock
private FightRepository fightRepository;

// Настройка поведения
when(fightRepository.findById(1L)).thenReturn(Optional.of(fight));
when(fightRepository.save(any(Fight.class))).thenReturn(savedFight);

// Проверка вызовов
verify(fightRepository).save(fight);
verify(fightRepository, times(2)).findAll();
verify(fightRepository, never()).delete(any());
```

## 🌐 API тесты (REST Assured)

### Создание API теста

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FightApiTest extends BaseApiTest {

    @Test
    void testCreateFight() {
        Map<String, Object> fightData = ApiTestUtils.createFightData();
        
        givenPostRequest()
                .body(fightData)
                .when()
                .post("/api/fights")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", notNullValue())
                .body("myFighter", equalTo(fightData.get("myFighter")));
    }
}
```

### Основные команды REST Assured

```java
// GET запрос
given()
    .param("search", "тест")
    .when()
    .get("/api/fights")
    .then()
    .statusCode(200)
    .body("content", notNullValue());

// POST запрос
given()
    .contentType("application/json")
    .body(fightData)
    .when()
    .post("/api/fights")
    .then()
    .statusCode(201);

// PUT запрос
given()
    .contentType("application/json")
    .body(updateData)
    .when()
    .put("/api/fights/1")
    .then()
    .statusCode(200);

// DELETE запрос
given()
    .when()
    .delete("/api/fights/1")
    .then()
    .statusCode(204);
```

### Проверки ответов

```java
// Проверка статуса
.statusCode(200)
.statusCode(HttpStatus.OK.value())

// Проверка заголовков
.contentType("application/json")
.header("Content-Type", "application/json")

// Проверка тела ответа
.body("id", notNullValue())
.body("name", equalTo("Ожидаемое значение"))
.body("items", hasSize(5))
.body("items[0].name", equalTo("Первый элемент"))
.body("totalElements", greaterThan(0))
```

### Утилиты для API тестов

```java
// Создание тестовых данных
Map<String, Object> fightData = ApiTestUtils.createFightData();
Map<String, Object> lossData = ApiTestUtils.createLossFightData();

// Создание и очистка
Long fightId = ApiTestUtils.createAndCleanupFight(fightData);

// Проверка ответа
ApiTestUtils.assertFightResponse(response, expectedData);
```

## 🔗 Интеграционные тесты

### Создание интеграционного теста

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private FightRepository fightRepository;

    @Test
    @Transactional
    void testCreateFightIntegration() throws Exception {
        Fight fight = new Fight();
        fight.setMyFighter("Интеграционный Тест");
        
        mockMvc.perform(post("/api/fights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.myFighter").value("Интеграционный Тест"));
    }
}
```

## 🚀 Запуск тестов

### Команды Gradle

```bash
# Все тесты
./gradlew test

# Только UI тесты
./gradlew test --tests "*UITest*"

# Только API тесты
./gradlew test --tests "*ApiTest*"

# Только Unit тесты
./gradlew test --tests "*Test" --exclude-task "*UITest*" --exclude-task "*ApiTest*"

# Конкретный тест
./gradlew test --tests "FightApiTest.testCreateFight"

# UI тесты с браузером
./gradlew test --tests "*UITest*" -Dselenide.headless=false

# С отчетом о покрытии
./gradlew test jacocoTestReport
```

### Профили тестирования

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
selenide.headless=true
logging.level.com.ufcstats=DEBUG
```

## 📊 Лучшие практики

### 1. Структура тестов

```
src/test/java/com/ufcstats/
├── api/                    # API тесты
│   ├── BaseApiTest.java
│   ├── FightApiTest.java
│   ├── StatisticsApiTest.java
│   └── ApiTestUtils.java
├── ui/                     # UI тесты
│   ├── SelenideBaseTest.java
│   └── ComprehensiveCRUDTest.java
├── service/                # Unit тесты
│   └── FightServiceTest.java
├── integration/            # Интеграционные тесты
│   └── FightIntegrationTest.java
└── model/                  # Тесты моделей
    ├── ModelTest.java
    └── enums/EnumTest.java
```

### 2. Именование тестов

```java
// Хорошо
@Test
void testCreateFight_WithValidData_ShouldReturnCreatedFight() {
    // тест
}

@Test
void testGetFight_WithNonExistentId_ShouldReturnNotFound() {
    // тест
}

// Плохо
@Test
void test1() {
    // тест
}
```

### 3. Подготовка данных

```java
@BeforeEach
void setUp() {
    // Очистка базы данных
    fightRepository.deleteAll();
    
    // Создание тестовых данных
    createTestFights();
}

@AfterEach
void tearDown() {
    // Очистка после теста
    fightRepository.deleteAll();
}
```

### 4. Изоляция тестов

```java
// Используйте уникальные данные
String uniqueName = "Тест " + System.currentTimeMillis();

// Очищайте данные после тестов
@AfterEach
void cleanup() {
    fightRepository.deleteAll();
}
```

### 5. Логирование

```java
@Slf4j
public class MyTest {
    @Test
    void testSomething() {
        log.info("Начинаем тест: {}", testName);
        // выполнение теста
        log.info("Тест завершен успешно");
    }
}
```

### 6. Обработка ошибок

```java
@Test
void testWithErrorHandling() {
    try {
        // выполнение операции
        performOperation();
    } catch (Exception e) {
        log.error("Ошибка в тесте: {}", e.getMessage());
        fail("Тест не должен падать с ошибкой: " + e.getMessage());
    }
}
```

## 📈 Отчеты о покрытии

### Генерация отчета

```bash
./gradlew test jacocoTestReport
```

### Просмотр отчета

```bash
open build/reports/jacoco/test/html/index.html
```

### Настройка покрытия

```gradle
jacoco {
    toolVersion = "0.8.8"
}

jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}
```

## 🔧 Отладка тестов

### UI тесты

```java
// Скриншоты при ошибках
Configuration.screenshots = true;
Configuration.savePageSource = true;

// Пауза для отладки
Selenide.sleep(5000);
```

### API тесты

```java
// Подробное логирование
RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

// Проверка ответа
Response response = given().when().get("/api/fights");
log.info("Response: {}", response.asString());
```

### Unit тесты

```java
// Отладочная информация
log.debug("Repository state: {}", fightRepository.findAll());
log.debug("Service result: {}", result);
```

## 📝 Заключение

Этот проект имеет полную инфраструктуру для всех типов тестирования:

- ✅ **UI тесты** - Selenide для тестирования пользовательского интерфейса
- ✅ **API тесты** - REST Assured для тестирования HTTP API
- ✅ **Unit тесты** - JUnit 5 + Mockito для тестирования бизнес-логики
- ✅ **Интеграционные тесты** - Spring Boot Test для полной интеграции

Следуйте этому руководству для создания качественных и надежных тестов! 🚀
