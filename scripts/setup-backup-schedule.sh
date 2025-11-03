#!/bin/bash

# Скрипт для настройки автоматического бэкапа на macOS
# Использует launchd для надежного запуска задач

PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
PLIST_FILE="com.ufcstats.backup.schedule.plist"
LAUNCH_AGENTS_DIR="$HOME/Library/LaunchAgents"
PLIST_PATH="$LAUNCH_AGENTS_DIR/$PLIST_FILE"
SCRIPT_DIR="$PROJECT_DIR/scripts"

echo "🔧 Настройка автоматического бэкапа для macOS..."

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

# Копируем основной plist файл
cp "$SCRIPT_DIR/$PLIST_FILE" "$PLIST_PATH"
echo "✅ Файл конфигурации скопирован"

# Загружаем основную задачу (расписание на 2:00)
launchctl load "$PLIST_PATH"
echo "✅ Задача бэкапа по расписанию установлена"

# Устанавливаем задачу при пробуждении (дополнительная страховка)
WAKEUP_PLIST_FILE="com.ufcstats.backup.wakeup.plist"
WAKEUP_PLIST_PATH="$LAUNCH_AGENTS_DIR/$WAKEUP_PLIST_FILE"

if [ -f "$SCRIPT_DIR/$WAKEUP_PLIST_FILE" ]; then
    if [ -f "$WAKEUP_PLIST_PATH" ]; then
        launchctl unload "$WAKEUP_PLIST_PATH" 2>/dev/null || true
    fi
    cp "$SCRIPT_DIR/$WAKEUP_PLIST_FILE" "$WAKEUP_PLIST_PATH"
    launchctl load "$WAKEUP_PLIST_PATH"
    echo "✅ Задача бэкапа при пробуждении установлена"
fi

echo ""
echo "📋 Информация:"
echo "   - Задача 1: $PLIST_FILE"
echo "      Расписание: Ежедневно в 2:00 ночи (если компьютер активен)"
echo "      Логи: $PROJECT_DIR/backups/launchd-schedule.log"
echo ""
echo "   - Задача 2: $WAKEUP_PLIST_FILE (если установлена)"
echo "      Триггер: При пробуждении компьютера/подключении к сети"
echo "      Проверка: Не чаще раза в час"
echo "      Логи: $PROJECT_DIR/backups/wake-backup.log"
echo ""
echo "📝 Полезные команды:"
echo "   Просмотр статуса: launchctl list | grep ufcstats"
echo "   Остановить: launchctl unload $PLIST_PATH"
echo "   Запустить: launchctl load $PLIST_PATH"
echo "   Перезапустить: launchctl unload $PLIST_PATH && launchctl load $PLIST_PATH"
echo "   Удалить: launchctl unload $PLIST_PATH && rm $PLIST_PATH"
echo ""
echo "✅ Настройка завершена!"

