# UFC Stats 🥊

**Веб-приложение для отслеживания и анализа статистики боев в игре UFC 5**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-7.0-blue.svg)](https://gradle.org/)
[![H2 Database](https://img.shields.io/badge/H2-Database-yellow.svg)](https://www.h2database.com/)

## 🚀 Быстрый старт

### Предварительные требования
- Java 17 или выше
- Gradle 7.0 или выше
- Git

### Установка и запуск

```bash
# 1. Клонирование репозитория
git clone https://github.com/epicevgen/ufc_stats.git
cd ufc_stats

# 2. Сборка проекта
./gradlew build

# 3. Запуск приложения
./gradlew bootRun
```

### Доступ к приложению
- **URL**: http://localhost:8080
- **Главная страница**: http://localhost:8080/
- **Список боев**: http://localhost:8080/fights
- **Статистика**: http://localhost:8080/statistics

## ✨ Основные возможности

### 🥊 Управление боями
- ✅ Добавление новых боев с детальной информацией
- ✅ Просмотр списка всех боев с фильтрацией
- ✅ Редактирование существующих боев
- ✅ Детальная статистика по каждому бою

### 📊 Аналитика и статистика
- ✅ Общая статистика побед/поражений/ничьих
- ✅ Статистика по режимам боев (ММА, Стойка)
- ✅ Анализ по весовым категориям
- ✅ Фильтрация по сезонам
- ✅ Визуализация данных с графиками

### 🎯 Дополнительные функции
- ✅ Генерация тестовых данных
- ✅ Автоматическое резервное копирование БД
- ✅ Адаптивный дизайн для всех устройств
- ✅ Быстрый поиск и фильтрация

## 🛠️ Технологический стек

### Backend
- **Java 17** - Основной язык программирования
- **Spring Boot 3.2.0** - Фреймворк для веб-приложений
- **Spring Data JPA** - Работа с базой данных
- **Spring Security** - Аутентификация и авторизация
- **H2 Database** - Встроенная база данных
- **Gradle** - Система сборки

### Frontend
- **Thymeleaf** - Шаблонизатор для серверного рендеринга
- **Bootstrap 5** - CSS фреймворк для адаптивного дизайна
- **Chart.js** - Библиотека для создания графиков
- **JavaScript** - Клиентская логика

### Тестирование
- **JUnit 5** - Unit тестирование
- **Selenide** - UI тестирование
- **REST Assured** - API тестирование
- **Spring Boot Test** - Интеграционное тестирование

## 📁 Структура проекта

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

## 🔧 Разработка

### Команды разработки

```bash
# Сборка проекта
./gradlew build

# Запуск тестов
./gradlew test

# Запуск приложения
./gradlew bootRun

# Создание JAR файла
./gradlew bootJar

# Очистка
./gradlew clean
```

### Добавление новых функций

1. **Создание новой модели**:
   ```java
   @Entity
   @Table(name = "new_entity")
   public class NewEntity {
       // Поля и методы
   }
   ```

2. **Создание нового сервиса**:
   ```java
   @Service
   @Transactional
   public class NewService {
       // Бизнес-логика
   }
   ```

3. **Создание нового контроллера**:
   ```java
   @Controller
   @RequestMapping("/new")
   public class NewController {
       // Обработка запросов
   }
   ```

## 💾 Резервное копирование

### Автоматическое резервное копирование

```bash
# Создание бэкапа
./scripts/backup-db.sh backup

# Восстановление из бэкапа
./scripts/backup-db.sh restore backups/backup.zip

# Просмотр списка бэкапов
./scripts/backup-db.sh list

# Очистка старых бэкапов
./scripts/backup-db.sh cleanup
```

### Настройка автоматического бэкапа

```bash
# Добавьте в crontab для ежедневного бэкапа в 2:00
crontab -e
# Добавьте строку:
0 2 * * * /path/to/ufc_stats/scripts/daily-backup.sh
```

## 🧪 Тестирование

### Запуск тестов

```bash
# Все тесты
./gradlew test

# Только unit тесты
./gradlew test --tests "*Test" --exclude-task "*UITest*" --exclude-task "*ApiTest*"

# Только UI тесты
./gradlew test --tests "*UITest*"

# Только API тесты
./gradlew test --tests "*ApiTest*"

# Интеграционные тесты
./gradlew test --tests "*IntegrationTest*"

# UI тесты с отображением браузера
./gradlew test --tests "*UITest*" -Dselenide.headless=false

# Тесты с подробным выводом
./gradlew test --info
```

### Покрытие тестами

```bash
# Генерация отчета о покрытии
./gradlew test jacocoTestReport

# Просмотр отчета
open build/reports/jacoco/test/html/index.html
```

### Типы тестов

| **Тип** | **Технология** | **Покрытие** | **Команда** |
|---------|----------------|--------------|-------------|
| **UI тесты** | Selenide | Пользовательский интерфейс | `./gradlew test --tests "*UITest*"` |
| **API тесты** | REST Assured | HTTP API | `./gradlew test --tests "*ApiTest*"` |
| **Unit тесты** | JUnit 5 + Mockito | Бизнес-логика | `./gradlew test --tests "*Test" --exclude-task "*UITest*" --exclude-task "*ApiTest*"` |
| **Интеграционные** | Spring Boot Test | Полная интеграция | `./gradlew test --tests "*IntegrationTest*"` |

## 🚀 Развертывание

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

## 📚 Документация

- **[Обзор проекта](docs/PROJECT_OVERVIEW.md)** - Подробное описание проекта
- **[Руководство по разработке](docs/DEVELOPMENT_GUIDE.md)** - Как разрабатывать новые функции
- **[Руководство по тестированию](docs/TESTING_GUIDE.md)** - Создание и поддержка тестов
- **[Устранение неполадок](docs/TROUBLESHOOTING.md)** - Решение проблем
- **[Резервное копирование](backups/README.md)** - Управление бэкапами

## 🐛 Устранение неполадок

### Частые проблемы

#### 1. Ошибка "База данных уже используется"
```bash
# Остановите все процессы Java
pkill -f java

# Удалите lock файлы
rm -f data/ufc_stats.mv.db.lock

# Запустите приложение заново
./gradlew bootRun
```

#### 2. Ошибка "Порт 8080 уже используется"
```bash
# Найдите процесс, использующий порт
lsof -i :8080

# Убейте процесс
kill -9 <PID>
```

#### 3. Ошибка компиляции
```bash
# Очистите кэш Gradle
./gradlew clean

# Пересоберите проект
./gradlew build
```

## 🤝 Участие в разработке

1. Форкните репозиторий
2. Создайте ветку для новой функции (`git checkout -b feature/amazing-feature`)
3. Зафиксируйте изменения (`git commit -m 'Add amazing feature'`)
4. Отправьте в ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект распространяется под лицензией MIT. См. файл `LICENSE` для подробностей.

## 👨‍💻 Автор

**Evgenij Stolarov**
- GitHub: [@epicevgen](https://github.com/epicevgen)
- Email: [ваш-email@example.com]

## 🙏 Благодарности

- Spring Boot команде за отличный фреймворк
- Bootstrap команде за красивый UI
- Chart.js команде за отличные графики
- Всем контрибьюторам проекта

---

**Сделано с ❤️ для сообщества UFC 5**