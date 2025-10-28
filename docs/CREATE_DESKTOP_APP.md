# Создание нативного приложения с иконкой

## 🎯 Цель

Создать нативное приложение UFC Stats, которое можно запускать по иконке на рабочем столе, как обычное macOS/Windows/Linux приложение.

## 🚀 Пошаговое руководство

### **Шаг 1: Подготовка**

#### 1.1 Установка зависимостей
```bash
# Установка Node.js зависимостей
npm install

# Сборка Java приложения
./gradlew bootJar
```

#### 1.2 Проверка файлов
```bash
# Проверяем, что JAR файл создан
ls -la build/libs/ufc-stats-1.0.0.jar

# Проверяем, что Electron установлен
npm list electron
```

### **Шаг 2: Создание иконки**

#### 2.1 Требования к иконке
- **Размер**: 512x512 пикселей (минимум)
- **Формат**: PNG с прозрачностью
- **Стиль**: Простой, узнаваемый дизайн

#### 2.2 Создание иконки
```bash
# Создаем директорию для иконок
mkdir -p assets

# Создаем иконку (замените на свою)
# Рекомендуется использовать онлайн-генераторы:
# - https://www.favicon-generator.org/
# - https://realfavicongenerator.net/
# - https://iconifier.net/
```

#### 2.3 Размещение иконок
```
assets/
├── icon.png          # 512x512 PNG
├── icon.ico          # Windows ICO
├── icon.icns         # macOS ICNS
└── icon-16x16.png    # Маленькая иконка
```

### **Шаг 3: Создание установщика**

#### 3.1 Для macOS
```bash
# Создание .dmg установщика
npm run build-mac

# Результат: dist-electron/UFC Stats-1.0.0.dmg
```

#### 3.2 Для Windows
```bash
# Создание .exe установщика
npm run build-win

# Результат: dist-electron/UFC Stats Setup 1.0.0.exe
```

#### 3.3 Для Linux
```bash
# Создание AppImage
npm run build-linux

# Результат: dist-electron/UFC Stats-1.0.0.AppImage
```

### **Шаг 4: Установка приложения**

#### 4.1 macOS
1. **Откройте .dmg файл**
2. **Перетащите приложение в Applications**
3. **Запустите из Applications или Launchpad**

#### 4.2 Windows
1. **Запустите .exe файл**
2. **Следуйте инструкциям установщика**
3. **Запустите из меню Пуск**

#### 4.3 Linux
1. **Сделайте AppImage исполняемым**: `chmod +x UFC\ Stats-1.0.0.AppImage`
2. **Запустите**: `./UFC\ Stats-1.0.0.AppImage`

## 🔧 Автоматизация

### **Скрипт для создания установщика**

```bash
#!/bin/bash
# scripts/create-installer.sh

echo "🚀 Создание установщика UFC Stats Desktop..."

# Проверяем зависимости
if ! command -v node &> /dev/null; then
    echo "❌ Node.js не найден"
    exit 1
fi

# Собираем Java приложение
./gradlew bootJar

# Устанавливаем зависимости
npm install

# Создаем установщик для текущей платформы
npm run build

echo "✅ Установщик создан в dist-electron/"
```

### **Запуск скрипта**
```bash
chmod +x scripts/create-installer.sh
./scripts/create-installer.sh
```

## 📱 Результат

### **После установки у вас будет:**

#### **macOS**
- 🖥️ **Приложение в Applications**
- 🚀 **Иконка в Launchpad**
- 📱 **Возможность запуска по иконке**

#### **Windows**
- 🖥️ **Приложение в меню Пуск**
- 🚀 **Ярлык на рабочем столе**
- 📱 **Возможность запуска по иконке**

#### **Linux**
- 🖥️ **AppImage файл**
- 🚀 **Возможность добавления в меню**
- 📱 **Запуск по иконке**

## 🎨 Создание иконки

### **Онлайн-генераторы иконок**

#### **1. Favicon Generator**
- **URL**: https://www.favicon-generator.org/
- **Функции**: Создание иконок разных размеров
- **Форматы**: PNG, ICO, Apple Touch Icon

#### **2. Real Favicon Generator**
- **URL**: https://realfavicongenerator.net/
- **Функции**: Полный набор иконок для всех платформ
- **Форматы**: PNG, ICO, ICNS, Android

#### **3. Iconifier**
- **URL**: https://iconifier.net/
- **Функции**: Создание иконок для macOS
- **Форматы**: ICNS

### **Рекомендации по дизайну**

#### **Цвета**
- 🥊 **Основной**: Темно-синий (#1a365d)
- 🥊 **Акцент**: Золотой (#f6ad55)
- 🥊 **Фон**: Белый или прозрачный

#### **Элементы**
- 🥊 **Перчатки** - Символ UFC
- 📊 **График** - Статистика
- 🏆 **Кубок** - Победы

#### **Текст**
- **Название**: "UFC Stats"
- **Шрифт**: Жирный, читаемый
- **Размер**: Занимает 30-40% иконки

## 🛠️ Настройка package.json

### **Конфигурация иконок**

```json
{
  "build": {
    "mac": {
      "icon": "assets/icon.icns",
      "category": "public.app-category.sports"
    },
    "win": {
      "icon": "assets/icon.ico"
    },
    "linux": {
      "icon": "assets/icon.png"
    }
  }
}
```

### **Настройка приложения**

```json
{
  "build": {
    "appId": "com.ufcstats.desktop",
    "productName": "UFC Stats",
    "directories": {
      "output": "dist-electron"
    },
    "files": [
      "electron/**/*",
      "build/libs/*.jar",
      "data/**/*"
    ]
  }
}
```

## 🚀 Быстрый старт

### **1. Создание иконки**
```bash
# Создайте иконку 512x512 PNG
# Сохраните как assets/icon.png
```

### **2. Создание установщика**
```bash
# Запустите скрипт
./scripts/create-installer.sh
```

### **3. Установка приложения**
```bash
# macOS
open dist-electron/UFC\ Stats-1.0.0.dmg

# Windows
# Запустите .exe файл

# Linux
chmod +x dist-electron/UFC\ Stats-1.0.0.AppImage
./dist-electron/UFC\ Stats-1.0.0.AppImage
```

## 🎉 Результат

После выполнения всех шагов у вас будет:

- ✅ **Нативное приложение** с иконкой
- ✅ **Установщик** для распространения
- ✅ **Возможность запуска** по иконке
- ✅ **Профессиональный вид** приложения

**Приложение готово к использованию!** 🚀





