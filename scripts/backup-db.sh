#!/bin/bash

# Скрипт для резервного копирования базы данных H2
# Автор: UFC Stats Project
# Дата: $(date)

# Настройки
PROJECT_DIR="/Users/evgenijstolarov/Documents/JavaProjects/ufc_stats"
DB_DIR="$PROJECT_DIR/data"
BACKUP_DIR="$PROJECT_DIR/backups"
DB_NAME="ufc_stats"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_backup_${TIMESTAMP}.zip"

# Создаем директорию для бэкапов если её нет
mkdir -p "$BACKUP_DIR"

# Функция для логирования
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$BACKUP_DIR/backup.log"
}

# Функция для очистки старых бэкапов (оставляем последние 10)
cleanup_old_backups() {
    log "Очистка старых бэкапов..."
    cd "$BACKUP_DIR"
    ls -t ${DB_NAME}_backup_*.zip | tail -n +11 | xargs -r rm
    log "Старые бэкапы удалены"
}

# Основная функция бэкапа
backup_database() {
    log "Начинаем резервное копирование базы данных..."
    
    # Проверяем существование файлов БД
    if [ ! -f "$DB_DIR/${DB_NAME}.mv.db" ]; then
        log "ОШИБКА: Файл базы данных не найден: $DB_DIR/${DB_NAME}.mv.db"
        exit 1
    fi
    
    # Создаем архив с файлами БД
    cd "$DB_DIR"
    zip -r "$BACKUP_FILE" "${DB_NAME}.mv.db" "${DB_NAME}.trace.db" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log "✅ Бэкап успешно создан: $BACKUP_FILE"
        
        # Проверяем размер файла
        BACKUP_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
        log "Размер бэкапа: $BACKUP_SIZE"
        
        # Очищаем старые бэкапы
        cleanup_old_backups
        
        log "✅ Резервное копирование завершено успешно"
        return 0
    else
        log "❌ ОШИБКА: Не удалось создать бэкап"
        exit 1
    fi
}

# Функция для восстановления из бэкапа
restore_database() {
    local backup_file="$1"
    
    if [ -z "$backup_file" ]; then
        log "ОШИБКА: Не указан файл для восстановления"
        echo "Использование: $0 restore <путь_к_бэкапу>"
        exit 1
    fi
    
    if [ ! -f "$backup_file" ]; then
        log "ОШИБКА: Файл бэкапа не найден: $backup_file"
        exit 1
    fi
    
    log "Начинаем восстановление из бэкапа: $backup_file"
    
    # Останавливаем приложение если оно запущено
    pkill -f "ufc-stats" 2>/dev/null || true
    sleep 2
    
    # Создаем резервную копию текущей БД
    if [ -f "$DB_DIR/${DB_NAME}.mv.db" ]; then
        mv "$DB_DIR/${DB_NAME}.mv.db" "$DB_DIR/${DB_NAME}.mv.db.backup.$(date +%Y%m%d_%H%M%S)"
        mv "$DB_DIR/${DB_NAME}.trace.db" "$DB_DIR/${DB_NAME}.trace.db.backup.$(date +%Y%m%d_%H%M%S)" 2>/dev/null || true
    fi
    
    # Восстанавливаем из архива
    cd "$DB_DIR"
    unzip -o "$backup_file"
    
    if [ $? -eq 0 ]; then
        log "✅ База данных успешно восстановлена"
        log "Старая БД сохранена как backup"
    else
        log "❌ ОШИБКА: Не удалось восстановить базу данных"
        exit 1
    fi
}

# Функция для отображения списка бэкапов
list_backups() {
    log "Список доступных бэкапов:"
    ls -la "$BACKUP_DIR"/*.zip 2>/dev/null | while read line; do
        echo "  $line"
    done
}

# Основная логика
case "$1" in
    "backup")
        backup_database
        ;;
    "restore")
        restore_database "$2"
        ;;
    "list")
        list_backups
        ;;
    "cleanup")
        cleanup_old_backups
        ;;
    *)
        echo "Использование: $0 {backup|restore|list|cleanup}"
        echo ""
        echo "Команды:"
        echo "  backup   - Создать резервную копию БД"
        echo "  restore  - Восстановить БД из бэкапа"
        echo "  list     - Показать список бэкапов"
        echo "  cleanup  - Удалить старые бэкапы"
        echo ""
        echo "Примеры:"
        echo "  $0 backup"
        echo "  $0 restore /path/to/backup.zip"
        echo "  $0 list"
        exit 1
        ;;
esac
