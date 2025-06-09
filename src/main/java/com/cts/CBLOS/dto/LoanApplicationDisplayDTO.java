package com.cts.CBLOS.dto;

import java.time.LocalDate;

public class LoanApplicationDisplayDTO {
    private Integer applicationId;
    private String companyName; // ADDED: Field for Company Name
    private String loanType;    // ADDED: Field for Loan Type
    private Double loanAmount;
    private String status;
    private LocalDate submissionDate;
    private String userName;

    // UPDATED: Constructor to include companyName and loanType
    public LoanApplicationDisplayDTO(Integer applicationId, String companyName, String loanType, Double loanAmount, String status, LocalDate submissionDate, String userName) {
        this.applicationId = applicationId;
        this.companyName = companyName; // Assign companyName
        this.loanType = loanType;       // Assign loanType
        this.loanAmount = loanAmount;
        this.status = status;
        this.submissionDate = submissionDate;
        this.userName = userName;
    }

    // Getters for all fields, including the newly added ones
    public Integer getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; } // ADDED: Getter for companyName
    public String getLoanType() { return loanType; }       // ADDED: Getter for loanType
    public Double getLoanAmount() { return loanAmount; }
    public String getStatus() { return status; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public String getUserName() { return userName; }

    // You might also add setters if this DTO is used for incoming data,
    // but for display purposes, getters are sufficient.
}