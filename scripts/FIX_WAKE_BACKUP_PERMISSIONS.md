# Исправление ошибки "Operation not permitted" для бэкапа при пробуждении

## Проблема
Ошибка "Operation not permitted" возникает при запуске скрипта через launchd на macOS.

## Решения

### 1. Проверка разрешений в настройках macOS

1. Откройте **Системные настройки** → **Конфиденциальность и безопасность**
2. Перейдите в раздел **Полный доступ к диску** (Full Disk Access)
3. Убедитесь, что включен доступ для:
   - `/usr/bin/bash` или `/bin/bash`
   - Терминал (Terminal) или iTerm

### 2. Альтернативный способ - использование cron через launchd

Если проблема сохраняется, можно использовать альтернативный подход через cron:

```bash
# Проверить, работает ли cron
sudo launchctl list | grep cron

# Если cron не работает, запустить его
sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.periodic-daily.plist
```

### 3. Проверка текущего статуса

```bash
# Проверить статус задачи
launchctl list | grep ufcstats

# Посмотреть логи ошибок
tail -f ~/Documents/JavaProjects/ufc_stats/backups/launchd-wakeup-error.log

# Посмотреть логи выполнения
tail -f ~/Documents/JavaProjects/ufc_stats/backups/launchd-wakeup.log
```

### 4. Ручной тест

Проверьте, что скрипт работает вручную:
```bash
cd ~/Documents/JavaProjects/ufc_stats
/bin/bash scripts/backup-on-wake.sh
```

### 5. Если проблема сохраняется

Можно попробовать использовать полный путь к bash в plist файле:
- Убедитесь, что в plist указан `/bin/bash` (не `/usr/bin/bash`)
- Проверьте, что скрипт имеет права на выполнение: `chmod +x scripts/backup-on-wake.sh`

### 6. Проверка расширенных атрибутов

Удалите расширенные атрибуты, которые могут блокировать выполнение:
```bash
cd ~/Documents/JavaProjects/ufc_stats
xattr -d com.apple.quarantine scripts/backup-on-wake.sh 2>/dev/null || true
xattr -d com.apple.quarantine scripts/backup-db.sh 2>/dev/null || true
```

### 7. Перезагрузка задачи

После исправления перезагрузите задачу:
```bash
launchctl unload ~/Library/LaunchAgents/com.ufcstats.backup.wakeup.plist
launchctl load ~/Library/LaunchAgents/com.ufcstats.backup.wakeup.plist
```

## Примечание

На macOS Sequoia (15.0) и новее могут быть дополнительные ограничения безопасности. 
Если проблема сохраняется, возможно потребуется:
- Отключить SIP (System Integrity Protection) - НЕ РЕКОМЕНДУЕТСЯ
- Использовать альтернативный способ запуска (например, через cron)

