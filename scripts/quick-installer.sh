#!/bin/bash

# Быстрое создание установщика UFC Stats Desktop
echo "🚀 Быстрое создание установщика UFC Stats Desktop..."

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

echo "📦 Устанавливаем зависимости..."
npm install

echo "🔨 Собираем Java приложение..."
./gradlew bootJar

echo "📱 Создаем установщик для macOS..."
npm run build-mac

echo "✅ Установщик создан!"
echo ""
echo "📁 Файлы находятся в директории: dist-electron/"
echo ""
echo "🎯 Для установки:"
echo "   1. Откройте файл .dmg в dist-electron/"
echo "   2. Перетащите приложение в Applications"
echo "   3. Запустите из Applications или Launchpad"
echo ""
echo "🚀 Приложение будет запускаться по иконке!"

# Показываем созданные файлы
if [ -d "dist-electron" ]; then
    echo ""
    echo "📊 Созданные файлы:"
    ls -la dist-electron/
fi





