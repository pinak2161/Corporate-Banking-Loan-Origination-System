//package com.cts.CBLOS.model;
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Entity
//@Data
//@Getter
//@Setter
//@Table(name="disbursement")
//public class Disbursement {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer disbursementId;
//
//    @ManyToOne
//    @JoinColumn(name = "applicationId", nullable = false)
//    private LoanApplication loanApplication;
//
//    private BigDecimal disbursedAmount;
//    private LocalDate disbursementDate;
//    private String repaymentSchedule;
//}


package com.cts.CBLOS.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects; // Import Objects for equals and hashCode

@Entity
@Table(name="disbursement")
public class Disbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer disbursementId;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private LoanApplication loanApplication;

    private BigDecimal disbursedAmount;
    private LocalDate disbursementDate;
    private String repaymentSchedule;

    // --- Getters ---
    public Integer getDisbursementId() {
        return disbursementId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public String getRepaymentSchedule() {
        return repaymentSchedule;
    }

    // --- Setters ---
    public void setDisbursementId(Integer disbursementId) {
        this.disbursementId = disbursementId;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public void setRepaymentSchedule(String repaymentSchedule) {
        this.repaymentSchedule = repaymentSchedule;
    }

    // --- equals(), hashCode(), toString() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disbursement that = (Disbursement) o;
        return Objects.equals(disbursementId, that.disbursementId) &&
                Objects.equals(disbursedAmount, that.disbursedAmount) &&
                Objects.equals(disbursementDate, that.disbursementDate) &&
                Objects.equals(repaymentSchedule, that.repaymentSchedule);
        // LoanApplication is typically excluded from equals/hashCode to prevent circular dependencies
        // and potential stack overflows. If its equality is crucial, compare by ID.
    }

    @Override
    public int hashCode() {
        return Objects.hash(disbursementId, disbursedAmount, disbursementDate, repaymentSchedule);
    }

    @Override
    public String toString() {
        return "Disbursement{" +
                "disbursementId=" + disbursementId +
                ", disbursedAmount=" + disbursedAmount +
                ", disbursementDate=" + disbursementDate +
                ", repaymentSchedule='" + repaymentSchedule + '\'' +
                ", loanApplicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
                '}';
    }
}