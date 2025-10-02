#!/bin/bash

echo "🥊 Создание боев для демонстрации пагинации..."

# Массивы данных
fighters=("Иван Петров" "Алексей Сидоров" "Дмитрий Волков" "Сергей Козлов" "Андрей Морозов")
opponents=("Джон Смит" "Майк Джонсон" "Том Уилсон" "Джейк Браун" "Боб Дэвис")
results=("WIN" "LOSS" "DRAW")
methods=("DECISION" "KNOCKOUT" "SUBMISSION" "EARLY_EXIT")
modes=("STANCE" "MMA")
weight_classes=("LIGHTWEIGHT" "WELTERWEIGHT" "MIDDLEWEIGHT" "LIGHT_HEAVYWEIGHT" "HEAVYWEIGHT")

# Создаем 15 боев
for i in {0..14}; do
    echo "Создание боя #$((i + 1))"
    
    # Генерируем случайные данные
    fighter=${fighters[$((i % 5))]}
    opponent=${opponents[$((i % 5))]}
    result=${results[$((i % 3))]}
    method=${methods[$((i % 4))]}
    mode=${modes[$((i % 2))]}
    weight_class=${weight_classes[$((i % 5))]}
    
    # Создаем JSON для боя
    fight_json=$(cat <<EOF
{
    "myFighter": "$fighter",
    "opponent": "$opponent",
    "fightDate": "2024-01-$((15 + i))T20:00:00",
    "fightMode": "$mode",
    "season": 2024,
    "result": "$result",
    "method": "$method",
    "roundsPlayed": $((i % 5 + 1)),
    "ratingPoints": $((1500 + i * 10)),
    "rankingPosition": $((i + 1)),
    "weightClass": "$weight_class",
    "notes": "Тестовый бой #$((i + 1))"
}
EOF
)
    
    # Отправляем запрос
    response=$(curl -s -X POST "http://localhost:8080/api/fights" \
        -H "Content-Type: application/json" \
        -d "$fight_json")
    
    if [[ $response == *"id"* ]]; then
        echo "✅ Бой #$((i + 1)) создан: $fighter vs $opponent"
    else
        echo "❌ Ошибка при создании боя #$((i + 1)): $response"
    fi
    
    # Небольшая пауза между запросами
    sleep 0.5
done

echo "🎉 Создание тестовых боев завершено!"
