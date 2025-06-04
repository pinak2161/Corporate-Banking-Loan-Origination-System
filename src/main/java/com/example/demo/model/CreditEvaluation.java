//package com.example.demo.model;
//
//
//
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Objects; // For equals and hashCode
//
//@Entity
//@Table(name = "credit_evaluation")
//public class CreditEvaluation {
//
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long evaluationId;
//
// @ManyToOne(fetch = FetchType.LAZY)
// @JoinColumn(name = "applicationId", nullable = false)
// private LoanApplication loanApplication; // Renamed from applicationId to loanApplication for relationship
//
// @Column(precision = 5, scale = 2)
// private BigDecimal riskScore;
//
// private Integer creditScore;
//
// @Column(nullable = false)
// private LocalDate evaluationDate;
//
// // --- Constructors ---
// public CreditEvaluation() {
//     // Default constructor
// }
//
// public CreditEvaluation(LoanApplication loanApplication, BigDecimal riskScore, Integer creditScore, LocalDate evaluationDate) {
//     this.loanApplication = loanApplication;
//     this.riskScore = riskScore;
//     this.creditScore = creditScore;
//     this.evaluationDate = evaluationDate;
// }
//
// // --- Getters and Setters ---
// public Long getEvaluationId() {
//     return evaluationId;
// }
//
// public void setEvaluationId(Long evaluationId) {
//     this.evaluationId = evaluationId;
// }
//
// public LoanApplication getLoanApplication() {
//     return loanApplication;
// }
//
// public void setLoanApplication(LoanApplication loanApplication) {
//     this.loanApplication = loanApplication;
// }
//
// public BigDecimal getRiskScore() {
//     return riskScore;
// }
//
// public void setRiskScore(BigDecimal riskScore) {
//     this.riskScore = riskScore;
// }
//
// public Integer getCreditScore() {
//     return creditScore;
// }
//
// public void setCreditScore(Integer creditScore) {
//     this.creditScore = creditScore;
// }
//
// public LocalDate getEvaluationDate() {
//     return evaluationDate;
// }
//
// public void setEvaluationDate(LocalDate evaluationDate) {
//     this.evaluationDate = evaluationDate;
// }
//
//
// @PrePersist
// protected void onCreate() {
//     if (this.evaluationDate == null) {
//         this.evaluationDate = LocalDate.now();
//     }
// }
//
// // --- equals, hashCode, toString (Good practice) ---
// @Override
// public boolean equals(Object o) {
//     if (this == o) return true;
//     if (o == null || getClass() != o.getClass()) return false;
//     CreditEvaluation that = (CreditEvaluation) o;
//     return Objects.equals(evaluationId, that.evaluationId);
// }
//
// @Override
// public int hashCode() {
//     return Objects.hash(evaluationId);
// }
//
// @Override
// public String toString() {
//     return "CreditEvaluation{" +
//            "evaluationId=" + evaluationId +
//            ", applicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
//            ", riskScore=" + riskScore +
//            ", creditScore=" + creditScore +
//            ", evaluationDate=" + evaluationDate +
//            '}';
// }
//}
