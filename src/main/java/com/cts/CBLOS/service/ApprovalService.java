// src/main/java/com/cts/CBLOS/service/ApprovalService.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.LoanApplication;

public interface ApprovalService {

    // Method to approve a loan application
    LoanApplication approveLoan(Integer applicationId);

    // Method to reject a loan application
    LoanApplication rejectLoan(Integer applicationId, String rejectionReason);

    // You might also want methods to retrieve applications awaiting approval for the admin dashboard
    // List<LoanApplication> getApplicationsAwaitingLoanApproval();
}