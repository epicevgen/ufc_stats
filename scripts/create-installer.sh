#!/bin/bash

# Скрипт для создания установщика UFC Stats Desktop
echo "🚀 Создание установщика UFC Stats Desktop..."

# Переходим в директорию проекта
cd "$(dirname "$0")/.."

# Проверяем наличие Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js не найден. Установите Node.js с https://nodejs.org/"
    exit 1
fi

# Проверяем наличие npm
if ! command -v npm &> /dev/null; then
    echo "❌ npm не найден. Установите npm"
    exit 1
fi

# Проверяем наличие JAR файла
if [ ! -f "build/libs/ufc-stats-1.0.0.jar" ]; then
    echo "🔨 JAR файл не найден. Собираем приложение..."
    ./gradlew bootJar
fi

# Проверяем наличие node_modules
if [ ! -d "node_modules" ]; then
    echo "📦 Устанавливаем зависимости..."
    npm install
fi

echo "📱 Создаем установщик для macOS..."
npm run build-mac

echo "✅ Установщик создан!"
echo "📁 Файлы находятся в директории: dist-electron/"

# Показываем информацию о созданных файлах
if [ -d "dist-electron" ]; then
    echo ""
    echo "📊 Созданные файлы:"
    ls -la dist-electron/
    echo ""
    echo "🎯 Для установки:"
    echo "   1. Откройте файл .dmg"
    echo "   2. Перетащите приложение в Applications"
    echo "   3. Запустите из Applications или Launchpad"
fi





