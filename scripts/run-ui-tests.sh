#!/bin/bash

# Скрипт для запуска UI тестов

set -e

echo "🚀 Запуск UI тестов для UFC Stats"

# Проверка наличия Java
if ! command -v java &> /dev/null; then
    echo "❌ Java не найдена. Установите Java 17 или выше."
    exit 1
fi

# Проверка версии Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Требуется Java 17 или выше. Текущая версия: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java версия: $JAVA_VERSION"

# Проверка наличия Chrome
if ! command -v google-chrome &> /dev/null && ! command -v chromium-browser &> /dev/null; then
    echo "⚠️  Chrome не найден. UI тесты будут запущены в headless режиме."
    export CHROME_HEADLESS=true
else
    echo "✅ Chrome найден"
fi

# Создание директории для отчетов
mkdir -p build/reports/ui-tests

echo "📋 Запуск UI тестов..."

# Запуск UI тестов
./gradlew test \
    --tests "*UITest" \
    --tests "*IntegrationUITest" \
    --info \
    --stacktrace \
    -Dspring.profiles.active=ui-test \
    -Dui.test.headless=true \
    -Dui.test.screenshot-on-failure=true

# Проверка результата
if [ $? -eq 0 ]; then
    echo "✅ UI тесты выполнены успешно!"
    echo "📊 Отчеты доступны в build/reports/tests/test/"
    echo "📸 Скриншоты ошибок (если есть) в build/screenshots/"
else
    echo "❌ UI тесты завершились с ошибками"
    echo "📊 Отчеты доступны в build/reports/tests/test/"
    echo "📸 Скриншоты ошибок в build/screenshots/"
    exit 1
fi

echo "🎉 UI тесты завершены!"
