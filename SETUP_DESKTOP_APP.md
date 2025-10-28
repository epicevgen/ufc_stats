# 🚀 Настройка нативного приложения UFC Stats

## 📋 Текущий статус

✅ **Конфигурация Electron готова**
- `electron/main.js` - основной файл приложения
- `package.json` - конфигурация сборки
- `assets/icon.png` - иконка 512x512
- `assets/icon.icns` - иконка для macOS
- Скрипты сборки готовы

❌ **Требуется установка зависимостей**
- Node.js не установлен
- npm не установлен
- Electron не установлен

## 🛠️ Установка зависимостей

### **Шаг 1: Установка Node.js**

#### **macOS (рекомендуется)**
```bash
# Установка через Homebrew
brew install node

# Или скачайте с официального сайта
# https://nodejs.org/
```

#### **Альтернативный способ (NVM)**
```bash
# Установка NVM
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Перезагрузите терминал или выполните:
source ~/.bashrc

# Установка Node.js
nvm install 18
nvm use 18
```

### **Шаг 2: Проверка установки**
```bash
node --version
npm --version
```

### **Шаг 3: Установка зависимостей проекта**
```bash
cd /Users/evgenijstolarov/Documents/JavaProjects/ufc_stats
npm install
```

## 🎯 Создание нативного приложения

### **Быстрый способ**
```bash
# Запустите скрипт быстрого создания
./scripts/quick-installer.sh
```

### **Ручной способ**
```bash
# 1. Соберите Java приложение
./gradlew bootJar

# 2. Установите зависимости
npm install

# 3. Создайте установщик для macOS
npm run build-mac
```

## 📱 Результат

После выполнения всех шагов:

### **Созданные файлы**
- `dist-electron/UFC Stats-1.0.0.dmg` - установщик для macOS
- `dist-electron/UFC Stats-1.0.0.app` - приложение для macOS

### **Установка приложения**
1. **Откройте .dmg файл**
2. **Перетащите приложение в Applications**
3. **Запустите из Applications или Launchpad**

### **Возможности**
- ✅ **Запуск по иконке** - как обычное macOS приложение
- ✅ **Нативное меню** - меню приложения в строке меню
- ✅ **Автоматический запуск Java** - приложение само запускает Spring Boot
- ✅ **Резервное копирование** - встроенные функции бэкапа
- ✅ **Профессиональный вид** - как коммерческое приложение

## 🔧 Команды разработки

```bash
# Запуск в режиме разработки
npm run electron

# Сборка для всех платформ
npm run build

# Сборка только для macOS
npm run build-mac

# Сборка только для Windows
npm run build-win

# Сборка только для Linux
npm run build-linux
```

## 🎨 Настройка иконки

### **Текущие иконки**
- `assets/icon.png` - основная иконка 512x512
- `assets/icon.icns` - иконка для macOS
- `assets/icon.svg` - векторная иконка

### **Создание новой иконки**
1. **Создайте PNG файл 512x512 пикселей**
2. **Сохраните как `assets/icon.png`**
3. **Создайте ICNS**: `sips -s format icns assets/icon.png --out assets/icon.icns`

## 🚀 Быстрый старт

### **1. Установите Node.js**
```bash
brew install node
```

### **2. Установите зависимости**
```bash
npm install
```

### **3. Создайте приложение**
```bash
./scripts/quick-installer.sh
```

### **4. Установите приложение**
```bash
open dist-electron/UFC\ Stats-1.0.0.dmg
```

## 🎉 Готово!

После выполнения всех шагов у вас будет полноценное нативное приложение UFC Stats, которое:

- 🖥️ **Запускается по иконке** в Applications
- 🚀 **Имеет нативное меню** и интерфейс
- 📱 **Работает как обычное macOS приложение**
- 🔧 **Автоматически запускает Java backend**

**Приложение готово к использованию!** 🚀


