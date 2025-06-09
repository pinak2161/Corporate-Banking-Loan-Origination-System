//package com.cts.CBLOS.model;
//
//import jakarta.persistence.*;
//
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Entity
//@Data
//public class Approval {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer approvalId;
//
//    @ManyToOne
//    @JoinColumn(name = "applicationId", nullable = false)
//    private LoanApplication loanApplication;
//    // private Integer applicationId;
//    private Integer approverId;
//    private Integer approvalLevel;
//
//    @Enumerated(EnumType.STRING)
//    private ApprovalStatus approvalStatus;
//
//    @Column(columnDefinition = "TEXT")
//    private String comments;
//
//    private LocalDate approvalDate;
//
//
//    public enum ApprovalStatus {
//        Pending,
//        Approved,
//        Rejected
//    }
//}

package com.cts.CBLOS.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects; // Import Objects for equals and hashCode

@Entity
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer approvalId;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private LoanApplication loanApplication;
    private Integer approverId;
    private Integer approvalLevel;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(columnDefinition = "TEXT")
    private String comments;

    private LocalDate approvalDate;

    public enum ApprovalStatus {
        Pending,
        Approved,
        Rejected
    }

    // --- Getters ---
    public Integer getApprovalId() {
        return approvalId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public Integer getApprovalLevel() {
        return approvalLevel;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public String getComments() {
        return comments;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    // --- Setters ---
    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public void setApprovalLevel(Integer approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    // --- equals(), hashCode(), toString() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Approval approval = (Approval) o;
        return Objects.equals(approvalId, approval.approvalId) &&
                Objects.equals(approverId, approval.approverId) &&
                Objects.equals(approvalLevel, approval.approvalLevel) &&
                approvalStatus == approval.approvalStatus &&
                Objects.equals(comments, approval.comments) &&
                Objects.equals(approvalDate, approval.approvalDate);
        // LoanApplication is typically excluded from equals/hashCode to prevent circular dependencies
        // and potential stack overflows. If its equality is crucial, compare by ID.
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvalId, approverId, approvalLevel, approvalStatus, comments, approvalDate);
    }

    @Override
    public String toString() {
        return "Approval{" +
                "approvalId=" + approvalId +
                ", approverId=" + approverId +
                ", approvalLevel=" + approvalLevel +
                ", approvalStatus=" + approvalStatus +
                ", comments='" + comments + '\'' +
                ", approvalDate=" + approvalDate +
                ", loanApplicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
                '}';
    }
}