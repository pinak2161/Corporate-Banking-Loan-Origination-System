package com.example.demo.controller;


import com.example.demo.model.LoanApplication;
import com.example.demo.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @Autowired
    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @GetMapping
    public String getAllApplications(Model model) {
        List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplications();
        model.addAttribute("loanApplications", loanApplications);
        return "user_dashboard";
    }

    @GetMapping("/new")
    public String showCreateNewApplicationForm(Model model) {
        model.addAttribute("loanApplication", new LoanApplication());
        return "create-application";
    }

    @PostMapping("/new")
    public String createNewApplication(@ModelAttribute LoanApplication loanApplication, Model model) {
        System.out.println("--- Entering createNewApplication method ---");
        System.out.println("LoanApplication received from form: " + loanApplication);

        // Set default status if not provided by the form
        if (loanApplication.getStatus() == null || loanApplication.getStatus().isEmpty()) {
            loanApplication.setStatus("PENDING");
            System.out.println("Status set to PENDING.");
        }
        loanApplication.setSubmittedDate(LocalDate.now());
        loanApplication.setLastUpdated(LocalDate.now());
        System.out.println("SubmittedDate and LastUpdated set to: " + LocalDate.now());

        // --- Automatically set contactEmail from logged-in user ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String userEmail = authentication.getName();
            loanApplication.setContactEmail(userEmail);
            System.out.println("Contact email set from authenticated user: " + userEmail);
        } else {
            System.out.println("Warning: No authenticated user found. Contact email will remain null or previous value.");
            // Optionally, handle this more robustly, e.g., redirect to login or show an error
            // model.addAttribute("errorMessage", "You must be logged in to create an application.");
            // return "create-application"; // Return to the form with an error message
        }

        // --- Validation Check for required fields before saving ---
        // applicationId is unique and not null
        if (loanApplication.getApplicationId() == null || loanApplication.getApplicationId().trim().isEmpty()) {
            System.err.println("ERROR: Application ID is null or empty. Cannot save.");
            model.addAttribute("errorMessage", "Application ID is required.");
            return "create-application"; // Return to form with error
        }
        // legalBusinessName is not null
        if (loanApplication.getLegalBusinessName() == null || loanApplication.getLegalBusinessName().trim().isEmpty()) {
            System.err.println("ERROR: Legal Business Name is null or empty. Cannot save.");
            model.addAttribute("errorMessage", "Legal Business Name is required.");
            return "create-application"; // Return to form with error
        }
        // loanAmount is not null
        if (loanApplication.getLoanAmount() == null) {
            System.err.println("ERROR: Loan Amount is null. Cannot save.");
            model.addAttribute("errorMessage", "Loan Amount is required.");
            return "create-application"; // Return to form with error
        }


        System.out.println("LoanApplication object BEFORE save: " + loanApplication);

        try {
            LoanApplication savedApplication = loanApplicationService.saveLoanApplication(loanApplication);
            System.out.println("LoanApplication saved successfully! ID: " + savedApplication.getId());
            return "redirect:/applications";
        } catch (Exception e) {
            System.err.println("ERROR: Failed to save loan application: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for detailed error
            model.addAttribute("errorMessage", "Failed to save application. Please try again. Error: " + e.getMessage());
            return "create-application"; // Return to form with error
        }
    }

    @GetMapping("/{id}")
    public String viewApplicationDetails(@PathVariable Long id, Model model) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.getLoanApplicationById(id);
        if (loanApplicationOptional.isPresent()) {
            model.addAttribute("loanApplication", loanApplicationOptional.get());
            return "application-details";
        } else {
            return "redirect:/applications?error=notFound";
        }
    }
}
