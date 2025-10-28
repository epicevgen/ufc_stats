# Использование Electron приложения UFC Stats

## 🎯 Обзор

UFC Stats Desktop - это нативное приложение, которое оборачивает веб-приложение UFC Stats в десктопный интерфейс с помощью Electron.

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

## 📱 Возможности приложения

### Основные функции
- ✅ **Нативное окно** - Приложение запускается в отдельном окне
- ✅ **Автоматический запуск** - Java приложение запускается автоматически
- ✅ **Меню приложения** - Полноценное меню с функциями
- ✅ **Резервное копирование** - Встроенные функции бэкапа
- ✅ **Кроссплатформенность** - Работает на macOS, Windows, Linux

### Меню приложения

#### Файл
- **Создать резервную копию** (Cmd/Ctrl+B) - Создает бэкап БД
- **Восстановить из бэкапа** - Восстанавливает БД из архива
- **Выход** (Cmd/Ctrl+Q) - Закрывает приложение

#### Просмотр
- **Перезагрузить** (Cmd/Ctrl+R) - Обновляет страницу
- **Полный экран** (F11) - Переключает полноэкранный режим
- **Инструменты разработчика** (F12) - Открывает DevTools

#### Справка
- **О программе** - Информация о приложении
- **Документация** - Ссылка на GitHub

## 🔧 Команды разработки

### Основные команды
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

### Сборка для разных платформ
```bash
# macOS
npm run build-mac

# Windows  
npm run build-win

# Linux
npm run build-linux
```

## 📦 Создание установщиков

### Автоматическая сборка
```bash
# Сборка для текущей платформы
npm run build

# Сборка для всех платформ
npm run dist
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

### Структура файлов
```
electron/
├── main.js          # Основной процесс
├── preload.js       # Preload скрипт
└── assets/          # Иконки и ресурсы
    └── icon.png     # Иконка приложения
```

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

## 🔒 Безопасность

### Настройки безопасности
- ✅ **Context Isolation** - Включена по умолчанию
- ✅ **Node Integration** - Отключена для безопасности
- ✅ **Preload скрипты** - Единственный способ доступа к Node.js
- ✅ **CSP заголовки** - Настроены для безопасности

### Рекомендации
- Не включайте `nodeIntegration: true`
- Используйте `contextIsolation: true`
- Ограничивайте доступ к Node.js через preload скрипты

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
