// src/main/java/com/cts/CBLOS/service/LoanApplicationService.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.LoanApplication;
import java.util.List;
import java.util.Optional;

public interface LoanApplicationService {

    LoanApplication applyForLoan(LoanApplication loanApplication);

    Optional<LoanApplication> getApplicationById(Integer applicationId);

    List<LoanApplication> getAllApplications();
    long countPendingLoanApplications();
    List<LoanApplication> getApplicationsByUserId(Integer userId);
    long countTotalLoanApplications();
    // You can add more methods here as needed, e.g., for updating application
    // status by other services, or for administrative views.
    // Example:
    // LoanApplication updateApplicationStatus(Integer applicationId, LoanApplication.LoanApplicationStatus newStatus);
}