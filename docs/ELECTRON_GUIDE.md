# Руководство по Electron приложению UFC Stats

## 📋 Описание

UFC Stats Desktop - это нативное приложение для macOS, Windows и Linux, созданное с использованием Electron. Оно оборачивает веб-приложение UFC Stats в нативный интерфейс.

## 🏗️ Архитектура

```
UFC Stats Desktop
├── Electron (Нативный интерфейс)
│   ├── main.js          # Основной процесс
│   ├── preload.js       # Безопасный API
│   └── assets/          # Иконки и ресурсы
├── Spring Boot (Backend)
│   ├── ufc-stats-1.0.0.jar
│   └── data/            # База данных
└── Web Interface (Frontend)
    ├── Thymeleaf шаблоны
    └── Bootstrap UI
```

## 🚀 Быстрый старт

### Предварительные требования

1. **Node.js** (версия 16 или выше)
   ```bash
   # Проверка версии
   node --version
   npm --version
   ```

2. **Java 17** (для Spring Boot)
   ```bash
   # Проверка версии
   java --version
   ```

3. **Gradle** (для сборки Java приложения)
   ```bash
   # Проверка версии
   ./gradlew --version
   ```

### Установка и запуск

#### 1. Установка зависимостей
```bash
# Установка Node.js зависимостей
npm install
```

#### 2. Сборка Java приложения
```bash
# Сборка JAR файла
./gradlew bootJar
```

#### 3. Запуск Electron приложения
```bash
# Запуск в режиме разработки
npm run electron

# Или используйте скрипт
./scripts/run-electron.sh
```

## 🔧 Команды разработки

### Основные команды

```bash
# Запуск Electron приложения
npm run electron

# Запуск с автоматическим перезапуском Java
npm run electron-dev

# Сборка Electron приложения
npm run build

# Создание установщика
npm run dist
```

### Сборка для разных платформ

```bash
# macOS
npm run build-mac

# Windows
npm run build-win

# Linux
npm run build-linux
```

## 📁 Структура проекта

```
ufc_stats/
├── electron/                    # Electron файлы
│   ├── main.js                 # Основной процесс
│   ├── preload.js              # Preload скрипт
│   └── assets/                 # Иконки и ресурсы
├── package.json                # Node.js конфигурация
├── scripts/                    # Скрипты сборки
│   ├── build-electron.sh      # Сборка Electron
│   └── run-electron.sh        # Запуск Electron
└── docs/                       # Документация
    └── ELECTRON_GUIDE.md      # Это руководство
```

## ⚙️ Конфигурация

### package.json

```json
{
  "name": "ufc-stats-desktop",
  "version": "1.0.0",
  "main": "electron/main.js",
  "scripts": {
    "electron": "electron .",
    "electron-dev": "concurrently \"npm run start\" \"wait-on http://localhost:8080 && electron .\"",
    "build": "electron-builder"
  },
  "build": {
    "appId": "com.ufcstats.desktop",
    "productName": "UFC Stats",
    "directories": {
      "output": "dist-electron"
    }
  }
}
```

### Настройки приложения

```javascript
// electron/main.js
const APP_CONFIG = {
  name: 'UFC Stats',
  version: '1.0.0',
  javaAppPort: 8080,
  javaAppPath: path.join(__dirname, '../build/libs/ufc-stats-1.0.0.jar'),
  dataPath: path.join(__dirname, '../data'),
  scriptsPath: path.join(__dirname, '../scripts')
};
```

## 🎯 Функциональность

### Основные возможности

- ✅ **Нативный интерфейс** - Окно приложения с меню
- ✅ **Автоматический запуск** - Java приложение запускается автоматически
- ✅ **Резервное копирование** - Встроенные функции бэкапа
- ✅ **Кроссплатформенность** - Работает на macOS, Windows, Linux
- ✅ **Установщики** - Создание .dmg, .exe, .AppImage файлов

### Меню приложения

