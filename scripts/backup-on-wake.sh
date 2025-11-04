#!/bin/bash

# Скрипт для запуска бэкапа при пробуждении ноутбука
# Создает бэкап один раз в сутки при первом пробуждении
# Если в течение дня ноутбук открывается повторно - проверяет наличие сегодняшнего бэкапа

# Переходим в директорию проекта
cd /Users/evgenijstolarov/Documents/JavaProjects/ufc_stats || exit 1

PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
BACKUP_SCRIPT="$PROJECT_DIR/scripts/backup-db.sh"
BACKUP_DIR="$PROJECT_DIR/backups"
LOG_FILE="$BACKUP_DIR/wake-backup.log"

# Создаем директорию для логов, если её нет
mkdir -p "$BACKUP_DIR"

# Функция для логирования
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# Проверяем, есть ли бэкап за сегодняшний день
has_backup_today() {
    local today=$(date +%Y%m%d)
    
    # Проверяем наличие бэкапов с сегодняшней датой в имени файла
    # Формат имени: ufc_stats_backup_YYYYMMDD_HHMMSS.zip
    if ls "$BACKUP_DIR"/ufc_stats_backup_${today}_*.zip 1> /dev/null 2>&1; then
        return 0  # Бэкап за сегодня найден
    else
        return 1  # Бэкапа за сегодня нет
    fi
}

# Основная логика
log "Проверка необходимости бэкапа при пробуждении ноутбука..."

if has_backup_today; then
    local backup_file=$(ls -t "$BACKUP_DIR"/ufc_stats_backup_$(date +%Y%m%d)_*.zip 2>/dev/null | head -1)
    log "✅ Бэкап за сегодня уже существует: $(basename "$backup_file")"
    log "Пропускаем создание бэкапа (уже был создан сегодня)"
else
    log "Бэкапа за сегодня еще нет. Создаем бэкап при пробуждении..."
    "$BACKUP_SCRIPT" backup
    
    if [ $? -eq 0 ]; then
        log "✅ Бэкап успешно создан при пробуждении"
    else
        log "❌ Ошибка при выполнении бэкапа"
        exit 1
    fi
fi

