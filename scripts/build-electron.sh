#!/bin/bash

# Скрипт для сборки Electron приложения UFC Stats
# Автор: UFC Stats Project

set -e

echo "🚀 Начинаем сборку Electron приложения UFC Stats..."

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

# Переходим в директорию проекта
cd "$(dirname "$0")/.."

echo "📦 Устанавливаем зависимости Electron..."
npm install

echo "🔨 Собираем Java приложение..."
./gradlew clean bootJar

echo "📱 Собираем Electron приложение..."
npm run build

echo "✅ Electron приложение успешно собрано!"
echo "📁 Файлы приложения находятся в директории: dist-electron/"

# Показываем информацию о собранном приложении
if [ -d "dist-electron" ]; then
    echo ""
    echo "📊 Информация о собранном приложении:"
    ls -la dist-electron/
    echo ""
    echo "🎯 Для запуска приложения используйте:"
    echo "   npm run electron"
    echo ""
    echo "📦 Для создания установщика используйте:"
    echo "   npm run dist"
fi
