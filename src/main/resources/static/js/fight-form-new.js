// Глобальные функции для форматирования полей
// Функция для автоматического добавления двоеточия в поля контроля времени
function formatControlTime(input) {
    console.log('formatControlTime called with value:', input.value);
    
    let value = input.value.replace(/\D/g, ''); // Убираем все нецифровые символы
    console.log('Cleaned value:', value);
    
    if (value.length >= 2) {
        // Если введено 2 или больше цифр, добавляем двоеточие
        value = value.substring(0, 2) + ':' + value.substring(2, 4);
        console.log('Formatted value:', value);
    }
    
    input.value = value;
    console.log('Final value set to:', input.value);
}

// Функция для обработки изменения режима боя
function handleFightModeChange(select) {
    const fightMode = select.value;
    
    if (fightMode === 'STANCE') {
        // Если выбран режим "Стойка", заполняем поля тейкдаунов нулями
        clearTakedownFields();
    }
}

// Функция для очистки полей тейкдаунов
function clearTakedownFields() {
    const roundsPlayed = document.getElementById('roundsPlayed');
    const roundsCount = parseInt(roundsPlayed.value) || 0;
    
    for (let round = 1; round <= roundsCount; round++) {
        // Очищаем поля тейкдаунов для моего бойца
        const mySuccessfulField = document.getElementById(`round${round}_my_takedowns_successful`);
        const myAttemptedField = document.getElementById(`round${round}_my_takedowns_attempted`);
        
        if (mySuccessfulField) mySuccessfulField.value = '0';
        if (myAttemptedField) myAttemptedField.value = '0';
        
        // Очищаем поля тейкдаунов для соперника
        const opponentSuccessfulField = document.getElementById(`round${round}_opponent_takedowns_successful`);
        const opponentAttemptedField = document.getElementById(`round${round}_opponent_takedowns_attempted`);
        
        if (opponentSuccessfulField) opponentSuccessfulField.value = '0';
        if (opponentAttemptedField) opponentAttemptedField.value = '0';
    }
}

