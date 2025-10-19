# Руководство по устранению неполадок UFC Stats

## Частые проблемы и решения

### 🚨 Критические ошибки

#### 1. Ошибка "База данных уже используется"
**Симптомы:**
```
Database may be already in use: "/path/to/ufc_stats.mv.db"
Possible solutions: close all other connection(s); use the server mode
```

**Причины:**
- Приложение уже запущено в другом процессе
- Неправильное завершение предыдущего запуска
- Блокировка файла БД

**Решения:**
```bash
# 1. Остановите все процессы Java
pkill -f java
pkill -f "ufc-stats"
pkill -f "gradle"

# 2. Удалите lock файлы
rm -f data/ufc_stats.mv.db.lock
rm -f data/ufc_stats.trace.db.lock

# 3. Подождите 5 секунд
sleep 5

# 4. Запустите приложение заново
./gradlew bootRun
```

#### 2. Ошибка "Порт 8080 уже используется"
**Симптомы:**
```
Port 8080 was already in use
```

**Решения:**
```bash
# 1. Найдите процесс, использующий порт
lsof -i :8080

# 2. Убейте процесс
kill -9 <PID>

# 3. Или используйте другой порт
./gradlew bootRun --args='--server.port=8081'
```

#### 3. Ошибка компиляции "ClassNotFoundException"
**Симптомы:**
```
java.lang.ClassNotFoundException: com.ufcstats.repository.FightRepository
```

**Решения:**
```bash
# 1. Очистите кэш Gradle
./gradlew clean

# 2. Пересоберите проект
./gradlew build

# 3. Если не помогает, удалите .gradle
rm -rf .gradle
./gradlew build
```

### ⚠️ Предупреждения

#### 1. Предупреждение "No JTA platform available"
**Симптомы:**
```
HHH000489: No JTA platform available
```

**Решение:**
Это предупреждение не критично. Для отключения добавьте в `application.properties`:
```properties
spring.jpa.properties.hibernate.transaction.jta.platform=none
```

#### 2. Предупреждение "favicon.ico not found"
**Симптомы:**
```
No static resource favicon.ico
```

**Решение:**
Добавьте favicon.ico в `src/main/resources/static/` или игнорируйте это предупреждение.

### 🔧 Проблемы с производительностью

#### 1. Медленная загрузка страниц
**Диагностика:**
```bash
# Проверьте использование памяти
jps -v | grep ufc-stats

# Проверьте логи
tail -f logs/application.log | grep "slow query"
```

**Решения:**
- Увеличьте память JVM: `-Xmx2g`
- Оптимизируйте SQL запросы
- Добавьте индексы в БД

#### 2. Высокое использование CPU
**Диагностика:**
```bash
# Мониторинг процессов
top -p $(pgrep java)

# Профилирование
jstack $(pgrep java)
```

**Решения:**
- Проверьте бесконечные циклы в коде
- Оптимизируйте алгоритмы
- Добавьте кэширование

### 🗄️ Проблемы с базой данных

#### 1. Ошибка "Table doesn't exist"
**Симптомы:**
```
Table "FIGHT" doesn't exist
```

**Решения:**
```bash
# 1. Удалите файлы БД
rm -f data/ufc_stats.mv.db
rm -f data/ufc_stats.trace.db

# 2. Запустите приложение (создастся новая БД)
./gradlew bootRun
```

#### 2. Ошибка "Database is corrupted"
**Симптомы:**
```
Database is corrupted
```

**Решения:**
```bash
# 1. Восстановите из бэкапа
./scripts/backup-db.sh restore backups/latest_backup.zip

# 2. Если бэкапа нет, создайте новую БД
rm -f data/ufc_stats.*
./gradlew bootRun
```

#### 3. Ошибка "Out of memory"
**Симптомы:**
```
OutOfMemoryError: Java heap space
```

**Решения:**
```bash
# Увеличьте память JVM
export JAVA_OPTS="-Xmx2g -Xms1g"
./gradlew bootRun
```

### 🌐 Проблемы с сетью

#### 1. Ошибка "Connection refused"
**Симптомы:**
```
Connection refused: localhost:8080
```

**Решения:**
```bash
# 1. Проверьте, что приложение запущено
curl http://localhost:8080

# 2. Проверьте порт
netstat -an | grep 8080

# 3. Перезапустите приложение
./gradlew bootRun
```

#### 2. Ошибка "Timeout"
**Симптомы:**
```
Read timeout
```

**Решения:**
- Увеличьте timeout в настройках
- Проверьте производительность БД
- Оптимизируйте запросы

