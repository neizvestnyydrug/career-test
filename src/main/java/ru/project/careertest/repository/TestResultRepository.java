package ru.project.careertest.repository;

import ru.project.careertest.model.TestResult;
import ru.project.careertest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, UUID> {
    Optional<TestResult> findByUser(User user);
}
