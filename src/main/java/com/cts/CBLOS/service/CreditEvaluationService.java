// src/main/java/com/cts/CBLOS/service/CreditEvaluationService.java
package com.cts.CBLOS.service;

import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.model.LoanApplication;

import java.util.Optional;

public interface CreditEvaluationService {

    // Method that the ApprovalServiceImpl needs
    Optional<CreditEvaluation> getCreditEvaluationByApplicationId(Integer applicationId);

    // Core method to trigger a credit evaluation for a loan application
    CreditEvaluation evaluateCredit(Integer applicationId);

    // You might also add methods to update an existing evaluation
    // CreditEvaluation updateCreditEvaluation(Integer evaluationId, CreditEvaluation updatedEvaluation);
}