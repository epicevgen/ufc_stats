// Управление формой создания/редактирования боя
document.addEventListener('DOMContentLoaded', function() {
    const roundsPlayedInput = document.getElementById('roundsPlayed');
    const roundsContainer = document.getElementById('roundsContainer');
    const judgesContainer = document.getElementById('judgesContainer');
    
    // Инициализация формы
    initializeForm();
    
    // Обработчики событий
    roundsPlayedInput.addEventListener('input', updateRounds);
    
    // Валидация формы
    const form = document.querySelector('.needs-validation');
    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    });
    
    function initializeForm() {
        const roundsCount = parseInt(roundsPlayedInput.value) || 1;
        updateRounds();
        updateJudges();
        
        // Проверяем, что поле даты заполнено (должно быть заполнено сервером)
        const fightDateInput = document.getElementById('fightDate');
        if (fightDateInput) {
            console.log('Fight date input value:', fightDateInput.value);
        }
    }
    
    function updateRounds() {
        const roundsCount = parseInt(roundsPlayedInput.value) || 1;
        roundsContainer.innerHTML = '';
        
        for (let round = 1; round <= roundsCount; round++) {
            const roundCard = createRoundCard(round);
            roundsContainer.appendChild(roundCard);
        }
    }
    
    function updateJudges() {
        judgesContainer.innerHTML = '';
        
        for (let judge = 1; judge <= 3; judge++) {
            const judgeCard = createJudgeCard(judge);
            judgesContainer.appendChild(judgeCard);
        }
    }
    
    function createRoundCard(roundNumber) {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-header">
                <h6 class="mb-0">Раунд ${roundNumber}</h6>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <h6 class="text-primary">Мой боец</h6>
                        <div class="row g-2">
                            <div class="col-4">
                                <label class="form-label small">Повреждения головы</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_head_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_my_head_damage" 
                                           name="rounds[${roundNumber-1}].myHeadDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_head_damage')">+</button>
                                </div>
                            </div>
                            <div class="col-4">
                                <label class="form-label small">Повреждения корпуса</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_body_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_my_body_damage" 
                                           name="rounds[${roundNumber-1}].myBodyDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_body_damage')">+</button>
                                </div>
                            </div>
                            <div class="col-4">
                                <label class="form-label small">Повреждения ног</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_leg_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_my_leg_damage" 
                                           name="rounds[${roundNumber-1}].myLegDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_leg_damage')">+</button>
                                </div>
                            </div>
                        </div>
                        <div class="row g-2 mt-2">
                            <div class="col-3">
                                <label class="form-label small">Нокдауны</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_my_knockdowns')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_my_knockdowns" 
                                           name="rounds[${roundNumber-1}].myKnockdowns" min="0" max="10">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_my_knockdowns')">+</button>
                                </div>
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Значимые удары (попал)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_significant_landed" 
                                       name="rounds[${roundNumber-1}].mySignificantStrikesLanded" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Значимые удары (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_significant_attempted" 
                                       name="rounds[${roundNumber-1}].mySignificantStrikesAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Все удары (попал)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_total_landed" 
                                       name="rounds[${roundNumber-1}].myTotalStrikesLanded" min="0">
                            </div>
                        </div>
                        <div class="row g-2 mt-2">
                            <div class="col-3">
                                <label class="form-label small">Все удары (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_total_attempted" 
                                       name="rounds[${roundNumber-1}].myTotalStrikesAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Тейкдауны (успешные)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_takedowns_successful" 
                                       name="rounds[${roundNumber-1}].myTakedownsSuccessful" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Тейкдауны (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_my_takedowns_attempted" 
                                       name="rounds[${roundNumber-1}].myTakedownsAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Контроль (мм:сс)</label>
                                <input type="text" class="form-control form-control-sm" id="round${roundNumber}_my_control_time" 
                                       name="rounds[${roundNumber-1}].myControlTime" pattern="[0-5][0-9]:[0-5][0-9]">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <h6 class="text-danger">Соперник</h6>
                        <div class="row g-2">
                            <div class="col-4">
                                <label class="form-label small">Повреждения головы</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_head_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_head_damage" 
                                           name="rounds[${roundNumber-1}].opponentHeadDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_head_damage')">+</button>
                                </div>
                            </div>
                            <div class="col-4">
                                <label class="form-label small">Повреждения корпуса</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_body_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_body_damage" 
                                           name="rounds[${roundNumber-1}].opponentBodyDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_body_damage')">+</button>
                                </div>
                            </div>
                            <div class="col-4">
                                <label class="form-label small">Повреждения ног</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_leg_damage')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_leg_damage" 
                                           name="rounds[${roundNumber-1}].opponentLegDamage" min="0" max="100">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_leg_damage')">+</button>
                                </div>
                            </div>
                        </div>
                        <div class="row g-2 mt-2">
                            <div class="col-3">
                                <label class="form-label small">Нокдауны</label>
                                <div class="input-group input-group-sm">
                                    <button type="button" class="btn btn-outline-secondary" onclick="decrementValue('round${roundNumber}_opponent_knockdowns')">-</button>
                                    <input type="number" class="form-control text-center" id="round${roundNumber}_opponent_knockdowns" 
                                           name="rounds[${roundNumber-1}].opponentKnockdowns" min="0" max="10">
                                    <button type="button" class="btn btn-outline-secondary" onclick="incrementValue('round${roundNumber}_opponent_knockdowns')">+</button>
                                </div>
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Значимые удары (попал)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_significant_landed" 
                                       name="rounds[${roundNumber-1}].opponentSignificantStrikesLanded" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Значимые удары (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_significant_attempted" 
                                       name="rounds[${roundNumber-1}].opponentSignificantStrikesAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Все удары (попал)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_total_landed" 
                                       name="rounds[${roundNumber-1}].opponentTotalStrikesLanded" min="0">
                            </div>
                        </div>
                        <div class="row g-2 mt-2">
                            <div class="col-3">
                                <label class="form-label small">Все удары (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_total_attempted" 
                                       name="rounds[${roundNumber-1}].opponentTotalStrikesAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Тейкдауны (успешные)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_takedowns_successful" 
                                       name="rounds[${roundNumber-1}].opponentTakedownsSuccessful" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Тейкдауны (всего)</label>
                                <input type="number" class="form-control form-control-sm" id="round${roundNumber}_opponent_takedowns_attempted" 
                                       name="rounds[${roundNumber-1}].opponentTakedownsAttempted" min="0">
                            </div>
                            <div class="col-3">
                                <label class="form-label small">Контроль (мм:сс)</label>
                                <input type="text" class="form-control form-control-sm" id="round${roundNumber}_opponent_control_time" 
                                       name="rounds[${roundNumber-1}].opponentControlTime" pattern="[0-5][0-9]:[0-5][0-9]">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        return card;
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