// Управление формой создания/редактирования боя
document.addEventListener('DOMContentLoaded', function() {
    const roundsPlayedInput = document.getElementById('roundsPlayed');
    const roundsContainer = document.getElementById('roundsContainer');
    const judgesContainer = document.getElementById('judgesContainer');
    
    // Инициализация формы
    initializeForm();
    
    // Обработчики событий
    if (roundsPlayedInput) {
        roundsPlayedInput.addEventListener('input', updateRounds);
    }
    
    // Валидация формы
    const form = document.querySelector('.needs-validation');
    if (form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    }
    
    function initializeForm() {
        // Не генерируем раунды автоматически, ждем когда пользователь выберет количество
        if (judgesContainer) {
            updateJudges();
        }
        
        // Проверяем, что поле даты заполнено (должно быть заполнено сервером)
        const fightDateInput = document.getElementById('fightDate');
        if (fightDateInput) {
            console.log('Fight date input value:', fightDateInput.value);
        }
    }
    
    function updateRounds() {
        const roundsCount = parseInt(roundsPlayedInput.value);
        console.log('🚀🚀🚀 updateRounds called with value:', roundsPlayedInput.value, 'parsed:', roundsCount);
        console.log('🚀🚀🚀 Using NEW HORIZONTAL LAYOUT v4 - NEW FILE!');
        console.log('🚀🚀🚀 This should show horizontal layout!');
        roundsContainer.innerHTML = '';
        
        // Генерируем раунды только если пользователь выбрал количество
        if (roundsCount && roundsCount > 0) {
            console.log('🚀🚀🚀 Generating', roundsCount, 'rounds with HORIZONTAL layout');
            
            // Создаем общую структуру для всех раундов - горизонтально в одном блоке
            const roundsWrapper = document.createElement('div');
            roundsWrapper.className = 'd-flex flex-wrap gap-3';
            console.log('🚀🚀🚀 Created roundsWrapper with d-flex flex-wrap gap-3');
            
            for (let round = 1; round <= roundsCount; round++) {
                const roundColumn = createRoundColumn(round);
                roundsWrapper.appendChild(roundColumn);
            }
            
            roundsContainer.appendChild(roundsWrapper);
            
            // Добавляем обработчики событий для полей контроля времени
            console.log('Calling addControlTimeEventListeners');
            addControlTimeEventListeners(roundsCount);
        }
    }
    
    function updateJudges() {
        if (!judgesContainer) {
            console.log('judgesContainer not found, skipping judge generation');
            return;
        }
        
        judgesContainer.innerHTML = '';
        
        for (let judge = 1; judge <= 3; judge++) {
            const judgeCard = createJudgeCard(judge);
            judgesContainer.appendChild(judgeCard);
        }
    }
    
    // Функция для добавления обработчиков событий к полям контроля времени
    function addControlTimeEventListeners(roundsCount) {
        console.log('addControlTimeEventListeners called with roundsCount:', roundsCount);
        
        for (let round = 1; round <= roundsCount; round++) {
            const myControlField = document.getElementById(`round${round}_my_control_time`);
            const opponentControlField = document.getElementById(`round${round}_opponent_control_time`);
            
            console.log(`Round ${round} - myControlField:`, myControlField);
            console.log(`Round ${round} - opponentControlField:`, opponentControlField);
            
            if (myControlField) {
                console.log(`Adding event listener to round${round}_my_control_time`);
                myControlField.addEventListener('input', function() {
                    console.log('Input event triggered on my control field');
                    formatControlTime(this);
                });
            }
            
            if (opponentControlField) {
                console.log(`Adding event listener to round${round}_opponent_control_time`);
                opponentControlField.addEventListener('input', function() {
                    console.log('Input event triggered on opponent control field');
                    formatControlTime(this);
                });
            }
        }
    }
    
    function createRoundColumn(roundNumber) {
        const column = document.createElement('div');
        column.className = 'flex-shrink-0';
        column.style.minWidth = '300px';
        column.innerHTML = `
            <div class="card h-100">
                <div class="card-header text-center">
                    <h6 class="mb-0">Раунд ${roundNumber}</h6>
                </div>
                <div class="card-body">
                    <!-- Мой боец -->
                    <div class="mb-4">
                        <h6 class="text-primary mb-2 text-center">Мой боец</h6>
                        
                        <!-- Повреждения -->
                        <div class="mb-2">
                            <label class="form-label small">Повреждения головы</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_head_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_my_head_damage" 
                                       name="rounds[${roundNumber-1}].myHeadDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_head_damage')">+</button>
                            </div>
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Повреждения корпуса</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_body_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_my_body_damage" 
                                       name="rounds[${roundNumber-1}].myBodyDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_body_damage')">+</button>
                            </div>
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Повреждения ног</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_leg_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_my_leg_damage" 
                                       name="rounds[${roundNumber-1}].myLegDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_leg_damage')">+</button>
                            </div>
                        </div>
                        
                        <!-- Нокдауны -->
                        <div class="mb-2">
                            <label class="form-label small">Нокдауны</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_knockdowns')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_my_knockdowns" 
                                       name="rounds[${roundNumber-1}].myKnockdowns" min="0" max="10">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_knockdowns')">+</button>
                            </div>
                        </div>
                        
                        <!-- Значимые удары -->
                        <div class="mb-2">
                            <label class="form-label small">Значимые удары (попал)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_significant_landed" 
                                   name="rounds[${roundNumber-1}].mySignificantStrikesLanded" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Значимые удары (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_significant_attempted" 
                                   name="rounds[${roundNumber-1}].mySignificantStrikesAttempted" min="0">
                        </div>
                        
                        <!-- Все удары -->
                        <div class="mb-2">
                            <label class="form-label small">Все удары (попал)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_total_landed" 
                                   name="rounds[${roundNumber-1}].myTotalStrikesLanded" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Все удары (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_total_attempted" 
                                   name="rounds[${roundNumber-1}].myTotalStrikesAttempted" min="0">
                        </div>
                        
                        <!-- Тейкдауны -->
                        <div class="mb-2">
                            <label class="form-label small">Тейкдауны (успешные)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_takedowns_successful" 
                                   name="rounds[${roundNumber-1}].myTakedownsSuccessful" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Тейкдауны (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_takedowns_attempted" 
                                   name="rounds[${roundNumber-1}].myTakedownsAttempted" min="0">
                        </div>
                        
                        <!-- Контроль времени -->
                        <div class="mb-2">
                            <label class="form-label small">Контроль (мм:сс)</label>
                            <input type="text" class="form-control form-control-sm" id="round${roundNumber}_my_control_time" 
                                   name="rounds[${roundNumber-1}].myControlTime" pattern="[0-5][0-9]:[0-5][0-9]"
                                   maxlength="5">
                        </div>
                    </div>
                    
                    <!-- Соперник -->
                    <div>
                        <h6 class="text-danger mb-2 text-center">Соперник</h6>
                        
                        <!-- Повреждения -->
                        <div class="mb-2">
                            <label class="form-label small">Повреждения головы</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_head_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_head_damage" 
                                       name="rounds[${roundNumber-1}].opponentHeadDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_head_damage')">+</button>
                            </div>
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Повреждения корпуса</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_body_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_body_damage" 
                                       name="rounds[${roundNumber-1}].opponentBodyDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_body_damage')">+</button>
                            </div>
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Повреждения ног</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_leg_damage')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_leg_damage" 
                                       name="rounds[${roundNumber-1}].opponentLegDamage" min="0" max="100">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_leg_damage')">+</button>
                            </div>
                        </div>
                        
                        <!-- Нокдауны -->
                        <div class="mb-2">
                            <label class="form-label small">Нокдауны</label>
                            <div class="input-group input-group-sm">
                                <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_knockdowns')">-</button>
                                <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_knockdowns" 
                                       name="rounds[${roundNumber-1}].opponentKnockdowns" min="0" max="10">
                                <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_knockdowns')">+</button>
                            </div>
                        </div>
                        
                        <!-- Значимые удары -->
                        <div class="mb-2">
                            <label class="form-label small">Значимые удары (попал)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_significant_landed" 
                                   name="rounds[${roundNumber-1}].opponentSignificantStrikesLanded" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Значимые удары (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_significant_attempted" 
                                   name="rounds[${roundNumber-1}].opponentSignificantStrikesAttempted" min="0">
                        </div>
                        
                        <!-- Все удары -->
                        <div class="mb-2">
                            <label class="form-label small">Все удары (попал)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_total_landed" 
                                   name="rounds[${roundNumber-1}].opponentTotalStrikesLanded" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Все удары (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_total_attempted" 
                                   name="rounds[${roundNumber-1}].opponentTotalStrikesAttempted" min="0">
                        </div>
                        
                        <!-- Тейкдауны -->
                        <div class="mb-2">
                            <label class="form-label small">Тейкдауны (успешные)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_takedowns_successful" 
                                   name="rounds[${roundNumber-1}].opponentTakedownsSuccessful" min="0">
                        </div>
                        
                        <div class="mb-2">
                            <label class="form-label small">Тейкдауны (всего)</label>
                            <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_takedowns_attempted" 
                                   name="rounds[${roundNumber-1}].opponentTakedownsAttempted" min="0">
                        </div>
                        
                        <!-- Контроль времени -->
                        <div class="mb-2">
                            <label class="form-label small">Контроль (мм:сс)</label>
                            <input type="text" class="form-control form-control-sm" id="round${roundNumber}_opponent_control_time" 
                                   name="rounds[${roundNumber-1}].opponentControlTime" pattern="[0-5][0-9]:[0-5][0-9]"
                                   maxlength="5">
                        </div>
                    </div>
                </div>
            </div>
        `;
        return column;
    }
    
    function createJudgeCard(judgeNumber) {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-header">
                <h6 class="mb-0">Судья ${judgeNumber}</h6>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-sm table-bordered">
                        <thead>
                            <tr>
                                <th>Боец</th>
                                <th>Раунд 1</th>
                                <th>Раунд 2</th>
                                <th>Раунд 3</th>
                                <th>Раунд 4</th>
                                <th>Раунд 5</th>
                                <th>Итого</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="fw-bold text-primary">Мой боец</td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_my_round1')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_my_round1" 
                                               name="judgeScores[${judgeNumber-1}].round1MyScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_my_round1')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_my_round2')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_my_round2" 
                                               name="judgeScores[${judgeNumber-1}].round2MyScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_my_round2')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_my_round3')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_my_round3" 
                                               name="judgeScores[${judgeNumber-1}].round3MyScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_my_round3')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_my_round4')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_my_round4" 
                                               name="judgeScores[${judgeNumber-1}].round4MyScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_my_round4')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_my_round5')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_my_round5" 
                                               name="judgeScores[${judgeNumber-1}].round5MyScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_my_round5')">+</button>
                                    </div>
                                </td>
                                <td class="fw-bold text-center" id="judge${judgeNumber}_my_total">0</td>
                            </tr>
                            <tr>
                                <td class="fw-bold text-danger">Соперник</td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_opponent_round1')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_opponent_round1" 
                                               name="judgeScores[${judgeNumber-1}].round1OpponentScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_opponent_round1')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_opponent_round2')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_opponent_round2" 
                                               name="judgeScores[${judgeNumber-1}].round2OpponentScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_opponent_round2')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_opponent_round3')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_opponent_round3" 
                                               name="judgeScores[${judgeNumber-1}].round3OpponentScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_opponent_round3')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_opponent_round4')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_opponent_round4" 
                                               name="judgeScores[${judgeNumber-1}].round4OpponentScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_opponent_round4')">+</button>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group input-group-sm">
                                        <button type="button" class="btn btn-outline-secondary" onclick="decrementJudgeScore('judge${judgeNumber}_opponent_round5')">-</button>
                                        <input type="number" class="form-control text-center" id="judge${judgeNumber}_opponent_round5" 
                                               name="judgeScores[${judgeNumber-1}].round5OpponentScore" min="0" max="10">
                                        <button type="button" class="btn btn-outline-secondary" onclick="incrementJudgeScore('judge${judgeNumber}_opponent_round5')">+</button>
                                    </div>
                                </td>
                                <td class="fw-bold text-center" id="judge${judgeNumber}_opponent_total">0</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        return card;
    }
});