```
Файл
├── Создать резервную копию (Cmd/Ctrl+B)
├── Восстановить из бэкапа
└── Выход (Cmd/Ctrl+Q)

Просмотр
├── Перезагрузить (Cmd/Ctrl+R)
├── Полный экран (F11)
└── Инструменты разработчика (F12)

Справка
├── О программе
└── Документация
```

## 🔨 Сборка и распространение

### Создание установщиков

```bash
# Сборка для текущей платформы
npm run build

# Сборка для macOS
npm run build-mac

# Сборка для Windows
npm run build-win

# Сборка для Linux
npm run build-linux
```

### Результаты сборки

```
dist-electron/
├── UFC Stats-1.0.0.dmg          # macOS установщик
├── UFC Stats Setup 1.0.0.exe   # Windows установщик
├── UFC Stats-1.0.0.AppImage     # Linux AppImage
└── mac/                         # macOS приложение
    └── UFC Stats.app
```

## 🛠️ Разработка

### Структура Electron приложения

#### main.js (Основной процесс)
- Создание и управление окнами
- Запуск Java приложения
- Обработка меню и диалогов
- IPC коммуникация

#### preload.js (Preload скрипт)
- Безопасный API для рендерера
- Мост между веб-контентом и Electron
- Защита от прямого доступа к Node.js

### Добавление новых функций

#### 1. Новое меню
```javascript
// В main.js
{
  label: 'Новая функция',
  click: () => {
    // Логика функции
  }
}
```

#### 2. IPC обработчик
```javascript
// В main.js
ipcMain.handle('new-function', async () => {
  // Логика функции
  return result;
});
```

#### 3. Preload API
```javascript
// В preload.js
contextBridge.exposeInMainWorld('electronAPI', {
  newFunction: () => ipcRenderer.invoke('new-function')
});
```

## 🐛 Отладка

### Инструменты разработчика

```bash
# Запуск с DevTools
npm run electron

# В приложении нажмите F12 или Cmd+Option+I
```

### Логирование

```javascript
// В main.js
console.log('Лог сообщение');

// В preload.js
console.log('Preload лог');
```

### Проверка Java приложения

```bash
# Проверка порта
curl http://localhost:8080

# Проверка логов Java
tail -f logs/application.log
```

## 📦 Распространение

### Создание релиза

1. **Обновление версии**
   ```bash
   # В package.json
   "version": "1.0.1"
   ```

2. **Сборка приложения**
   ```bash
   npm run build
   ```

3. **Тестирование**
   ```bash
   # Тестирование на разных платформах
   npm run build-mac
   npm run build-win
   npm run build-linux
   ```

4. **Создание релиза**
   ```bash
   # Создание архива
   tar -czf ufc-stats-desktop-1.0.1.tar.gz dist-electron/
   ```

## 🔒 Безопасность

### Рекомендации

- ✅ **Context Isolation** - Включена по умолчанию
- ✅ **Node Integration** - Отключена для безопасности
- ✅ **Preload скрипты** - Единственный способ доступа к Node.js
- ✅ **CSP заголовки** - Настроены для безопасности

### Настройки безопасности

```javascript
// В main.js
webPreferences: {
  nodeIntegration: false,        // Отключено
  contextIsolation: true,       // Включено
  enableRemoteModule: false,    // Отключено
  preload: path.join(__dirname, 'preload.js')
}
```

## 📚 Дополнительные ресурсы

### Полезные ссылки

- [Electron Documentation](https://www.electronjs.org/docs)
- [Electron Builder](https://www.electron.build/)
- [Node.js Documentation](https://nodejs.org/docs/)

### Примеры кода

- [Electron Examples](https://github.com/electron/electron/tree/main/docs/fiddles)
- [Electron Builder Examples](https://github.com/electron-userland/electron-builder/tree/master/packages/app-builder-lib/templates)

## 🎉 Заключение

Electron приложение UFC Stats предоставляет:

- 🖥️ **Нативный интерфейс** для всех платформ
- 🚀 **Простой запуск** одним кликом
- 📦 **Установщики** для распространения
- 🔧 **Полная функциональность** веб-приложения
- 🛡️ **Безопасность** и изоляция

Приложение готово к использованию и распространению! 🚀
