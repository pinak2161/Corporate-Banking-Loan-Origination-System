// src/main/java/com/cts/CBLOS/service/ApprovalServiceImpl.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus;
import com.cts.CBLOS.model.User;
import com.cts.CBLOS.repository.ApprovalRepository;
import com.cts.CBLOS.repository.LoanApplicationRepository;
import com.cts.CBLOS.repository.UserRepository;
// Assuming you have a CreditEvaluation model and service
import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.service.CreditEvaluationService;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cts.CBLOS.model.Approval;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRepository approvalRepository;
    
    @Autowired
    private CreditEvaluationService creditEvaluationService; // For fetching risk level/credit score

   
    @Override
    @Transactional
    public LoanApplication approveLoan(Integer applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan application not found with ID: " + applicationId));

        // Get the currently authenticated admin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentAdmin = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Admin user not found."));

        // Check for sufficient approval level (if you want to keep this business rule)
        if (currentAdmin.getMaxApprovalLevel() < 2) { // Assuming Level 2 is required for final loan approval
            throw new RuntimeException("Admin approval level is insufficient to approve loans.");
        }

        // Check if the loan is already in a final state (approved/rejected)
        if (loanApplication.getStatus() == LoanApplicationStatus.LOAN_APPROVED ||
            loanApplication.getStatus() == LoanApplicationStatus.LOAN_REJECTED) {
            throw new RuntimeException("Loan application is already " + loanApplication.getStatus() + " and cannot be approved again.");
        }

        // --- REMOVED THE CHECK FOR DOCUMENTS_APPROVED STATUS HERE ---
        // loanApplication.setStatus(LoanApplicationStatus.DOCUMENTS_APPROVED) is no longer a prerequisite for approval here.

        // Fetch CreditEvaluation to log its details (if applicable)
        Optional<CreditEvaluation> creditEvaluationOpt = creditEvaluationService.getCreditEvaluationByApplicationId(applicationId);
        String riskLevel = creditEvaluationOpt.map(CreditEvaluation::getRiskLevel).orElse("N/A");

        // Update loan application status to LOAN_APPROVED
        loanApplication.setStatus(LoanApplicationStatus.LOAN_APPROVED);

        // --- NEW CODE TO UPDATE APPROVAL TABLE ---
        Approval approvalEntry = new Approval();
        approvalEntry.setLoanApplication(loanApplication);
        approvalEntry.setApproverId(currentAdmin.getUserId());
        approvalEntry.setApprovalLevel(currentAdmin.getMaxApprovalLevel());
        approvalEntry.setApprovalStatus(Approval.ApprovalStatus.Approved);
        approvalEntry.setComments("Loan approved by admin. Risk Level: " + riskLevel);
        approvalEntry.setApprovalDate(LocalDate.now());

        approvalRepository.save(approvalEntry);
        // --- END NEW CODE ---

        return loanApplicationRepository.save(loanApplication);
    }


    @Override
    @Transactional
    public LoanApplication rejectLoan(Integer applicationId, String rejectionReason) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAdminEmail = authentication.getName();
        User currentAdmin = userRepository.findByEmail(currentAdminEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated admin not found."));

        // Check if the admin has sufficient approval level for final loan approval (Level 2)
        if (currentAdmin.getMaxApprovalLevel() < 2) {
            throw new RuntimeException("Admin does not have the required approval level (Level 2) to reject loans.");
        }

        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan Application not found with ID: " + applicationId));

        // You might still allow rejection even if documents aren't approved,
        // but adding a check for current status can prevent rejecting already approved loans by mistake.
        if (loanApplication.getStatus() == LoanApplicationStatus.LOAN_APPROVED ||
            loanApplication.getStatus() == LoanApplicationStatus.LOAN_REJECTED) {
            throw new RuntimeException("Loan application is already " + loanApplication.getStatus() + " and cannot be rejected again.");
        }

        loanApplication.setStatus(LoanApplicationStatus.LOAN_REJECTED);
        // You might want to add a field to LoanApplication for rejectionReason
        // loanApplication.setRejectionReason(rejectionReason);
        loanApplication.setStatus(LoanApplicationStatus.LOAN_REJECTED);

        // --- NEW CODE TO UPDATE APPROVAL TABLE ---
        Approval approvalEntry = new Approval();
        approvalEntry.setLoanApplication(loanApplication); // Link to the rejected loan application
        approvalEntry.setApproverId(currentAdmin.getUserId()); // Assuming User entity has getUserId()
        approvalEntry.setApprovalLevel(currentAdmin.getMaxApprovalLevel());
        approvalEntry.setApprovalStatus(Approval.ApprovalStatus.Rejected);
        approvalEntry.setComments(rejectionReason); // Use the provided rejection reason
        approvalEntry.setApprovalDate(LocalDate.now()); // Set the current date

        approvalRepository.save(approvalEntry); // Save the approval log entry
        // --- END NEW CODE ---

        return loanApplicationRepository.save(loanApplication); // Save the updated LoanApplication status
    }
}