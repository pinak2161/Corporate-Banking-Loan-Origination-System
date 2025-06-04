//package com.example.demo.model;
//
//
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Objects;
//
//@Entity
//@Table(name = "loan_application")
//public class LoanApplication {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long applicationId;
//
//    @Column(nullable = false, length = 100)
//    private String companyName;
//
//    @Column(nullable = false, length = 50)
//    private String loanType;
//
//    @Column(nullable = false, precision = 18, scale = 2)
//    private BigDecimal loanAmount;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private ApplicationStatus status;
//
//    @Column(nullable = false)
//    private LocalDate submissionDate;
//
//    @Column(nullable = false)
//    private LocalDate lastUpdated;
//
//    // NEW FIELD: To link the application to the user who created it
//    @Column(nullable = false, length = 100)
//    private String userEmail; // Assuming user's email is their unique identifier for applications
//
//    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CreditEvaluation> creditEvaluations;
//
//    public enum ApplicationStatus {
//        Pending,
//        Approved,
//        Rejected,
//        IN_REVIEW,
//        PENDING_SUBMISSION
//    }
//
//    // --- Constructors ---
//    public LoanApplication() {
//        // Default constructor
//    }
//
//    // Updated constructor to include userEmail
//    public LoanApplication(String companyName, String loanType, BigDecimal loanAmount,
//                           ApplicationStatus status, LocalDate submissionDate, LocalDate lastUpdated, String userEmail) {
//        this.companyName = companyName;
//        this.loanType = loanType;
//        this.loanAmount = loanAmount;
//        this.status = status;
//        this.submissionDate = submissionDate;
//        this.lastUpdated = lastUpdated;
//        this.userEmail = userEmail; // Initialize new field
//    }
//
//    // --- Getters and Setters ---
//    public Long getApplicationId() {
//        return applicationId;
//    }
//
//    public void setApplicationId(Long applicationId) {
//        this.applicationId = applicationId;
//    }
//
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public void setCompanyName(String companyName) {
//        this.companyName = companyName;
//    }
//
//    public String getLoanType() {
//        return loanType;
//    }
//
//    public void setLoanType(String loanType) {
//        this.loanType = loanType;
//    }
//
//    public BigDecimal getLoanAmount() {
//        return loanAmount;
//    }
//
//    public void setLoanAmount(BigDecimal loanAmount) {
//        this.loanAmount = loanAmount;
//    }
//
//    public ApplicationStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(ApplicationStatus status) {
//        this.status = status;
//    }
//
//    public LocalDate getSubmissionDate() {
//        return submissionDate;
//    }
//
//    public void setSubmissionDate(LocalDate submissionDate) {
//        this.submissionDate = submissionDate;
//    }
//
//    public LocalDate getLastUpdated() {
//        return lastUpdated;
//    }
//
//    public void setLastUpdated(LocalDate lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }
//
//    // NEW Getter and Setter for userEmail
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public List<CreditEvaluation> getCreditEvaluations() {
//        return creditEvaluations;
//    }
//
//    public void setCreditEvaluations(List<CreditEvaluation> creditEvaluations) {
//        this.creditEvaluations = creditEvaluations;
//    }
//
//    // --- Lifecycle Callbacks ---
//    @PrePersist
//    protected void onCreate() {
//        if (this.submissionDate == null) {
//            this.submissionDate = LocalDate.now();
//        }
//        if (this.lastUpdated == null) {
//            this.lastUpdated = LocalDate.now();
//        }
//        if (this.status == null) {
//            this.status = ApplicationStatus.PENDING_SUBMISSION;
//        }
//        // userEmail should ideally be set by the service/controller before persisting
//        // but adding a fallback or validation here can be useful.
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.lastUpdated = LocalDate.now();
//    }
//
//    // --- equals, hashCode, toString (Good practice) ---
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        LoanApplication that = (LoanApplication) o;
//        return Objects.equals(applicationId, that.applicationId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(applicationId);
//    }
//
//    @Override
//    public String toString() {
//        return "LoanApplication{" +
//               "applicationId=" + applicationId +
//               ", companyName='" + companyName + '\'' +
//               ", loanType='" + loanType + '\'' +
//               ", loanAmount=" + loanAmount +
//               ", status=" + status +
//               ", submissionDate=" + submissionDate +
//               ", lastUpdated=" + lastUpdated +
//               ", userEmail='" + userEmail + '\'' +
//               '}';
//    }
//}

package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loanapplication")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", unique = true, nullable = false)
    private String applicationId;

    @Column(name = "legal_business_name", nullable = false)
    private String legalBusinessName;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "contact_person")
    private String contactPerson;

    // FIX: Make contact_email explicitly nullable in the database
    @Column(name = "user_email", nullable = true)
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "loan_amount")
    private Double loanAmount;

    @Column(name = "loan_purpose")
    private String loanPurpose;

    @Column(nullable = false)
    private String status;

    @Column(name = "submitted_date", nullable = false)
    private LocalDate submittedDate;

    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    // Constructors
    public LoanApplication() {
    }

    public LoanApplication(String applicationId, String legalBusinessName, String businessAddress,
                           String contactPerson, String contactEmail, String contactPhone,
                           String loanType, Double loanAmount, String loanPurpose,
                           String status, LocalDate submittedDate, LocalDate lastUpdated) {
        this.applicationId = applicationId;
        this.legalBusinessName = legalBusinessName;
        this.businessAddress = businessAddress;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
        this.status = status;
        this.submittedDate = submittedDate;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public String getLegalBusinessName() { return legalBusinessName; }
    public void setLegalBusinessName(String legalBusinessName) { this.legalBusinessName = legalBusinessName; }
    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; } // Changed to Double to match model
    public String getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(String loanPurpose) { this.loanPurpose = loanPurpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }
    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "id=" + id +
                ", applicationId='" + applicationId + '\'' +
                ", legalBusinessName='" + legalBusinessName + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", loanType='" + loanType + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanPurpose='" + loanPurpose + '\'' +
                ", status='" + status + '\'' +
                ", submittedDate=" + submittedDate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
