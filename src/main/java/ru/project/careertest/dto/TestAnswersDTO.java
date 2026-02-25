package ru.project.careertest.dto;

public class TestAnswersDTO {
    private String userType;
    private Integer q1;
    private Integer q2;
    private Integer q3;
    private Integer q4;
    private Object q5;  // Может быть String или Integer
    private Integer q6;

    // Геттеры и сеттеры
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Integer getQ1() { return q1; }
    public void setQ1(Integer q1) { this.q1 = q1; }

    public Integer getQ2() { return q2; }
    public void setQ2(Integer q2) { this.q2 = q2; }

    public Integer getQ3() { return q3; }
    public void setQ3(Integer q3) { this.q3 = q3; }

    public Integer getQ4() { return q4; }
    public void setQ4(Integer q4) { this.q4 = q4; }

    public Object getQ5() { return q5; }
    public void setQ5(Object q5) { this.q5 = q5; }

    public String getQ5AsString() {
        return q5 != null ? q5.toString() : null;
    }

    public Integer getQ6() { return q6; }
    public void setQ6(Integer q6) { this.q6 = q6; }

    @Override
    public String toString() {
        return String.format("TestAnswersDTO{userType='%s', q1=%d, q2=%d, q3=%d, q4=%d, q5='%s', q6=%d}",
                userType, q1, q2, q3, q4, q5, q6);
    }
}