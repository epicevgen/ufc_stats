# UFC Stats - Система статистики боев UFC 5

Веб-приложение для хранения и анализа статистики боев UFC 5 с возможностью безопасных обновлений и тестирования.

## 🚀 Технологический стек

- **Backend**: Spring Boot 3.2, Java 17
- **Frontend**: Thymeleaf, Bootstrap 5, Chart.js
- **База данных**: H2 (разработка), PostgreSQL (продакшн)
- **Миграции**: Flyway
- **Безопасность**: Spring Security
- **Контейнеризация**: Docker, Docker Compose
- **CI/CD**: GitHub Actions
- **Тестирование**: JUnit 5, Testcontainers

## 📋 Требования

- Java 17+
- Docker и Docker Compose (опционально)
- Git

## 🛠️ Быстрый старт

### Локальная разработка

1. **Клонирование репозитория**
   ```bash
   git clone <repository-url>
   cd ufc_stats
   ```

2. **Запуск приложения**
   ```bash
   ./gradlew bootRun
   ```

3. **Открытие в браузере**
   - Приложение: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
   - Логин по умолчанию: admin / admin123

### Docker (рекомендуется)

1. **Запуск в режиме разработки**
   ```bash
   docker-compose up ufc-stats-dev
   ```

2. **Запуск в продакшн режиме**
   ```bash
   docker-compose up ufc-stats-prod postgres
   ```

## 🏗️ Структура проекта

```
ufc_stats/
├── src/main/java/com/ufcstats/
│   ├── controller/          # REST контроллеры
│   ├── service/            # Бизнес-логика
│   ├── repository/         # Доступ к данным
│   ├── model/              # Модели данных
│   ├── config/             # Конфигурация
│   ├── dto/                # Data Transfer Objects
│   └── exception/          # Обработка ошибок
├── src/main/resources/
│   ├── static/             # CSS, JS, изображения
│   ├── templates/          # HTML шаблоны
│   └── db/migration/       # Миграции БД
├── src/test/               # Тесты
├── docker/                 # Docker конфигурация
└── .github/workflows/      # CI/CD
```

## 🔧 Конфигурация

### Профили приложения

- **dev** (по умолчанию): H2 база данных, отладочные логи
- **prod**: PostgreSQL, оптимизированные настройки
- **test**: H2 в памяти для тестов

### Переменные окружения

```bash
# База данных
DATABASE_URL=jdbc:postgresql://localhost:5432/ufc_stats
DATABASE_USERNAME=ufc_stats
DATABASE_PASSWORD=password

# Администратор
ADMIN_USERNAME=admin
ADMIN_PASSWORD=secure_password
```

## 🧪 Тестирование

### Типы тестов

- **Unit тесты** - тестирование отдельных компонентов
- **Интеграционные тесты** - тестирование взаимодействия компонентов
- **UI тесты** - автоматизированное тестирование пользовательского интерфейса с Selenium WebDriver
- **API тесты** - тестирование REST API endpoints

```bash
# Запуск unit и интеграционных тестов
./gradlew test

# Запуск UI тестов
./scripts/run-ui-tests.sh

# Запуск конкретного UI теста
./gradlew test --tests "SimpleUITest"

# Запуск UI тестов с отображением браузера
./gradlew test --tests "*UITest" -Dui.test.headless=false

# Запуск с отчетом
./gradlew test jacocoTestReport

# Интеграционные тесты
./gradlew integrationTest
```

## 🚀 Развертывание

### Blue-Green Deployment

1. **Подготовка новой версии**
   ```bash
   docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d ufc-stats-green
   ```

2. **Переключение трафика**
   ```bash
   # Обновление nginx конфигурации
   docker-compose exec nginx nginx -s reload
   ```

3. **Остановка старой версии**
   ```bash
   docker-compose stop ufc-stats-blue
   ```

### Автоматическое развертывание

CI/CD pipeline автоматически:
- Запускает тесты при каждом push
- Сканирует на уязвимости
- Собирает Docker образ
- Развертывает в продакшн (при push в main)

## 📊 Мониторинг

- **Health Check**: `/actuator/health`
- **Метрики**: `/actuator/metrics`
- **Информация**: `/actuator/info`

## 🔒 Безопасность

- Аутентификация через Spring Security
- Хеширование паролей с BCrypt
- Защита от CSRF атак
- Валидация входных данных
- Сканирование уязвимостей в CI/CD

## 📝 Разработка

### Добавление новой функциональности

1. Создайте feature branch
2. Реализуйте изменения с тестами
3. Создайте Pull Request
4. После review - merge в develop
5. Тестирование в staging
6. Merge в main для продакшн

### Миграции базы данных

```bash
# Создание новой миграции
./gradlew flywayInfo

# Применение миграций
./gradlew flywayMigrate
```

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch
3. Внесите изменения с тестами
4. Создайте Pull Request

## 📄 Лицензия

Этот проект лицензирован под MIT License.

## 🆘 Поддержка

При возникновении проблем:
1. Проверьте логи: `docker-compose logs ufc-stats-dev`
2. Проверьте статус: `curl http://localhost:8080/actuator/health`
3. Создайте Issue в репозитории

---

**Примечание**: Бизнес-логика и модели данных будут добавлены после получения требований от пользователя.
