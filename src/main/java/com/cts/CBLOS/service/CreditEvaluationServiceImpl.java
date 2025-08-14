// src/main/java/com/cts/CBLOS/service/CreditEvaluationServiceImpl.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.repository.CreditEvaluationRepository;
import com.cts.CBLOS.repository.LoanApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random; // For dummy credit score/risk level generation

@Service
public class CreditEvaluationServiceImpl implements CreditEvaluationService {

    @Autowired
    private CreditEvaluationRepository creditEvaluationRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public Optional<CreditEvaluation> getCreditEvaluationByApplicationId(Integer applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan Application not found for ID: " + applicationId));
        return creditEvaluationRepository.findByLoanApplication(loanApplication);
    }

    @Override
    @Transactional
    public CreditEvaluation evaluateCredit(Integer applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan Application not found for ID: " + applicationId));

        // --- Dummy Credit Evaluation Logic (Replace with your actual module integration) ---
        Random random = new Random();
        int creditScore = 300 + random.nextInt(550); // Generates score between 300-850
        String riskLevel;
        String comments;

        if (creditScore >= 700) {
            riskLevel = "LOW";
            comments = "Excellent credit history.";
        } else if (creditScore >= 600) {
            riskLevel = "MEDIUM";
            comments = "Good credit history, some minor concerns.";
        } else {
            riskLevel = "HIGH";
            comments = "High risk, potential defaults identified.";
        }

        // Check if an evaluation already exists for this application
        Optional<CreditEvaluation> existingEvaluation = creditEvaluationRepository.findByLoanApplication(loanApplication);
        CreditEvaluation evaluation;
        if (existingEvaluation.isPresent()) {
            evaluation = existingEvaluation.get();
            evaluation.setCreditScore(creditScore);
            evaluation.setRiskLevel(riskLevel);
            evaluation.setEvaluationComments(comments);
            // evaluation.setEvaluationDate(LocalDate.now()); // Update date if needed
        } else {
            evaluation = new CreditEvaluation(loanApplication, creditScore, riskLevel, comments);
        }

        return creditEvaluationRepository.save(evaluation);
        // --- End of Dummy Logic ---
    }
}