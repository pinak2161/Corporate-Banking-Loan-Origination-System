package com.cts.CBLOS.controller;

import com.cts.CBLOS.dto.LoanApplicationDisplayDTO;
import com.cts.CBLOS.model.Document;
import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.User;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus; // Ensure this import is correct
import com.cts.CBLOS.service.DocumentService;
import com.cts.CBLOS.service.LoanApplicationService;
import com.cts.CBLOS.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize; // CORRECT IMPORT
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    /**
     * Displays the document upload/view/edit page for the current user,
     * showing only their own documents.
     */
    @GetMapping("/my-documents")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String showMyDocuments(
            Model model,
            Authentication authentication,
            @RequestParam(required = false) Integer applicationId, // <-- Added optional applicationId parameter
            RedirectAttributes redirectAttributes) { // Added RedirectAttributes for potential redirects

        String currentUserEmail = authentication.getName();
        User currentUser = userService.findByEmail(currentUserEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found: " + currentUserEmail));

        List<Document> documents;

        if (applicationId != null) {
            // If an applicationId is provided, fetch documents for that specific application
            Optional<LoanApplication> loanApplicationOpt = loanApplicationService.getApplicationById(applicationId);

            if (loanApplicationOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Loan application not found.");
                return "redirect:/loan-applications/my-applications"; // Redirect if application not found
            }

            LoanApplication loanApplication = loanApplicationOpt.get();

            // Security check: Ensure user owns the application or is an ADMIN
            if (!loanApplication.getUser().getUserId().equals(currentUser.getUserId()) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to view documents for this application.");
                return "redirect:/loan-applications/my-applications"; // Redirect if not authorized
            }

            documents = documentService.getDocumentsByApplicationId(applicationId);
        } else {
            // If no applicationId is provided, fetch all documents for the current user
            documents = documentService.getDocumentsByUserId(currentUser.getUserId());
        }

        model.addAttribute("documents", documents);
        return "my-documents";
    }

    @GetMapping("/upload-form")
    @PreAuthorize("hasRole('USER')")
    public String showUploadFormForApplication(@RequestParam Integer applicationId, Model model, HttpSession session) {
        // Ensure the application exists and belongs to the current user (optional but recommended security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(authentication.getName())
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        Optional<LoanApplication> loanApplicationOpt = loanApplicationService.getApplicationById(applicationId);

        if (loanApplicationOpt.isEmpty() || !loanApplicationOpt.get().getUser().getUserId().equals(currentUser.getUserId())) {
            model.addAttribute("error", "Loan application not found or you are not authorized to upload documents for it.");
            return "error"; // Or redirect to a user's dashboard
        }

        // Store the application ID in session if you need it for subsequent uploads on the same page
        session.setAttribute("applicationId", applicationId);
        model.addAttribute("applicationId", applicationId); // Pass to the view
        return "documents/upload-form"; // This should be the name of your HTML template for document upload
    }


    /**
     * MODIFIED: POST endpoint to handle the actual file upload submission.
     * Renamed from "/upload" to "/do-upload" to clearly separate it from the GET form.
     */
    @PostMapping("/do-upload") // Changed the mapping here
    @PreAuthorize("hasRole('USER')")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            @RequestParam Integer applicationId, // Expect application ID from the form
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        // Validate that the applicationId provided in the form belongs to the current user
        Optional<LoanApplication> loanAppOpt = loanApplicationService.getApplicationById(applicationId);
        if (loanAppOpt.isEmpty() || !loanAppOpt.get().getUser().getUserId().equals(currentUser.getUserId())) {
             redirectAttributes.addFlashAttribute("error", "Invalid loan application ID or not authorized.");
             return "redirect:/loan-applications/my-applications"; // Or appropriate error page
        }


        try {
            documentService.uploadDocument(file, documentType, applicationId, currentUser.getUserId()); // Use applicationId directly
            redirectAttributes.addFlashAttribute("message", "Document uploaded successfully!");

            // After document upload, you might want to redirect back to the upload form for more documents,
            // or to a "my documents" page.
            // Option 1: Redirect back to the upload form for the same application
            return "redirect:/documents/upload-form?applicationId=" + applicationId;
            // Option 2: Redirect to the user's list of documents for this application
            // return "redirect:/documents/my-documents?applicationId=" + applicationId; // If my-documents can filter by app ID
            // Option 3: Redirect to a general "my documents" page (if it lists all of user's docs)
            // return "redirect:/documents/my-documents";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload document: " + e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // If there's an error, redirect back to the upload form with the error message
        return "redirect:/documents/upload-form?applicationId=" + applicationId;
    }

    /**
     * Downloads a document by its ID.
     */
    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Users can download their own, admins any
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id, Authentication authentication) {
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        try {
            Document document = documentService.getDocumentById(id);

            // Security check: Ensure user owns the document or is an ADMIN
            if (!document.getUserId().equals(currentUser.getUserId()) && !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ByteArrayResource resource = new ByteArrayResource(document.getData());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Displays a document (e.g., PDF) directly in the browser by its ID.
     */
    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Users can view their own, admins any
    public ResponseEntity<Resource> viewDocument(@PathVariable Integer id, Authentication authentication) {
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found."));
        try {
            Document document = documentService.getDocumentById(id);

            // Security check: Ensure user owns the document or is an ADMIN
            if (!document.getUserId().equals(currentUser.getUserId()) && !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ByteArrayResource resource = new ByteArrayResource(document.getData());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFileName() + "\"") // Use "inline" to display in browser
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a document by its ID.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')") // Only users can delete their own documents
    public String deleteDocument(@PathVariable Integer id, RedirectAttributes redirectAttributes, Authentication authentication) {
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found."));
        try {
            // Before deleting, verify the user owns the document
            Document document = documentService.getDocumentById(id);
            if (!document.getUserId().equals(currentUser.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this document.");
                return "redirect:/documents/my-documents";
            }

            documentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("message", "Document deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting document: " + e.getMessage());
        }
        return "redirect:/documents/my-documents";
    }

    /**
     * Handles user-initiated updates to document metadata.
     */
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateDocumentMetadata(
            @PathVariable Integer id,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "userComments", required = false) String userComments, // Allow optional comments
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        try {
            documentService.updateDocumentMetadata(id, documentType, userComments, currentUser.getUserId());
            redirectAttributes.addFlashAttribute("message", "Document details updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating document: " + e.getMessage());
        }
        return "redirect:/documents/my-documents";
    }


    /**
     * Admin view for all documents (for DocAdmin role).
     */
    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasRole('ADMIN')") // Assuming ADMIN can view all documents
    public String viewAllDocumentsForAdmin(Model model) {
        List<Document> allDocuments = documentService.getAllDocumentsForAdmin();
        model.addAttribute("documents", allDocuments);
        return "admin-document-dashboard"; // This refers to 'admin-document-dashboard.html'
    }

    /**
     * Endpoint for DocAdmin to validate (approve/reject) a document.
     * Accessible only by ADMINs with sufficient approval level.
     */
    @PostMapping("/validate/{documentId}")
    @PreAuthorize("hasRole('ADMIN') and authentication.principal.maxApprovalLevel >= 1") // Requires Level 1 admin or higher
    public ResponseEntity<String> validateDocument(
            @PathVariable Integer documentId,
            @RequestParam boolean isValid, // true for approve, false for reject
            @RequestParam(required = false) String comments) { // Optional comments from approver
        try {
            documentService.updateDocumentValidity(documentId, isValid, comments);
            return ResponseEntity.ok("Document " + (isValid ? "approved" : "rejected") + " successfully.");
        } catch (RuntimeException e) {
            // Specific errors from service (e.g., document not found, admin level issue)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating document: " + e.getMessage());
        }
    }
    @GetMapping("/my-applications")
    public String myApplications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userService.findByEmail(currentUserEmail)
                                      .orElseThrow(() -> new RuntimeException("Logged-in user not found: " + currentUserEmail));

        List<LoanApplication> rawApplications = loanApplicationService.getApplicationsByUserId(currentUser.getUserId());

        List<LoanApplicationDisplayDTO> displayApplications = rawApplications.stream()
            .map(app -> new LoanApplicationDisplayDTO(
                app.getApplicationId(),
                app.getCompanyName(), // UPDATED: Pass companyName from LoanApplication
                app.getLoanType(),    // UPDATED: Pass loanType from LoanApplication
                app.getLoanAmount(),
                app.getStatus().toString(),
                app.getSubmissionDate(),
                app.getUser().getName()
            ))
            .collect(Collectors.toList());

        model.addAttribute("applications", displayApplications);
        return "loanApplication/track-applications-list";
        
    }
}