// Глобальные функции для кнопок
function incrementValue(inputId) {
    const input = document.getElementById(inputId);
    const max = parseInt(input.getAttribute('max')) || 999;
    const current = parseInt(input.value) || 0;
    if (current < max) {
        input.value = current + 1;
    }
}

function decrementValue(inputId) {
    const input = document.getElementById(inputId);
    const min = parseInt(input.getAttribute('min')) || 0;
    const current = parseInt(input.value) || 0;
    if (current > min) {
        input.value = current - 1;
    }
}

function incrementJudgeScore(inputId) {
    const input = document.getElementById(inputId);
    const current = parseInt(input.value) || 0;
    if (current < 10) {
        input.value = current + 1;
        updateJudgeTotal(inputId);
    }
}

function decrementJudgeScore(inputId) {
    const input = document.getElementById(inputId);
    const current = parseInt(input.value) || 0;
    if (current > 0) {
        input.value = current - 1;
        updateJudgeTotal(inputId);
    }
}

function updateJudgeTotal(inputId) {
    const judgeNumber = inputId.match(/judge(\d+)/)[1];
    const isMyFighter = inputId.includes('_my_');
    const totalId = `judge${judgeNumber}_${isMyFighter ? 'my' : 'opponent'}_total`;
    
    let total = 0;
    for (let round = 1; round <= 5; round++) {
        const roundInputId = `judge${judgeNumber}_${isMyFighter ? 'my' : 'opponent'}_round${round}`;
        const roundInput = document.getElementById(roundInputId);
        if (roundInput) {
            total += parseInt(roundInput.value) || 0;
        }
    }
    
    const totalElement = document.getElementById(totalId);
    if (totalElement) {
        totalElement.textContent = total;
    }
}
