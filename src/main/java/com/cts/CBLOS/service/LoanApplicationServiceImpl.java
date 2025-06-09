// src/main/java/com/cts/CBLOS/service/LoanApplicationServiceImpl.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus;
import com.cts.CBLOS.repository.LoanApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    @Transactional
    public LoanApplication applyForLoan(LoanApplication loanApplication) {
        if (loanApplication.getSubmissionDate() == null) {
            loanApplication.setSubmissionDate(LocalDate.now());
        }
        // --- CORRECTED: Use APPLIED status as per the updated enum ---
        loanApplication.setStatus(LoanApplicationStatus.APPLIED);
        return loanApplicationRepository.save(loanApplication);
    }
    @Override
    public long countPendingLoanApplications() {
        // Defines which statuses count as "pending approval" for loan applications
        return loanApplicationRepository.countByStatusIn(
            List.of(LoanApplicationStatus.APPLIED, LoanApplicationStatus.DOCUMENTS_PENDING)
        );
    }
    @Override
    public long countTotalLoanApplications() {
        return loanApplicationRepository.count();
    }

    
    @Override
    public Optional<LoanApplication> getApplicationById(Integer applicationId) {
        return loanApplicationRepository.findById(applicationId);
    }

    @Override
    public List<LoanApplication> getAllApplications() {
        return loanApplicationRepository.findAll();
    }

    @Override
    public List<LoanApplication> getApplicationsByUserId(Integer userId) {
        return loanApplicationRepository.findByUser_UserId(userId);
    }

    
}