### 📱 Проблемы с интерфейсом

#### 1. Страница не загружается
**Диагностика:**
```bash
# Проверьте логи
tail -f logs/application.log

# Проверьте доступность
curl -I http://localhost:8080
```

**Решения:**
- Очистите кэш браузера
- Проверьте консоль браузера на ошибки JavaScript
- Перезапустите приложение

#### 2. Ошибки JavaScript
**Диагностика:**
- Откройте консоль браузера (F12)
- Проверьте ошибки в Network tab

**Решения:**
- Проверьте пути к статическим ресурсам
- Убедитесь, что все JS файлы загружаются
- Проверьте синтаксис JavaScript

### 🔒 Проблемы с безопасностью

#### 1. Ошибка "Access denied"
**Симптомы:**
```
Access denied for user
```

**Решения:**
- Проверьте настройки Spring Security
- Убедитесь, что пользователь авторизован
- Проверьте права доступа к файлам

#### 2. Ошибка "CSRF token"
**Симптомы:**
```
Invalid CSRF token
```

**Решения:**
- Очистите cookies браузера
- Проверьте настройки CSRF в SecurityConfig
- Убедитесь, что формы содержат CSRF токены

### 🧪 Проблемы с тестами

#### 1. Тесты не проходят
**Диагностика:**
```bash
# Запустите тесты с подробным выводом
./gradlew test --info
```

**Решения:**
- Проверьте, что БД для тестов создается правильно
- Убедитесь, что все зависимости доступны
- Проверьте настройки тестовой БД

#### 2. UI тесты падают
**Диагностика:**
```bash
# Запустите UI тесты
./gradlew test --tests "*UITest*"
```

**Решения:**
- Убедитесь, что приложение запущено
- Проверьте, что браузер установлен
- Проверьте настройки Selenide

### 📊 Мониторинг и диагностика

#### Полезные команды для диагностики

```bash
# Проверка процессов Java
jps -v

# Мониторинг памяти
jstat -gc $(pgrep java)

# Анализ heap dump
jmap -dump:format=b,file=heap.hprof $(pgrep java)

# Проверка сетевых соединений
netstat -an | grep 8080

# Мониторинг файловой системы
df -h
du -sh data/

# Проверка логов
tail -f logs/application.log
grep "ERROR" logs/application.log
```

#### Создание диагностического отчета

```bash
# Создайте скрипт для сбора диагностической информации
cat > diagnose.sh << 'EOF'
#!/bin/bash
echo "=== UFC Stats Diagnostic Report ==="
echo "Date: $(date)"
echo ""

echo "=== System Info ==="
uname -a
java -version
echo ""

echo "=== Process Info ==="
jps -v | grep ufc
echo ""

echo "=== Memory Usage ==="
free -h
echo ""

echo "=== Disk Usage ==="
df -h
du -sh data/
echo ""

echo "=== Network ==="
netstat -an | grep 8080
echo ""

echo "=== Recent Logs ==="
tail -20 logs/application.log
EOF

chmod +x diagnose.sh
./diagnose.sh > diagnostic_report.txt
```

### 🆘 Экстренное восстановление

#### Полное восстановление системы

```bash
# 1. Остановите все процессы
pkill -f java
pkill -f gradle

# 2. Очистите все временные файлы
rm -rf .gradle
rm -rf build
rm -f data/ufc_stats.*.lock

# 3. Восстановите из бэкапа
./scripts/backup-db.sh restore backups/latest_backup.zip

# 4. Пересоберите проект
./gradlew clean build

# 5. Запустите приложение
./gradlew bootRun
```

#### Создание аварийного бэкапа

```bash
# Создайте аварийный бэкап перед изменениями
./scripts/backup-db.sh backup
cp data/ufc_stats.mv.db data/ufc_stats_emergency_backup.mv.db
```

### 📞 Получение помощи

#### Информация для отчета об ошибке

При обращении за помощью предоставьте:

1. **Версию Java**: `java -version`
2. **Версию Gradle**: `./gradlew --version`
3. **Операционную систему**: `uname -a`
4. **Полный стек ошибки**: из логов
5. **Шаги воспроизведения**: что делали перед ошибкой
6. **Диагностический отчет**: `./diagnose.sh`

#### Полезные ресурсы

- **Логи приложения**: `logs/application.log`
- **Логи Gradle**: `build/reports/tests/test/`
- **Конфигурация**: `application.properties`
- **Документация**: `docs/`

#### Контакты

- **Репозиторий**: https://github.com/epicevgen/ufc_stats
- **Issues**: GitHub Issues
- **Документация**: `docs/` в проекте
