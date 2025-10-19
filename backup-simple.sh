#!/bin/bash

# Простой скрипт для бэкапа БД
PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
DB_DIR="$PROJECT_DIR/data"
BACKUP_DIR="$PROJECT_DIR/backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo "Создаем бэкап базы данных..."

# Создаем директорию для бэкапов
mkdir -p "$BACKUP_DIR"

# Создаем архив
cd "$DB_DIR"
zip -r "$BACKUP_DIR/ufc_stats_backup_${TIMESTAMP}.zip" ufc_stats.mv.db ufc_stats.trace.db

if [ $? -eq 0 ]; then
    echo "✅ Бэкап создан: ufc_stats_backup_${TIMESTAMP}.zip"
    ls -la "$BACKUP_DIR"/*.zip
else
    echo "❌ Ошибка при создании бэкапа"
fi
