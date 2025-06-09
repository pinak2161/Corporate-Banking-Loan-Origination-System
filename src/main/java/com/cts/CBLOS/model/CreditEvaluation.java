// src/main/java/com/cts/CBLOS/model/CreditEvaluation.java
package com.cts.CBLOS.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_evaluation") // Assuming table name
public class CreditEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evaluationId;

    @OneToOne // Each LoanApplication has one CreditEvaluation
    @JoinColumn(name = "applicationId", referencedColumnName = "applicationId", nullable = false, unique = true)
    private LoanApplication loanApplication;

    private Integer creditScore; // e.g., FICO score or internal score
    private String riskLevel;    // e.g., "LOW", "MEDIUM", "HIGH"
    private String evaluationComments; // Any comments from the evaluation
    private LocalDate evaluationDate;

    // Constructors
    public CreditEvaluation() {
        this.evaluationDate = LocalDate.now();
    }

    public CreditEvaluation(LoanApplication loanApplication, Integer creditScore, String riskLevel, String evaluationComments) {
        this.loanApplication = loanApplication;
        this.creditScore = creditScore;
        this.riskLevel = riskLevel;
        this.evaluationComments = evaluationComments;
        this.evaluationDate = LocalDate.now();
    }

    // Getters and Setters
    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getEvaluationComments() {
        return evaluationComments;
    }

    public void setEvaluationComments(String evaluationComments) {
        this.evaluationComments = evaluationComments;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    @Override
    public String toString() {
        return "CreditEvaluation{" +
                "evaluationId=" + evaluationId +
                ", loanApplicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
                ", creditScore=" + creditScore +
                ", riskLevel='" + riskLevel + '\'' +
                ", evaluationComments='" + evaluationComments + '\'' +
                ", evaluationDate=" + evaluationDate +
                '}';
    }
}