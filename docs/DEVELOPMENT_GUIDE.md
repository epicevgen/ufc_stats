# Руководство по разработке UFC Stats

## Начало работы

### Предварительные требования
- **Java 17** или выше
- **Gradle 7.0** или выше
- **Git** для управления версиями
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Установка и настройка

1. **Клонирование репозитория**:
   ```bash
   git clone https://github.com/epicevgen/ufc_stats.git
   cd ufc_stats
   ```

2. **Проверка Java версии**:
   ```bash
   java -version
   # Должна быть версия 17 или выше
   ```

3. **Сборка проекта**:
   ```bash
   ./gradlew build
   ```

4. **Запуск приложения**:
   ```bash
   ./gradlew bootRun
   ```

## Структура проекта

### Директории
```
ufc_stats/
├── src/
│   ├── main/
│   │   ├── java/com/ufcstats/     # Исходный код Java
│   │   └── resources/             # Ресурсы (шаблоны, статика)
│   └── test/                      # Тесты
├── docs/                          # Документация
├── scripts/                       # Скрипты для автоматизации
├── backups/                       # Резервные копии БД
├── data/                          # Файлы базы данных
└── build.gradle                   # Конфигурация сборки
```

### Основные пакеты

#### `com.ufcstats.config`
Конфигурация приложения:
- **SecurityConfig** - Настройки безопасности
- **DataInitializer** - Инициализация данных
- **DateTimeConfig** - Настройки даты/времени

#### `com.ufcstats.controller`
Контроллеры для обработки запросов:
- **WebController** - Веб-страницы
- **FightController** - REST API для боев
- **TestDataController** - Генерация тестовых данных

