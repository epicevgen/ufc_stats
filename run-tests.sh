#!/bin/bash

# Скрипт для запуска UI тестов в разных режимах

echo "Выберите режим запуска тестов:"
echo "1) Headless (быстро, без GUI)"
echo "2) Visible (с GUI, для отладки)"
echo "3) CRUD тесты (headless)"
echo "4) CRUD тесты (visible)"
echo "5) Все тесты (headless)"

read -p "Введите номер (1-5): " choice

case $choice in
    1)
        echo "Запуск тестов в headless режиме..."
        ./gradlew test --tests "*SelenideTest*" -Dselenide.headless=true
        ;;
    2)
        echo "Запуск тестов с видимым браузером..."
        ./gradlew test --tests "*SelenideTest*" -Dselenide.headless=false
        ;;
    3)
        echo "Запуск CRUD тестов в headless режиме..."
        ./gradlew test --tests "FightCRUDSelenideTest" -Dselenide.headless=true
        ;;
    4)
        echo "Запуск CRUD тестов с видимым браузером..."
        ./gradlew test --tests "FightCRUDSelenideTest" -Dselenide.headless=false
        ;;
    5)
        echo "Запуск всех тестов в headless режиме..."
        ./gradlew test -Dselenide.headless=true
        ;;
    *)
        echo "Неверный выбор. Запуск в headless режиме по умолчанию..."
        ./gradlew test --tests "*SelenideTest*" -Dselenide.headless=true
        ;;
esac
