// src/main/java/com/cts/CBLOS/controller/ApprovalController.java
package com.cts.CBLOS.controller;

import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/approvals") // Base path for approval related endpoints
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    // Endpoint to approve a loan application
    // Only accessible by admins with sufficient approval level (handled in service)
    @PostMapping("/approve/{applicationId}")
    public ResponseEntity<LoanApplication> approveLoan(@PathVariable Integer applicationId) {
        try {
            LoanApplication approvedLoan = approvalService.approveLoan(applicationId);
            return ResponseEntity.ok(approvedLoan);
        } catch (RuntimeException e) {
            // Catch specific exceptions for better error handling (e.g., ApplicationNotFoundException, InsufficientApprovalLevelException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or return an ErrorResponse DTO
        }
    }

    // DTO for rejection request to cleanly encapsulate data
    static class RejectLoanRequest {
        public String rejectionReason;
    }

    // Endpoint to reject a loan application
    // Only accessible by admins with sufficient approval level (handled in service)
    @PostMapping("/reject/{applicationId}")
    public ResponseEntity<LoanApplication> rejectLoan(@PathVariable Integer applicationId,
                                                    @RequestBody RejectLoanRequest request) {
        try {
            if (request.rejectionReason == null || request.rejectionReason.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Rejection reason is mandatory
            }
            LoanApplication rejectedLoan = approvalService.rejectLoan(applicationId, request.rejectionReason);
            return ResponseEntity.ok(rejectedLoan);
        } catch (RuntimeException e) {
            // Catch specific exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or return an ErrorResponse DTO
        }
    }
}