// test.js - Полная логика для тестирования

// Глобальные переменные
let currentQuestion = 0;
let answers = {};
let userId = null;
let testStartTime = null;
let questionTimes = [];

// Конфигурация вопросов для разных типов пользователей
const questions = {
    schoolboy: [
        {
            id: 1,
            text: "Когда ты думаешь о поступлении, что ты обычно делаешь?",
            type: "radio",
            options: [
                "Сравниваю программы и предметы",
                "Смотрю отзывы студентов",
                "Ориентируюсь на советы родителей",
                "Выбираю по проходному баллу",
                "Выбираю то, где 'больше шансов'",
                "Уже давно знаю, куда хочу"
            ],
            category: "independence",
            weight: 2
        },
        {
            id: 2,
            text: "Как часто ты менял(а) предполагаемое направление за последний год?",
            type: "radio",
            options: [
                "Ни разу",
                "1–2 раза",
                "3–4 раза",
                "Постоянно сомневаюсь"
            ],
            category: "stability",
            weight: 3
        },
        {
            id: 3,
            text: "Если представить себя через 5 лет, тебе легче:",
            type: "radio",
            options: [
                "Описать конкретную работу и задачи",
                "Назвать только сферу",
                "Сказать лишь общее направление",
                "Сложно представить"
            ],
            category: "awareness",
            weight: 3
        },
        {
            id: 4,
            text: "Если предмет кажется сложным, ты чаще:",
            type: "radio",
            options: [
                "Углубляюсь и пытаюсь понять",
                "Стараюсь освоить минимум",
                "Теряю интерес",
                "Избегаю"
            ],
            category: "motivation",
            weight: 2
        },
        {
            id: 5,
            text: "Если все аналитики любят данные, а Иван любит данные, можно ли утверждать, что Иван аналитик?",
            type: "radio",
            options: [
                "Да",
                "Нет",
                "Недостаточно информации"
            ],
            correctAnswer: 2, // Нет
            category: "logic",
            weight: 2
        },
        {
            id: 6,
            text: "Продолжи последовательность: 2, 6, 12, 20, ?",
            type: "radio",
            options: ["26", "28", "30", "32"],
            correctAnswer: 3, // 30
            category: "logic",
            weight: 2
        }
    ],
    student: [
        {
            id: 1,
            text: "Насколько текущая программа совпала с тем, что ты ожидал(а)?",
            type: "radio",
            options: [
                "Полностью совпала",
                "В целом да",
                "Есть существенные расхождения",
                "Совсем не то, что представлял(а)"
            ],
            category: "expectations",
            weight: 3
        },
        {
            id: 2,
            text: "Бывали ли мысли изучить другое направление параллельно?",
            type: "radio",
            options: [
                "Нет",
                "Иногда",
                "Часто",
                "Уже изучаю альтернативу"
            ],
            category: "curiosity",
            weight: 2
        },
        {
            id: 3,
            text: "После учебного дня ты чаще чувствуешь:",
            type: "radio",
            options: [
                "Интерес и желание углубиться",
                "Нормально, без эмоций",
                "Усталость и сомнения",
                "Разочарование"
            ],
            category: "energy",
            weight: 2
        },
        {
            id: 4,
            text: "Насколько понятно тебе, какие навыки реально востребованы в профессии?",
            type: "radio",
            options: [
                "Очень понятно",
                "В общих чертах",
                "Примерно",
                "Практически не понимаю"
            ],
            category: "market_understanding",
            weight: 3
        },
        {
            id: 5,
            text: "В таблице 3 столбца с разными значениями. Как бы ты искал закономерности и делал вывод о зависимости?",
            type: "text",
            placeholder: "Опиши свой подход к анализу данных...",
            category: "analytics",
            weight: 3
        },
        {
            id: 6,
            text: "Если бы можно было начать заново без потери времени, ты бы:",
            type: "radio",
            options: [
                "Выбрал(а) то же самое",
                "Изменил(а) вуз",
                "Изменил(а) направление",
                "Выбрал(а) бы совсем другую сферу"
            ],
            category: "satisfaction",
            weight: 3
        }
    ],
    working: [
        {
            id: 1,
            text: "Насколько текущая работа соответствует тому, что ты представлял(а) в начале пути?",
            type: "radio",
            options: [
                "Почти идеально",
                "В целом соответствует",
                "Сильно отличается",
                "Совсем не соответствует"
            ],
            category: "expectations",
            weight: 3
        },
        {
            id: 2,
            text: "За последний год ты:",
            type: "radio",
            options: [
                "Углублялся в своей сфере",
                "Изучал смежные навыки",
                "Осваивал новую область",
                "Задумывался о смене профессии"
            ],
            category: "development",
            weight: 3
        },
        {
            id: 3,
            text: "Что ты чаще чувствуешь в конце недели?",
            type: "radio",
            options: [
                "Удовлетворение",
                "Нейтральность",
                "Эмоциональное истощение",
                "Желание всё поменять"
            ],
            category: "satisfaction",
            weight: 2
        },
        {
            id: 4,
            text: "Насколько полезен был бы инструмент, показывающий реальные задачи профессии?",
            type: "radio",
            options: [
                "Очень полезно",
                "Скорее полезно",
                "Не знаю",
                "Вряд ли полезно"
            ],
            category: "self_awareness",
            weight: 2
        },
        {
            id: 5,
            text: "Как ты оцениваешь текущий баланс работы и личной жизни?",
            type: "radio",
            options: [
                "Отличный баланс",
                "Хороший",
                "Средний",
                "Плохой"
            ],
            category: "balance",
            weight: 2
        },
        {
            id: 6,
            text: "Что для тебя важнее в работе?",
            type: "radio",
            options: [
                "Финансовое вознаграждение",
                "Интересные задачи",
                "Коллектив и атмосфера",
                "Возможность роста",
                "Стабильность"
            ],
            category: "values",
            weight: 2
        }
    ]
};