#### `com.ufcstats.model`
Модели данных:
- **Fight** - Основная модель боя
- **enums/** - Перечисления (результаты, режимы, методы)

#### `com.ufcstats.service`
Бизнес-логика:
- **FightService** - Основные операции с боями
- **AdvancedStatisticsService** - Расширенная аналитика
- **FighterStatisticsService** - Статистика бойца

## Разработка новых функций

### 1. Добавление нового поля в модель Fight

```java
// В классе Fight
@Column(name = "new_field")
private String newField;

// Геттер и сеттер
public String getNewField() { return newField; }
public void setNewField(String newField) { this.newField = newField; }
```

### 2. Создание нового сервиса

```java
@Service
@Transactional
public class NewService {
    
    private final FightRepository fightRepository;
    
    public NewService(FightRepository fightRepository) {
        this.fightRepository = fightRepository;
    }
    
    public void doSomething() {
        // Бизнес-логика
    }
}
```

### 3. Добавление нового контроллера

```java
@Controller
@RequestMapping("/new")
public class NewController {
    
    private final NewService newService;
    
    public NewController(NewService newService) {
        this.newService = newService;
    }
    
    @GetMapping
    public String index(Model model) {
        // Логика контроллера
        return "new-page";
    }
}
```

### 4. Создание нового шаблона

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Новая страница</title>
</head>
<body>
    <h1>Новая страница</h1>
    <!-- Содержимое страницы -->
</body>
</html>
```

## Работа с базой данных

### Миграции
Создание новой миграции:

1. Создайте файл в `src/main/resources/db/migration/`
2. Название: `V3__Description.sql`
3. Содержимое:
   ```sql
   -- Добавление нового поля
   ALTER TABLE fight ADD COLUMN new_field VARCHAR(255);
   ```

### Запросы к БД
Использование репозиториев:

```java
@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {
    
    @Query("SELECT f FROM Fight f WHERE f.result = :result")
    List<Fight> findByResult(@Param("result") FightResult result);
    
    @Query("SELECT COUNT(f) FROM Fight f WHERE f.fightMode = :mode")
    long countByFightMode(@Param("mode") FightMode mode);
}
```

## Тестирование

### Unit тесты
```java
@Test
void testFightService() {
    // Arrange
    Fight fight = new Fight();
    fight.setResult(FightResult.WIN);
    
    // Act
    Fight savedFight = fightService.createFight(fight);
    
    // Assert
    assertThat(savedFight.getId()).isNotNull();
    assertThat(savedFight.getResult()).isEqualTo(FightResult.WIN);
}
```

### Integration тесты
```java
@SpringBootTest
@AutoConfigureTestDatabase
class FightIntegrationTest {
    
    @Autowired
    private FightService fightService;
    
    @Test
    void testCreateFight() {
        // Тест интеграции
    }
}
```

### UI тесты
```java
@ExtendWith(SelenideExtension.class)
class FightUITest {
    
    @Test
    void testCreateFight() {
        open("/fights");
        $("#add-fight-btn").click();
        // Тест UI
    }
}
```

## Отладка и профилирование

### Логирование
```java
@Slf4j
public class FightService {
    
    public void createFight(Fight fight) {
        log.debug("Создание боя: {}", fight);
        // Логика
        log.info("Бой создан с ID: {}", fight.getId());
    }
}
```

### Профилирование
Включение профилирования в `application.properties`:
```properties
# Логирование SQL
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Профилирование
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Оптимизация производительности

### Кэширование
```java
@Service
public class FightService {
    
    @Cacheable("fights")
    public List<Fight> getAllFights() {
        return fightRepository.findAll();
    }
}
```

### Оптимизация запросов
```java
@Query("SELECT f FROM Fight f LEFT JOIN FETCH f.rounds WHERE f.id = :id")
Fight findByIdWithRounds(@Param("id") Long id);
```

## Развертывание

### Локальное развертывание
```bash
# Сборка JAR файла
./gradlew bootJar

# Запуск JAR файла
java -jar build/libs/ufc-stats-1.0.0.jar
```

### Настройка для продакшна
Создайте `application-prod.properties`:
```properties
# Настройки для продакшна
server.port=8080
spring.datasource.url=jdbc:h2:file:./data/ufc_stats
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

## Устранение неполадок

### Частые проблемы

#### 1. Ошибка "База данных уже используется"
```bash
# Остановите все процессы Java
pkill -f java

# Удалите lock файлы
rm -f data/ufc_stats.mv.db.lock
```

#### 2. Ошибка компиляции
```bash
# Очистите кэш Gradle
./gradlew clean

# Пересоберите проект
./gradlew build
```

#### 3. Проблемы с портом
```bash
# Проверьте, что порт 8080 свободен
lsof -i :8080

# Убейте процесс, если нужно
kill -9 <PID>
```

### Логи и отладка
```bash
# Просмотр логов приложения
tail -f logs/application.log

# Запуск с отладочными логами
./gradlew bootRun --debug
```

## Лучшие практики

### Код
- Используйте аннотации Lombok для уменьшения boilerplate кода
- Следуйте принципам SOLID
- Пишите тесты для новой функциональности
- Документируйте сложную логику

### Git
- Используйте осмысленные сообщения коммитов
- Создавайте ветки для новых функций
- Делайте регулярные коммиты
- Используйте .gitignore для исключения ненужных файлов

### Производительность
- Оптимизируйте SQL запросы
- Используйте кэширование где возможно
- Мониторьте использование памяти
- Тестируйте на больших объемах данных

## Полезные команды

### Gradle
```bash
# Сборка проекта
./gradlew build

# Запуск тестов
./gradlew test

# Запуск приложения
./gradlew bootRun

# Очистка
./gradlew clean

# Создание JAR
./gradlew bootJar
```

### Git
```bash
# Проверка статуса
git status

# Добавление изменений
git add .

# Коммит
git commit -m "Описание изменений"

# Отправка в репозиторий
git push origin master
```

### База данных
```bash
# Создание бэкапа
./scripts/backup-db.sh backup

# Восстановление из бэкапа
./scripts/backup-db.sh restore backups/backup.zip

# Просмотр бэкапов
./scripts/backup-db.sh list
```
