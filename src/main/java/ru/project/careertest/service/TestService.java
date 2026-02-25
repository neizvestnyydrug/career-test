package ru.project.careertest.service;

import ru.project.careertest.dto.TestAnswersDTO;
import ru.project.careertest.dto.TestResultDTO;
import ru.project.careertest.model.*;
import ru.project.careertest.repository.TestResultRepository;
import ru.project.careertest.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public User createUser(String sessionId, UserType userType) {
        logger.info("Создание нового пользователя: sessionId={}, userType={}", sessionId, userType);
        User user = new User();
        user.setSessionId(sessionId);
        user.setUserType(userType);
        user.setStartedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        logger.info("Пользователь создан с ID: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public TestResultDTO processTestResults(UUID userId, TestAnswersDTO answers) {
        logger.info("=== НАЧАЛО ОБРАБОТКИ РЕЗУЛЬТАТОВ ТЕСТА ===");
        logger.info("User ID: {}", userId);
        logger.info("User Type: {}", answers.getUserType());
        logger.info("Ответы на вопросы:");
        logger.info("q1 = {}", answers.getQ1());
        logger.info("q2 = {}", answers.getQ2());
        logger.info("q3 = {}", answers.getQ3());
        logger.info("q4 = {}", answers.getQ4());
        logger.info("q5 = {}", answers.getQ5());
        logger.info("q6 = {}", answers.getQ6());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Пользователь с ID {} не найден!", userId);
                    return new RuntimeException("User not found");
                });

        user.setCompletedAt(LocalDateTime.now());
        userRepository.save(user);

        TestResult result = new TestResult();
        result.setUser(user);

        try {
            String answersJson = objectMapper.writeValueAsString(answers);
            result.setAnswers(answersJson);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка при сериализации ответов в JSON", e);
            result.setAnswers("{}");
        }

        // Расчет скоринга в зависимости от типа пользователя
        calculateScores(result, answers);

        // Генерация рекомендации
        generateRecommendation(result);

        TestResult saved = testResultRepository.save(result);
        logger.info("Результаты теста сохранены в БД с ID: {}", saved.getId());

        return convertToDTO(result);
    }

    private void calculateScores(TestResult result, TestAnswersDTO answers) {
        UserType userType = UserType.valueOf(answers.getUserType());
        logger.info("Расчет скоринга для userType: {}", userType);

        switch (userType) {
            case SCHOOLBOY:
                calculateSchoolboyScores(result, answers);
                break;
            case STUDENT:
                calculateStudentScores(result, answers);
                break;
            case WORKING:
                calculateWorkingScores(result, answers);
                break;
            default:
                logger.error("Неизвестный тип пользователя: {}", userType);
        }
    }

    private void calculateSchoolboyScores(TestResult result, TestAnswersDTO answers) {
        try {
            // Вопрос 1: Самостоятельность выбора
            Integer q1Independence = 0;
            if (answers.getQ1() != null) {
                int q1Value = answers.getQ1();
                if (q1Value == 6) q1Independence = 5;
                else if (q1Value <= 2) q1Independence = 4;
                else if (q1Value <= 4) q1Independence = 3;
                else q1Independence = 2;
            }

            // Вопрос 2: Стабильность решения
            Integer q2Stability = 0;
            if (answers.getQ2() != null) {
                int q2Value = answers.getQ2();
                if (q2Value == 1) q2Stability = 5;
                else if (q2Value == 2) q2Stability = 4;
                else if (q2Value == 3) q2Stability = 2;
                else q2Stability = 1;
            }

            // Вопрос 3: Осознанность
            Integer q3Awareness = 0;
            if (answers.getQ3() != null) {
                int q3Value = answers.getQ3();
                if (q3Value == 1) q3Awareness = 5;
                else if (q3Value == 2) q3Awareness = 4;
                else if (q3Value == 3) q3Awareness = 2;
                else q3Awareness = 1;
            }

            // Вопрос 4: Мотивация
            Integer q4Motivation = 0;
            if (answers.getQ4() != null) {
                int q4Value = answers.getQ4();
                if (q4Value == 1) q4Motivation = 5;
                else if (q4Value == 2) q4Motivation = 3;
                else if (q4Value == 3) q4Motivation = 2;
                else q4Motivation = 1;
            }

            // Вопрос 5-6: Аналитические способности
            Integer q5Analytical = 0;

            // Логическая задача (вопрос 5: правильный ответ - 2)
            if (answers.getQ5() != null) {
                int q5Value;
                if (answers.getQ5() instanceof Number) {
                    q5Value = ((Number) answers.getQ5()).intValue();
                } else {
                    try {
                        q5Value = Integer.parseInt(answers.getQ5().toString());
                    } catch (NumberFormatException e) {
                        q5Value = 0;
                    }
                }
                if (q5Value == 2) q5Analytical += 3;
            }

            // Последовательность (вопрос 6: правильный ответ - 30)
            if (answers.getQ6() != null) {
                int q6Value = answers.getQ6();
                if (q6Value == 30) q5Analytical += 2;
            }

            result.setAwarenessScore(q3Awareness);
            result.setStabilityScore(q2Stability);
            result.setMotivationScore(q1Independence + q4Motivation);
            result.setAnalyticalScore(q5Analytical);

        } catch (Exception e) {
            logger.error("Ошибка при расчете баллов для школьника", e);
            setDefaultScores(result);
        }
    }

    private void calculateStudentScores(TestResult result, TestAnswersDTO answers) {
        try {
            // Вопрос 1: Совпадение ожиданий
            Integer q1Score = 0;
            if (answers.getQ1() != null) {
                int q1Value = answers.getQ1();
                switch (q1Value) {
                    case 1: q1Score = 5; break;
                    case 2: q1Score = 3; break;
                    case 3: q1Score = 1; break;
                    case 4: q1Score = 0; break;
                }
            }

            // Вопрос 2: Интерес к другим направлениям
            Integer q2Score = 0;
            if (answers.getQ2() != null) {
                int q2Value = answers.getQ2();
                switch (q2Value) {
                    case 1: q2Score = 5; break;
                    case 2: q2Score = 3; break;
                    case 3: q2Score = 1; break;
                    case 4: q2Score = 0; break;
                }
            }

            // Вопрос 3: Энергия после учебы
            Integer q3Score = 0;
            if (answers.getQ3() != null) {
                int q3Value = answers.getQ3();
                switch (q3Value) {
                    case 1: q3Score = 5; break;
                    case 2: q3Score = 3; break;
                    case 3: q3Score = 1; break;
                    case 4: q3Score = 0; break;
                }
            }

            // Вопрос 4: Понимание рынка
            Integer q4Score = 0;
            if (answers.getQ4() != null) {
                int q4Value = answers.getQ4();
                switch (q4Value) {
                    case 1: q4Score = 5; break;
                    case 2: q4Score = 3; break;
                    case 3: q4Score = 1; break;
                    case 4: q4Score = 0; break;
                }
            }

            // Вопрос 5: Аналитика (текстовый ответ)
            Integer q5Score = 0;
            if (answers.getQ5() != null) {
                String answer = answers.getQ5().toString().trim();
                if (!answer.isEmpty()) {
                    if (answer.length() > 100) q5Score = 5;
                    else if (answer.length() > 50) q5Score = 4;
                    else if (answer.length() > 20) q5Score = 3;
                    else if (answer.length() > 5) q5Score = 2;
                    else q5Score = 1;
                }
            }

            // Вопрос 6: Альтернативный сценарий
            Integer q6Score = 0;
            if (answers.getQ6() != null) {
                int q6Value = answers.getQ6();
                switch (q6Value) {
                    case 1: q6Score = 5; break;
                    case 2: q6Score = 3; break;
                    case 3: q6Score = 1; break;
                    case 4: q6Score = 0; break;
                }
            }

            result.setAwarenessScore(q1Score * 4);
            result.setStabilityScore(q2Score * 4);
            result.setMotivationScore((q3Score + q4Score) * 2);
            result.setAnalyticalScore(q5Score * 4 + q6Score * 2);

        } catch (Exception e) {
            logger.error("Ошибка при расчете баллов для студента", e);
            setDefaultScores(result);
        }
    }

    private void calculateWorkingScores(TestResult result, TestAnswersDTO answers) {
        try {
            // Вопрос 1: Соответствие работы ожиданиям (1-4)
            Integer q1Score = 0;
            if (answers.getQ1() != null) {
                int q1Value = answers.getQ1();
                q1Score = (5 - q1Value) * 5;
                if (q1Score < 0) q1Score = 0;
            }

            // Вопрос 2: Развитие за последний год (1-4)
            Integer q2Score = 0;
            if (answers.getQ2() != null) {
                int q2Value = answers.getQ2();
                q2Score = (5 - q2Value) * 5;
                if (q2Score < 0) q2Score = 0;
            }

            // Вопрос 3: Энергия от работы (1-4)
            Integer q3Score = 0;
            if (answers.getQ3() != null) {
                int q3Value = answers.getQ3();
                q3Score = (4 - q3Value) * 5;
                if (q3Score < 0) q3Score = 0;
            }

            // Вопрос 4: Полезность инструмента (1-4)
            Integer q4Score = 0;
            if (answers.getQ4() != null) {
                int q4Value = answers.getQ4();
                q4Score = (4 - q4Value) * 5;
                if (q4Score < 0) q4Score = 0;
            }

            // Вопрос 5: Баланс работы и жизни (1-4)
            Integer q5Score = 0;
            if (answers.getQ5() != null) {
                // Для работающих q5 - это число (оценка баланса)
                int q5Value;
                if (answers.getQ5() instanceof Number) {
                    q5Value = ((Number) answers.getQ5()).intValue();
                } else {
                    try {
                        q5Value = Integer.parseInt(answers.getQ5().toString());
                    } catch (NumberFormatException e) {
                        q5Value = 0;
                    }
                }
                q5Score = (4 - q5Value) * 5;
                if (q5Score < 0) q5Score = 0;
            }

            // Вопрос 6: Приоритеты в работе (1-5)
            Integer q6Score = 0;
            if (answers.getQ6() != null) {
                int q6Value = answers.getQ6();
                // Просто бонус за выбор
                q6Score = 3;
            }

            result.setAwarenessScore(q1Score);
            result.setStabilityScore(q2Score);
            result.setMotivationScore(q3Score + q4Score);
            result.setAnalyticalScore(q5Score + q6Score);

        } catch (Exception e) {
            logger.error("Ошибка при расчете баллов для работающего", e);
            setDefaultScores(result);
        }
    }

    private void setDefaultScores(TestResult result) {
        result.setAwarenessScore(0);
        result.setStabilityScore(0);
        result.setMotivationScore(0);
        result.setAnalyticalScore(0);
    }

    private void generateRecommendation(TestResult result) {
        int awareness = result.getAwarenessScore() != null ? result.getAwarenessScore() : 0;
        int stability = result.getStabilityScore() != null ? result.getStabilityScore() : 0;
        int motivation = result.getMotivationScore() != null ? result.getMotivationScore() : 0;
        int analytical = result.getAnalyticalScore() != null ? result.getAnalyticalScore() : 0;

        int totalScore = awareness + stability + motivation + analytical;

        if (totalScore >= 80) {
            result.setRecommendation("У вас отличное понимание своих карьерных целей! Рекомендуем углубиться в выбранное направление и рассмотреть программы менторства.");
            result.setCareerPath("Лидер развития");
        } else if (totalScore >= 60) {
            result.setRecommendation("Хорошая база для развития. Рекомендуем изучить смежные области и пройти профессиональные курсы.");
            result.setCareerPath("Уверенный профессионал");
        } else if (totalScore >= 40) {
            result.setRecommendation("У вас есть потенциал, но требуется больше информации. Рекомендуем карьерные консультации и профориентационные тесты.");
            result.setCareerPath("Исследователь");
        } else {
            result.setRecommendation("Рекомендуем начать с изучения базовых профессий и пройти курс по профориентации.");
            result.setCareerPath("Первооткрыватель");
        }
    }

    private TestResultDTO convertToDTO(TestResult result) {
        TestResultDTO dto = new TestResultDTO();

        dto.setAwarenessScore(result.getAwarenessScore() != null ? result.getAwarenessScore() : 0);
        dto.setStabilityScore(result.getStabilityScore() != null ? result.getStabilityScore() : 0);
        dto.setMotivationScore(result.getMotivationScore() != null ? result.getMotivationScore() : 0);
        dto.setAnalyticalScore(result.getAnalyticalScore() != null ? result.getAnalyticalScore() : 0);
        dto.setRecommendation(result.getRecommendation() != null ? result.getRecommendation() : "Рекомендация будет доступна после обработки");
        dto.setCareerPath(result.getCareerPath() != null ? result.getCareerPath() : "В процессе определения");

        String detailedAnalysis = String.format(
                "Осознанность: %d/20 | Стабильность: %d/20 | Мотивация: %d/30 | Аналитика: %d/30",
                dto.getAwarenessScore(),
                dto.getStabilityScore(),
                dto.getMotivationScore(),
                dto.getAnalyticalScore()
        );
        dto.setDetailedAnalysis(detailedAnalysis);

        return dto;
    }
}