// Инициализация теста
async function initTest() {
    showLoader();

    try {
        testStartTime = new Date();

        // Получаем тип пользователя из URL
        const path = window.location.pathname;
        const userType = path.split('/').pop();

        // Создаем пользователя на сервере
        const response = await fetch(`/api/start-test?userType=${userType}&sessionId=${sessionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to start test');
        }

        const data = await response.json();
        userId = data.userId;

        // Загружаем первый вопрос
        loadQuestion(0);
        updateProgress();

        // Сохраняем время начала вопроса
        startQuestionTimer();

    } catch (error) {
        console.error('Error initializing test:', error);
        showModal('Ошибка', 'Не удалось начать тест. Пожалуйста, обновите страницу.');
    } finally {
        hideLoader();
    }
}

// Загрузка вопроса
function loadQuestion(index) {
    const container = document.getElementById('questionsContainer');
    const userType = getUserTypeFromUrl();
    const question = questions[userType][index];

    let html = `<div class="question active" data-question-id="${question.id}">
        <div class="question-header">
            <h3>${question.text}</h3>
            ${question.category ? `<span class="question-category">${getCategoryName(question.category)}</span>` : ''}
        </div>
        <div class="options">`;

    if (question.type === 'text') {
        html += `<textarea 
            name="q${index + 1}" 
            placeholder="${question.placeholder || 'Введите ваш ответ...'}"
            oninput="saveAnswer(${index + 1}, this.value)"
            rows="4">${answers[`q${index + 1}`] || ''}</textarea>`;
    } else if (question.options) {
        question.options.forEach((option, optIndex) => {
            const checked = answers[`q${index + 1}`] === optIndex + 1 ? 'checked' : '';
            const optionId = `q${index + 1}_opt${optIndex + 1}`;

            html += `<label class="option" for="${optionId}">
                <input type="radio" 
                    id="${optionId}"
                    name="q${index + 1}" 
                    value="${optIndex + 1}"
                    ${checked}
                    onchange="saveAnswer(${index + 1}, ${optIndex + 1})">
                <span class="radio-custom"></span>
                <span class="option-text">${option}</span>
            </label>`;
        });
    }

    html += `</div>
        <div class="question-footer">
            ${answers[`q${index + 1}`] ?
        '<span class="answered-indicator">✓ Отвечено</span>' :
        '<span class="required-indicator">* Обязательный вопрос</span>'}
        </div>
    </div>`;

    container.innerHTML = html;

    // Анимируем появление вопроса
    const questionElement = container.querySelector('.question');
    questionElement.style.animation = 'fadeIn 0.5s ease';

    updateNavigation(index);
    updateQuestionCounter(index);

    // Запускаем таймер для нового вопроса
    startQuestionTimer();
}

// Сохранение ответа
function saveAnswer(questionNum, value) {
    answers[`q${questionNum}`] = value;

    // Сохраняем время ответа на вопрос
    const timeSpent = stopQuestionTimer();
    questionTimes.push({
        question: questionNum,
        timeSpent: timeSpent,
        answer: value
    });

    // Обновляем индикатор ответа
    const questionElement = document.querySelector('.question.active');
    if (questionElement) {
        const footer = questionElement.querySelector('.question-footer');
        if (footer) {
            footer.innerHTML = '<span class="answered-indicator">✓ Отвечено</span>';
        }
    }

    // Автоматически переходим к следующему вопросу через небольшую задержку
    // (для лучшего UX, но не для последнего вопроса)
    const userType = getUserTypeFromUrl();
    if (questionNum < questions[userType].length) {
        clearTimeout(window.autoNextTimeout);
        window.autoNextTimeout = setTimeout(() => {
            if (questionNum === currentQuestion + 1) {
                nextQuestion();
            }
        }, 500);
    }
}

// Переход к следующему вопросу
function nextQuestion() {
    const userType = getUserTypeFromUrl();

    // Проверяем, отвечен ли текущий вопрос
    if (!answers[`q${currentQuestion + 1}`]) {
        showModal('Внимание', 'Пожалуйста, ответьте на вопрос перед переходом к следующему');
        return;
    }

    if (currentQuestion < questions[userType].length - 1) {
        // Анимация перехода
        const currentQuestionElement = document.querySelector('.question.active');
        currentQuestionElement.style.animation = 'fadeOut 0.3s ease';

        setTimeout(() => {
            currentQuestion++;
            loadQuestion(currentQuestion);
            updateProgress();

            // Прокручиваем к началу вопроса
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        }, 300);
    }
}

// Переход к предыдущему вопросу
function prevQuestion() {
    if (currentQuestion > 0) {
        // Анимация перехода
        const currentQuestionElement = document.querySelector('.question.active');
        currentQuestionElement.style.animation = 'fadeOut 0.3s ease';

        setTimeout(() => {
            currentQuestion--;
            loadQuestion(currentQuestion);
            updateProgress();
        }, 300);
    }
}

// Обновление навигации
function updateNavigation(index) {
    const userType = getUserTypeFromUrl();
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const submitBtn = document.getElementById('submitBtn');

    if (prevBtn) {
        prevBtn.disabled = index === 0;
    }

    if (index === questions[userType].length - 1) {
        if (nextBtn) nextBtn.style.display = 'none';
        if (submitBtn) submitBtn.style.display = 'inline-block';
    } else {
        if (nextBtn) nextBtn.style.display = 'inline-block';
        if (submitBtn) submitBtn.style.display = 'none';
    }
}

// Обновление счетчика вопросов
function updateQuestionCounter(index) {
    const userType = getUserTypeFromUrl();
    const counter = document.getElementById('questionCounter');
    if (counter) {
        counter.textContent = `Вопрос ${index + 1} из ${questions[userType].length}`;
    }
}

// Обновление прогресс-бара
function updateProgress() {
    const userType = getUserTypeFromUrl();
    const progress = ((currentQuestion + 1) / questions[userType].length) * 100;
    const progressBar = document.getElementById('progressBar');
    if (progressBar) {
        progressBar.style.width = progress + '%';
    }
}

// Получение типа пользователя из URL
function getUserTypeFromUrl() {
    const path = window.location.pathname;
    return path.split('/').pop();
}

// Получение названия категории
function getCategoryName(category) {
    const categories = {
        independence: 'Самостоятельность',
        stability: 'Стабильность',
        awareness: 'Осознанность',
        motivation: 'Мотивация',
        logic: 'Логическое мышление',
        expectations: 'Ожидания',
        curiosity: 'Любознательность',
        energy: 'Энергия',
        market_understanding: 'Понимание рынка',
        analytics: 'Аналитика',
        satisfaction: 'Удовлетворенность',
        development: 'Развитие',
        balance: 'Баланс',
        values: 'Ценности'
    };
    return categories[category] || category;
}

// Таймер для вопросов
function startQuestionTimer() {
    window.questionStartTime = new Date();
}

function stopQuestionTimer() {
    if (window.questionStartTime) {
        const endTime = new Date();
        return (endTime - window.questionStartTime) / 1000; // время в секундах
    }
    return 0;
}


// Отправка теста
async function submitTest() {
    // Проверяем, отвечен ли последний вопрос
    const userType = getUserTypeFromUrl();
    if (!answers[`q${questions[userType].length}`]) {
        showModal('Внимание', 'Пожалуйста, ответьте на последний вопрос');
        return;
    }

    // Показываем подтверждение
    const confirmSubmit = await showConfirmModal('Завершить тест?',
        'Вы уверены, что хотите завершить тест? Вы не сможете изменить ответы после отправки.');

    if (!confirmSubmit) return;

    showLoader();

    try {
        // Подготавливаем данные для отправки - только q1-q6 и userType
        const testData = {
            userType: userType,
            q1: answers.q1,
            q2: answers.q2,
            q3: answers.q3,
            q4: answers.q4,
            q5: answers.q5,
            q6: answers.q6
        };

        console.log("Отправляемые данные:", testData);

        const response = await fetch(`/api/submit-test/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(testData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error("Ошибка сервера:", errorData);
            throw new Error(errorData.error || 'Failed to submit test');
        }

        const result = await response.json();
        console.log("Ответ от сервера:", result);

        // Сохраняем результат и переходим на страницу результатов
        sessionStorage.setItem('testResult', JSON.stringify(result));
        window.location.href = '/result';

    } catch (error) {
        console.error('Error submitting test:', error);
        showModal('Ошибка', 'Не удалось отправить результаты: ' + error.message);
    } finally {
        hideLoader();
    }
}

// Валидация ответов
function validateAnswers() {
    const userType = getUserTypeFromUrl();
    const missingAnswers = [];

    for (let i = 1; i <= questions[userType].length; i++) {
        if (!answers[`q${i}`]) {
            missingAnswers.push(i);
        }
    }

    return {
        isValid: missingAnswers.length === 0,
        missing: missingAnswers
    };
}

// Показ модального окна
function showModal(title, message) {
    const modal = document.getElementById('modal');
    const modalTitle = document.getElementById('modalTitle');
    const modalMessage = document.getElementById('modalMessage');

    if (modal && modalTitle && modalMessage) {
        modalTitle.textContent = title;
        modalMessage.textContent = message;
        modal.style.display = 'flex';
    } else {
        alert(`${title}: ${message}`);
    }
}

// Закрытие модального окна
function closeModal() {
    const modal = document.getElementById('modal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// Показ модального окна с подтверждением
function showConfirmModal(title, message) {
    return new Promise((resolve) => {
        // Создаем кастомный confirm
        const modal = document.createElement('div');
        modal.className = 'modal confirm-modal';
        modal.innerHTML = `
            <div class="modal-content">
                <h3>${title}</h3>
                <p>${message}</p>
                <div class="modal-actions">
                    <button class="btn btn-secondary" onclick="this.closest('.modal').remove(); resolve(false)">Отмена</button>
                    <button class="btn btn-primary" onclick="this.closest('.modal').remove(); resolve(true)">Подтвердить</button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        // Обработчики
        const cancelBtn = modal.querySelector('.btn-secondary');
        const confirmBtn = modal.querySelector('.btn-primary');

        cancelBtn.onclick = () => {
            modal.remove();
            resolve(false);
        };

        confirmBtn.onclick = () => {
            modal.remove();
            resolve(true);
        };
    });
}

// Показ загрузчика
function showLoader() {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = 'flex';
    }
}

// Скрытие загрузчика
function hideLoader() {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = 'none';
    }
}

// Сохранение черновика
function saveDraft() {
    try {
        localStorage.setItem('testDraft_' + userId, JSON.stringify({
            answers: answers,
            currentQuestion: currentQuestion,
            timestamp: new Date().toISOString()
        }));
        showModal('Сохранено', 'Черновик успешно сохранен');
    } catch (error) {
        console.error('Error saving draft:', error);
    }
}

// Загрузка черновика
function loadDraft() {
    try {
        const draft = localStorage.getItem('testDraft_' + userId);
        if (draft) {
            const draftData = JSON.parse(draft);
            const draftAge = (new Date() - new Date(draftData.timestamp)) / (1000 * 60); // в минутах

            if (draftAge < 60) { // черновик не старше часа
                showConfirmModal('Найден черновик',
                    'У вас есть несохраненный черновик от ' + new Date(draftData.timestamp).toLocaleString() +
                    '. Хотите восстановить?')
                    .then(confirmed => {
                        if (confirmed) {
                            answers = draftData.answers;
                            currentQuestion = draftData.currentQuestion;
                            loadQuestion(currentQuestion);
                            updateProgress();
                        }
                    });
            } else {
                localStorage.removeItem('testDraft_' + userId);
            }
        }
    } catch (error) {
        console.error('Error loading draft:', error);
    }
}

// Обработка клавиш
document.addEventListener('keydown', function(e) {
    // Ctrl + S для сохранения черновика
    if (e.ctrlKey && e.key === 's') {
        e.preventDefault();
        saveDraft();
    }

    // Стрелки для навигации (только если нет модального окна)
    if (!document.querySelector('.modal')) {
        if (e.key === 'ArrowRight' && !e.ctrlKey && !e.altKey && !e.shiftKey) {
            e.preventDefault();
            const userType = getUserTypeFromUrl();
            if (currentQuestion < questions[userType].length - 1) {
                nextQuestion();
            }
        } else if (e.key === 'ArrowLeft' && !e.ctrlKey && !e.altKey && !e.shiftKey) {
            e.preventDefault();
            if (currentQuestion > 0) {
                prevQuestion();
            }
        }
    }
});

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Добавляем стили для анимаций
    const style = document.createElement('style');
    style.textContent = `
        @keyframes fadeOut {
            from { opacity: 1; transform: translateX(0); }
            to { opacity: 0; transform: translateX(-20px); }
        }
        
        .confirm-modal .modal-content {
            max-width: 400px;
            text-align: center;
        }
        
        .confirm-modal .modal-actions {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
        }
        
        .question-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
            gap: 10px;
        }
        
        .question-category {
            background: #667eea;
            color: white;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
        }
        
        .answered-indicator {
            color: #4CAF50;
            font-size: 0.9rem;
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        .required-indicator {
            color: #f44336;
            font-size: 0.9rem;
        }
        
        .question-footer {
            margin-top: 20px;
            text-align: right;
        }
    `;
    document.head.appendChild(style);

    initTest();

    // Пробуем загрузить черновик через небольшую задержку
    setTimeout(loadDraft, 1000);
});