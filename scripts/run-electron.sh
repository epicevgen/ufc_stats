#!/bin/bash

# Скрипт для запуска Electron приложения UFC Stats
# Автор: UFC Stats Project

set -e

echo "🚀 Запуск UFC Stats Desktop приложения..."

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

echo "🎯 Запускаем Electron приложение..."
npm run electron

echo "✅ UFC Stats Desktop приложение завершено"
