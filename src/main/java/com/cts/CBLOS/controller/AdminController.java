package com.cts.CBLOS.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

import com.cts.CBLOS.model.Disbursement;
import com.cts.CBLOS.model.Document;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus;
import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.repository.DocumentRepository;
import com.cts.CBLOS.service.DisbursementService;
import com.cts.CBLOS.service.DocumentService;
import com.cts.CBLOS.service.LoanApplicationService;
import com.cts.CBLOS.service.CreditEvaluationService;
import com.cts.CBLOS.service.ApprovalService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private DisbursementService disbursementService;

    @Autowired
    private CreditEvaluationService creditEvaluationService;

    @Autowired
    private ApprovalService approvalService;

    // --- DOC ADMIN DASHBOARD ---
    @GetMapping("/docadmin_dash")
    @PreAuthorize("hasRole('DOC_ADMIN')")
    public String getDocAdminDashboard(Model model) {
        List<Document> allDocuments = documentService.getAllDocumentsForAdmin();
        model.addAttribute("file", allDocuments);
        model.addAttribute("pendingDocumentsCount", documentService.countByIsValidIsNull());
        model.addAttribute("totalDocumentsCount", documentService.getAllDocumentsForAdmin().size());
        return "admin/docadmin_dash";
    }

    // Existing Document Approve/Reject for DOC_ADMIN (redirects to docadmin_dash)
    @PostMapping("/documents/{documentId}/approve")
    public String approveDocument(@PathVariable Integer documentId, RedirectAttributes redirectAttributes) {
        try {
            documentService.validateDocument(documentId, true);
            redirectAttributes.addFlashAttribute("successMessage", "Document " + documentId + " approved successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving document " + documentId + ": " + e.getMessage());
        }
        return "redirect:/admin/docadmin_dash";
    }

    @PostMapping("/documents/{documentId}/reject")
    public String rejectDocument(@PathVariable Integer documentId, RedirectAttributes redirectAttributes) {
        try {
            documentService.validateDocument(documentId, false);
            redirectAttributes.addFlashAttribute("successMessage", "Document " + documentId + " rejected successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting document " + documentId + ": " + e.getMessage());
        }
        return "redirect:/admin/docadmin_dash";
    }

    // NEW: Endpoint to approve a Loan Application from the main ADMIN dashboard
    @PostMapping("/loan-applications/{applicationId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveLoanApplicationFromAdminDash(@PathVariable Integer applicationId, RedirectAttributes redirectAttributes) {
        try {
            approvalService.approveLoan(applicationId);
            redirectAttributes.addFlashAttribute("successMessage", "Loan Application " + applicationId + " approved successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving Loan Application " + applicationId + ": " + e.getMessage());
        }
        return "redirect:/admin"; // Redirect back to the main admin dashboard
    }

    // NEW: Endpoint to reject a Loan Application from the main ADMIN dashboard
    @PostMapping("/loan-applications/{applicationId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectLoanApplicationFromAdminDash(@PathVariable Integer applicationId,
                                                    @RequestParam(required = false) String rejectionReason,
                                                    RedirectAttributes redirectAttributes) {
        try {
            String reason = (rejectionReason != null && !rejectionReason.trim().isEmpty()) ? rejectionReason : "No reason provided.";
            approvalService.rejectLoan(applicationId, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Loan Application " + applicationId + " rejected successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting Loan Application " + applicationId + ": " + e.getMessage());
        }
        return "redirect:/admin"; // Redirect back to the main admin dashboard
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String superAdminDashboard(Model model) {
        model.addAttribute("totalloanCount", loanApplicationService.countTotalLoanApplications());
        model.addAttribute("pendingloanCount", loanApplicationService.countPendingLoanApplications());

        List<Document> allDocuments = documentService.getAllDocumentsForAdmin();

        Map<Integer, CreditEvaluation> creditEvaluationsMap = new HashMap<>();
        for (Document document : allDocuments) {
            if (document.getLoanApplication() != null && document.getLoanApplication().getApplicationId() != null) {
                Integer applicationId = document.getLoanApplication().getApplicationId();
                Optional<CreditEvaluation> evaluation = creditEvaluationService.getCreditEvaluationByApplicationId(applicationId);
                evaluation.ifPresent(ce -> creditEvaluationsMap.put(applicationId, ce));
            }
        }
        model.addAttribute("creditEvaluationsMap", creditEvaluationsMap);
        // --- ADD THESE LINES TO PASS ENUM VALUES TO THE MODEL ---
        model.addAttribute("LOAN_APPROVED", LoanApplicationStatus.LOAN_APPROVED);
        model.addAttribute("LOAN_REJECTED", LoanApplicationStatus.LOAN_REJECTED);
        model.addAttribute("APPLIED", LoanApplicationStatus.APPLIED);
        model.addAttribute("DOCUMENTS_PENDING", LoanApplicationStatus.DOCUMENTS_PENDING);
        model.addAttribute("DOCUMENTS_APPROVED", LoanApplicationStatus.DOCUMENTS_APPROVED);
        // --- END ADDITIONS --
        model.addAttribute("documents", allDocuments);
        model.addAttribute("totalDocumentsCount", documentService.getAllDocumentsForAdmin().size());
        model.addAttribute("pendingDocumentsCount", documentService.countByIsValidIsNull());
        return "admin/admin_dash";
    }

    @GetMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public String reviewAllDocuments(Model model) {
        List<Document> documents = documentService.getAllDocumentsForAdmin();
        model.addAttribute("documents", documents);
        return "documents/adminDoc";
    }

    @PostMapping("/validate/{documentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveOrRejectDocument(
            @PathVariable Integer documentId,
            @RequestParam Boolean isValid,
            @RequestParam(required = false) String comments,
            RedirectAttributes redirectAttributes) {

        System.out.println("Received isValid: " + isValid + " for Document ID: " + documentId);
        System.out.println("Comments: " + (comments != null ? comments : "No comments"));

        try {
            documentService.updateDocumentValidity(documentId, isValid, comments);
            redirectAttributes.addFlashAttribute("message", "Document " + (isValid ? "approved" : "rejected") + " successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error validating document: " + e.getMessage());
        }
        return "redirect:/admin/review";
    }

    @PostMapping("/scheduleDisbursement")
    @PreAuthorize("hasRole('ADMIN')")
    public String scheduleDisbursement(@ModelAttribute Disbursement disbursement, Model model, RedirectAttributes redirectAttributes) {
        disbursementService.scheduleDisbursement(disbursement);
        redirectAttributes.addFlashAttribute("message", "Disbursement scheduled successfully!");

        List<Disbursement> disbursements = disbursementService.getAllDisbursements();
        model.addAttribute("disbursements", disbursements);
        return "approval/adminReport";
    }

    @GetMapping("/scheduleDisbursement")
    @PreAuthorize("hasRole('ADMIN')")
    public String showDisbursementForm(Model model) {
        model.addAttribute("disbursement", new Disbursement());
        return "approval/scheduleDisbursement";
    }

    @GetMapping("/disbursementReport")
    @PreAuthorize("hasRole('ADMIN')")
    public String generateDisbursementReport(Model model) {
        List<Disbursement> disbursements = disbursementService.getAllDisbursements();
        model.addAttribute("disbursements", disbursements);
        return "approval/adminReport";
    }

    @GetMapping("/view/{documentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> viewDocumentForAdmin(@PathVariable Integer documentId) {
        try {
            Optional<Document> documentOpt = documentRepository.findById(documentId);

            if (documentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Document document = documentOpt.get();
            ByteArrayResource resource = new ByteArrayResource(document.getData());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFileName() + "\"");
            headers.setContentType(MediaType.parseMediaType(document.getContentType()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(document.getData().length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}