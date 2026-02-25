package ru.project.careertest.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String answers;

    @Column(name = "awareness_score")
    private Integer awarenessScore;

    @Column(name = "stability_score")
    private Integer stabilityScore;

    @Column(name = "motivation_score")
    private Integer motivationScore;

    @Column(name = "analytical_score")
    private Integer analyticalScore;

    private String recommendation;

    @Column(name = "career_path")
    private String careerPath;

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAnswers() { return answers; }
    public void setAnswers(String answers) { this.answers = answers; }

    public Integer getAwarenessScore() { return awarenessScore; }
    public void setAwarenessScore(Integer awarenessScore) { this.awarenessScore = awarenessScore; }

    public Integer getStabilityScore() { return stabilityScore; }
    public void setStabilityScore(Integer stabilityScore) { this.stabilityScore = stabilityScore; }

    public Integer getMotivationScore() { return motivationScore; }
    public void setMotivationScore(Integer motivationScore) { this.motivationScore = motivationScore; }

    public Integer getAnalyticalScore() { return analyticalScore; }
    public void setAnalyticalScore(Integer analyticalScore) { this.analyticalScore = analyticalScore; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getCareerPath() { return careerPath; }
    public void setCareerPath(String careerPath) { this.careerPath = careerPath; }
}
