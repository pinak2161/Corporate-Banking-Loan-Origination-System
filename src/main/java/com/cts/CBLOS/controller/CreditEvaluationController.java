// src/main/java/com/cts/CBLOS/controller/CreditEvaluationController.java
package com.cts.CBLOS.controller;

import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.service.CreditEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/credit-evaluations") // Base path for credit evaluation endpoints
public class CreditEvaluationController {

    @Autowired
    private CreditEvaluationService creditEvaluationService;

    // Endpoint to trigger a credit evaluation for a specific loan application
    // This would typically be called by an admin or an automated system
    @PostMapping("/evaluate/{applicationId}")
    public ResponseEntity<CreditEvaluation> evaluateCredit(@PathVariable Integer applicationId) {
        try {
            CreditEvaluation evaluation = creditEvaluationService.evaluateCredit(applicationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluation);
        } catch (RuntimeException e) {
            // Catch specific exceptions (e.g., LoanApplicationNotFoundException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or ErrorResponse DTO
        }
    }

    // Endpoint to retrieve a credit evaluation by loan application ID
    @GetMapping("/{applicationId}")
    public ResponseEntity<CreditEvaluation> getCreditEvaluationByApplicationId(@PathVariable Integer applicationId) {
        Optional<CreditEvaluation> evaluation = creditEvaluationService.getCreditEvaluationByApplicationId(applicationId);
        return evaluation.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
}