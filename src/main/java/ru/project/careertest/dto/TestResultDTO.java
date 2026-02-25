package ru.project.careertest.dto;

public class TestResultDTO {
    private Integer awarenessScore;
    private Integer stabilityScore;
    private Integer motivationScore;
    private Integer analyticalScore;
    private String recommendation;
    private String careerPath;
    private String detailedAnalysis;

    // Геттеры и сеттеры
    public Integer getAwarenessScore() {
        return awarenessScore != null ? awarenessScore : 0;
    }
    public void setAwarenessScore(Integer awarenessScore) {
        this.awarenessScore = awarenessScore;
    }

    public Integer getStabilityScore() {
        return stabilityScore != null ? stabilityScore : 0;
    }
    public void setStabilityScore(Integer stabilityScore) {
        this.stabilityScore = stabilityScore;
    }

    public Integer getMotivationScore() {
        return motivationScore != null ? motivationScore : 0;
    }
    public void setMotivationScore(Integer motivationScore) {
        this.motivationScore = motivationScore;
    }

    public Integer getAnalyticalScore() {
        return analyticalScore != null ? analyticalScore : 0;
    }
    public void setAnalyticalScore(Integer analyticalScore) {
        this.analyticalScore = analyticalScore;
    }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getCareerPath() { return careerPath; }
    public void setCareerPath(String careerPath) { this.careerPath = careerPath; }

    public String getDetailedAnalysis() { return detailedAnalysis; }
    public void setDetailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; }
}
