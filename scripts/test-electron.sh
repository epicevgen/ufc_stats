#!/bin/bash

# Простой тест Electron приложения
echo "🧪 Тестирование Electron приложения..."

# Проверяем наличие Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js не найден"
    exit 1
fi

# Проверяем наличие npm
if ! command -v npm &> /dev/null; then
    echo "❌ npm не найден"
    exit 1
fi

# Проверяем наличие Electron
if ! npm list electron &> /dev/null; then
    echo "📦 Устанавливаем Electron..."
    npm install electron
fi

echo "✅ Electron готов к тестированию"
echo "🚀 Запускаем тестовое окно..."

# Запускаем простой тест
node test-electron.js
