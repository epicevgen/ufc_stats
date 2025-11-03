#!/bin/bash

# Ежедневный автоматический бэкап базы данных UFC Stats
# Этот скрипт можно добавить в crontab для автоматического выполнения

PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
BACKUP_SCRIPT="$PROJECT_DIR/scripts/backup-db.sh"

# Проверяем существование скрипта бэкапа
if [ ! -f "$BACKUP_SCRIPT" ]; then
    echo "ОШИБКА: Скрипт бэкапа не найден: $BACKUP_SCRIPT"
    exit 1
fi

# Выполняем бэкап
echo "Выполняем ежедневный бэкап базы данных UFC Stats..."
"$BACKUP_SCRIPT" backup

# Проверяем результат
if [ $? -eq 0 ]; then
    echo "✅ Ежедневный бэкап выполнен успешно"
else
    echo "❌ ОШИБКА: Ежедневный бэкап не удался"
    exit 1
fi
