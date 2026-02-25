package ru.project.careertest.controller;

import ru.project.careertest.dto.TestAnswersDTO;
import ru.project.careertest.dto.TestResultDTO;
import ru.project.careertest.model.User;
import ru.project.careertest.model.UserType;
import ru.project.careertest.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private TestService testService;

    @PostMapping("/start-test")
    public ResponseEntity<?> startTest(@RequestParam String userType,
                                       @RequestParam String sessionId) {
        logger.info("=== START TEST REQUEST ===");
        logger.info("userType: {}, sessionId: {}", userType, sessionId);

        try {
            String upperCaseUserType = userType.toUpperCase();
            UserType type = UserType.valueOf(upperCaseUserType);

            User user = testService.createUser(sessionId, type);

            Map<String, String> response = new HashMap<>();
            response.put("userId", user.getId().toString());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid user type: {}", userType, e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user type: " + userType));
        } catch (Exception e) {
            logger.error("Error starting test", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/submit-test/{userId}")
    public ResponseEntity<?> submitTest(@PathVariable UUID userId,
                                        @RequestBody Map<String, Object> payload) {
        logger.info("=== SUBMIT TEST REQUEST ===");
        logger.info("userId: {}", userId);
        logger.info("Payload: {}", payload);

        try {
            TestAnswersDTO answers = new TestAnswersDTO();

            // Устанавливаем тип пользователя
            if (payload.containsKey("userType")) {
                String userType = (String) payload.get("userType");
                answers.setUserType(userType.toUpperCase());
            }

            // Безопасное преобразование для q1
            if (payload.containsKey("q1") && payload.get("q1") != null) {
                Object value = payload.get("q1");
                if (value instanceof Number) {
                    answers.setQ1(((Number) value).intValue());
                } else if (value instanceof String) {
                    try {
                        answers.setQ1(Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("q1 is not a number: {}, setting to 0", value);
                        answers.setQ1(0);
                    }
                }
            }

            // Безопасное преобразование для q2
            if (payload.containsKey("q2") && payload.get("q2") != null) {
                Object value = payload.get("q2");
                if (value instanceof Number) {
                    answers.setQ2(((Number) value).intValue());
                } else if (value instanceof String) {
                    try {
                        answers.setQ2(Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("q2 is not a number: {}, setting to 0", value);
                        answers.setQ2(0);
                    }
                }
            }

            // Безопасное преобразование для q3
            if (payload.containsKey("q3") && payload.get("q3") != null) {
                Object value = payload.get("q3");
                if (value instanceof Number) {
                    answers.setQ3(((Number) value).intValue());
                } else if (value instanceof String) {
                    try {
                        answers.setQ3(Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("q3 is not a number: {}, setting to 0", value);
                        answers.setQ3(0);
                    }
                }
            }

            // Безопасное преобразование для q4
            if (payload.containsKey("q4") && payload.get("q4") != null) {
                Object value = payload.get("q4");
                if (value instanceof Number) {
                    answers.setQ4(((Number) value).intValue());
                } else if (value instanceof String) {
                    try {
                        answers.setQ4(Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("q4 is not a number: {}, setting to 0", value);
                        answers.setQ4(0);
                    }
                }
            }

            // Для q5 - это текстовое поле, оставляем как строку
            if (payload.containsKey("q5")) {
                Object value = payload.get("q5");
                if (value != null) {
                    answers.setQ5(value.toString());
                    logger.info("q5 text: {}", value.toString());
                }
            }

            // Безопасное преобразование для q6
            if (payload.containsKey("q6") && payload.get("q6") != null) {
                Object value = payload.get("q6");
                if (value instanceof Number) {
                    answers.setQ6(((Number) value).intValue());
                } else if (value instanceof String) {
                    try {
                        answers.setQ6(Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("q6 is not a number: {}, setting to 0", value);
                        answers.setQ6(0);
                    }
                }
            }

            logger.info("TestAnswersDTO created: {}", answers);

            TestResultDTO result = testService.processTestResults(userId, answers);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error submitting test", e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}