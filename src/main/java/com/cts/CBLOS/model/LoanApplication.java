// src/main/java/com/cts/CBLOS/model/LoanApplication.java
package com.cts.CBLOS.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicationId;
	private String companyName;
	private String loanType;
	private Double loanAmount;
	@Enumerated(EnumType.STRING)
	private LoanApplicationStatus status;
	private LocalDate submissionDate;

	// Parameters as requested for credit evaluation
	private Integer NewCreditAccounts;
	private Double MonthlyIncome;
	private Double MonthlyLoanRepaymentAmount;
	private Integer NumberOfPastDefaults;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
	private User user;

	// --- CORRECTED LoanApplicationStatus Enum for 2-level approval ---
	public enum LoanApplicationStatus {
		APPLIED,                // Initial status
		DOCUMENTS_PENDING,      // Documents submitted, waiting DocAdmin review
		DOCUMENTS_APPROVED,     // All documents validated, ready for Loan Admin review
		LOAN_APPROVED,          // Final loan approval
		LOAN_REJECTED           // Final loan rejection
	}

	// Constructors
	public LoanApplication() {
		this.submissionDate = LocalDate.now();
		this.status = LoanApplicationStatus.APPLIED; // Default initial status
	}

	public LoanApplication(String companyName, String loanType, Double loanAmount,
						   Integer newCreditAccounts, Double monthlyIncome,
						   Double monthlyLoanRepaymentAmount, Integer numberOfPastDefaults,
						   User user) {
		this.companyName = companyName;
		this.loanType = loanType;
		this.loanAmount = loanAmount;
		this.NewCreditAccounts = newCreditAccounts;
		this.MonthlyIncome = monthlyIncome;
		this.MonthlyLoanRepaymentAmount = monthlyLoanRepaymentAmount;
		this.NumberOfPastDefaults = numberOfPastDefaults;
		this.user = user;
		this.submissionDate = LocalDate.now();
		this.status = LoanApplicationStatus.APPLIED; // Initial status
	}

	// --- Getters ---
	public Integer getApplicationId() {
		return applicationId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getLoanType() {
		return loanType;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public LoanApplicationStatus getStatus() {
		return status;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public Integer getNewCreditAccounts() {
		return NewCreditAccounts;
	}

	public Double getMonthlyIncome() {
		return MonthlyIncome;
	}

	public Double getMonthlyLoanRepaymentAmount() {
		return MonthlyLoanRepaymentAmount;
	}

	public Integer getNumberOfPastDefaults() {
		return NumberOfPastDefaults;
	}

	public User getUser() {
		return user;
	}

	// --- Setters ---
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public void setStatus(LoanApplicationStatus status) {
		this.status = status;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public void setNewCreditAccounts(Integer newCreditAccounts) {
		NewCreditAccounts = newCreditAccounts;
	}

	public void setMonthlyIncome(Double monthlyIncome) {
		MonthlyIncome = monthlyIncome;
	}

	public void setMonthlyLoanRepaymentAmount(Double monthlyLoanRepaymentAmount) {
		this.MonthlyLoanRepaymentAmount = monthlyLoanRepaymentAmount;
	}

	public void setNumberOfPastDefaults(Integer numberOfPastDefaults) {
		NumberOfPastDefaults = numberOfPastDefaults;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// --- equals(), hashCode(), toString() ---
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoanApplication that = (LoanApplication) o;
		return Objects.equals(applicationId, that.applicationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(applicationId);
	}

	@Override
	public String toString() {
		return "LoanApplication{" +
				"applicationId=" + applicationId +
				", companyName='" + companyName + '\'' +
				", loanType='" + loanType + '\'' +
				", loanAmount=" + loanAmount +
				", status=" + status +
				", submissionDate=" + submissionDate +
				", NewCreditAccounts=" + NewCreditAccounts +
				", MonthlyIncome=" + MonthlyIncome +
				", MonthlyLoanRepaymentAmount=" + MonthlyLoanRepaymentAmount +
				", NumberOfPastDefaults=" + NumberOfPastDefaults +
				'}';
	}
}