# UFC Stats Desktop - Electron приложение

## 🚀 Быстрый запуск

### 1. Установка зависимостей
```bash
npm install
```

### 2. Сборка Java приложения
```bash
./gradlew bootJar
```

### 3. Запуск Electron приложения
```bash
npm run electron
```

## 📁 Структура

```
electron/
├── main.js          # Основной процесс Electron
├── preload.js       # Preload скрипт для безопасности
├── README.md        # Этот файл
└── assets/          # Иконки и ресурсы
    └── icon.png     # Иконка приложения
```

## 🔧 Команды

```bash
# Запуск приложения
npm run electron

# Запуск с DevTools
npm run electron-dev

# Сборка приложения
npm run build

# Создание установщика
npm run dist
```

## 🎯 Функциональность

- ✅ Нативный интерфейс
- ✅ Автоматический запуск Java
- ✅ Резервное копирование
- ✅ Кроссплатформенность
- ✅ Установщики для всех ОС

## 📚 Документация

Подробная документация: [ELECTRON_GUIDE.md](../docs/ELECTRON_GUIDE.md)
