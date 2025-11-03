#!/bin/bash

# Скрипт для настройки автоматического бэкапа при пробуждении ноутбука
# Бэкап создается один раз в сутки при первом пробуждении

PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
PLIST_FILE="com.ufcstats.backup.wakeup.plist"
LAUNCH_AGENTS_DIR="$HOME/Library/LaunchAgents"
PLIST_PATH="$LAUNCH_AGENTS_DIR/$PLIST_FILE"
SCRIPT_DIR="$PROJECT_DIR/scripts"

echo "🔧 Настройка автоматического бэкапа при пробуждении ноутбука..."

# Проверяем наличие директории LaunchAgents
if [ ! -d "$LAUNCH_AGENTS_DIR" ]; then
    mkdir -p "$LAUNCH_AGENTS_DIR"
    echo "✅ Создана директория LaunchAgents"
fi

# Проверяем, не установлен ли уже бэкап
if [ -f "$PLIST_PATH" ]; then
    echo "⚠️  Задача бэкапа уже установлена. Переустанавливаем..."
    launchctl unload "$PLIST_PATH" 2>/dev/null || true
fi

# Копируем plist файл
cp "$SCRIPT_DIR/$PLIST_FILE" "$PLIST_PATH"
echo "✅ Файл конфигурации скопирован"

# Загружаем задачу
launchctl load "$PLIST_PATH"
echo "✅ Задача бэкапа при пробуждении установлена"

echo ""
echo "📋 Как это работает:"
echo "   1. При каждом пробуждении ноутбука проверяется наличие бэкапа за сегодня"
echo "   2. Если бэкапа за сегодня НЕТ → создается новый"
echo "   3. Если бэкап за сегодня УЖЕ ЕСТЬ → пропускается (один раз в сутки)"
echo ""
echo "📝 Полезные команды:"
echo "   Просмотр статуса: launchctl list | grep ufcstats"
echo "   Остановить: launchctl unload $PLIST_PATH"
echo "   Запустить: launchctl load $PLIST_PATH"
echo "   Посмотреть логи: tail -f $PROJECT_DIR/backups/wake-backup.log"
echo "   Удалить: launchctl unload $PLIST_PATH && rm $PLIST_PATH"
echo ""
echo "✅ Настройка завершена!"

