//package com.example.demo.controller;
//
//
//import com.example.demo.model.LoanApplication;
//import com.example.demo.service.LoanApplicationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/applications")
//public class LoanApplicationController {
//
//    private final LoanApplicationService loanApplicationService;
//
//    @Autowired
//    public LoanApplicationController(LoanApplicationService loanApplicationService) {
//        this.loanApplicationService = loanApplicationService;
//    }
//
//    @GetMapping
//    public String getAllApplications(Model model) {
//        List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplications();
//        model.addAttribute("loanApplications", loanApplications);
//        return "user_dashboard";
//    }
//
//    @GetMapping("/new")
//    public String showCreateNewApplicationForm(Model model) {
//        model.addAttribute("loanApplication", new LoanApplication());
//        return "create-application";
//    }
//
//    @PostMapping("/new")
//    public String createNewApplication(@ModelAttribute LoanApplication loanApplication, Model model) {
//        System.out.println("--- Entering createNewApplication method ---");
//        System.out.println("LoanApplication received from form: " + loanApplication);
//
//        // Set default status if not provided by the form
//        if (loanApplication.getStatus() == null || loanApplication.getStatus().isEmpty()) {
//            loanApplication.setStatus("PENDING");
//            System.out.println("Status set to PENDING.");
//        }
//        loanApplication.setSubmittedDate(LocalDate.now());
//        loanApplication.setLastUpdated(LocalDate.now());
//        System.out.println("SubmittedDate and LastUpdated set to: " + LocalDate.now());
//
//        // --- Automatically set contactEmail from logged-in user ---
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
//            String userEmail = authentication.getName();
//            loanApplication.setContactEmail(userEmail);
//            System.out.println("Contact email set from authenticated user: " + userEmail);
//        } else {
//            System.out.println("Warning: No authenticated user found. Contact email will remain null or previous value.");
//            // Optionally, handle this more robustly, e.g., redirect to login or show an error
//            // model.addAttribute("errorMessage", "You must be logged in to create an application.");
//            // return "create-application"; // Return to the form with an error message
//        }
//
//        // --- Validation Check for required fields before saving ---
//        // applicationId is unique and not null
//        if (loanApplication.getApplicationId() == null || loanApplication.getApplicationId().trim().isEmpty()) {
//            System.err.println("ERROR: Application ID is null or empty. Cannot save.");
//            model.addAttribute("errorMessage", "Application ID is required.");
//            return "create-application"; // Return to form with error
//        }
//        // legalBusinessName is not null
//        if (loanApplication.getLegalBusinessName() == null || loanApplication.getLegalBusinessName().trim().isEmpty()) {
//            System.err.println("ERROR: Legal Business Name is null or empty. Cannot save.");
//            model.addAttribute("errorMessage", "Legal Business Name is required.");
//            return "create-application"; // Return to form with error
//        }
//        // loanAmount is not null
//        if (loanApplication.getLoanAmount() == null) {
//            System.err.println("ERROR: Loan Amount is null. Cannot save.");
//            model.addAttribute("errorMessage", "Loan Amount is required.");
//            return "create-application"; // Return to form with error
//        }
//
//
//        System.out.println("LoanApplication object BEFORE save: " + loanApplication);
//
//        try {
//            LoanApplication savedApplication = loanApplicationService.saveLoanApplication(loanApplication);
//            System.out.println("LoanApplication saved successfully! ID: " + savedApplication.getId());
//            return "redirect:/applications";
//        } catch (Exception e) {
//            System.err.println("ERROR: Failed to save loan application: " + e.getMessage());
//            e.printStackTrace(); // Print full stack trace for detailed error
//            model.addAttribute("errorMessage", "Failed to save application. Please try again. Error: " + e.getMessage());
//            return "create-application"; // Return to form with error
//        }
//    }
//
//    @GetMapping("/{id}")
//    public String viewApplicationDetails(@PathVariable Long id, Model model) {
//        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.getLoanApplicationById(id);
//        if (loanApplicationOptional.isPresent()) {
//            model.addAttribute("loanApplication", loanApplicationOptional.get());
//            return "application-details";
//        } else {
//            return "redirect:/applications?error=notFound";
//        }
//    }
//}

package com.example.demo.controller;

import com.example.demo.model.LoanApplication;
import com.example.demo.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Ensure this import is present if using @Valid
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Ensure this import is present if using @Valid
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
        // Get the currently authenticated user's email from Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = null;
        List<LoanApplication> loanApplications;

        // Check if a user is authenticated and not an "anonymousUser"
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            userEmail = authentication.getName(); // In Spring Security, getName() typically returns the username (which is often the email)
            System.out.println("Fetching applications for authenticated user: " + userEmail);
            // Fetch applications specifically for this user's email
            loanApplications = loanApplicationService.getLoanApplicationsByContactEmail(userEmail);
        } else {
            // If no user is logged in or it's an anonymous user, return an empty list
            // or redirect to a login page if access to this dashboard requires authentication.
            System.out.println("No authenticated user found. Returning an empty list of applications.");
            loanApplications = List.of(); // Return an empty, immutable list
            // If you want to force login: return "redirect:/login"; // Adjust '/login' to your actual login page
        }

        // Add the (potentially empty) list of applications to the model for the frontend
        model.addAttribute("loanApplications", loanApplications);
        return "user_dashboard"; // This will render your existing dashboard HTML
    }

    @GetMapping("/new")
    public String showCreateNewApplicationForm(Model model) {
        model.addAttribute("loanApplication", new LoanApplication());
        return "create-application";
    }

    @PostMapping("/new")
    public String createNewApplication(
            @Valid @ModelAttribute("loanApplication") LoanApplication loanApplication,
            BindingResult bindingResult, // Must immediately follow the @Valid object
            Model model) {

        System.out.println("--- Entering createNewApplication method ---");
        System.out.println("LoanApplication received from form: " + loanApplication);

        // Handle validation errors from @Valid annotations in the model
        if (bindingResult.hasErrors()) {
            System.err.println("Validation errors occurred: " + bindingResult.getAllErrors());
            model.addAttribute("errorMessage", "Please correct the errors below.");
            return "create-application"; // Return to the form to display errors
        }

        // Set default status and dates
        if (loanApplication.getStatus() == null || loanApplication.getStatus().isEmpty()) {
            loanApplication.setStatus("PENDING");
            System.out.println("Status set to PENDING.");
        }
        loanApplication.setSubmittedDate(LocalDate.now());
        loanApplication.setLastUpdated(LocalDate.now());
        System.out.println("SubmittedDate and LastUpdated set to: " + LocalDate.now());

        // Automatically set contactEmail from logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String userEmail = authentication.getName();
            loanApplication.setContactEmail(userEmail);
            System.out.println("Contact email set from authenticated user: " + userEmail);
        } else {
            // This case indicates an attempt to submit without a logged-in user.
            // You might want to throw an error or redirect to login.
            System.err.println("CRITICAL WARNING: Attempted to create application without authenticated user. Contact email will be null.");
            model.addAttribute("errorMessage", "You must be logged in to create an application.");
            return "create-application"; // Return to the form with an error
        }

        System.out.println("LoanApplication object BEFORE save: " + loanApplication);

        try {
            LoanApplication savedApplication = loanApplicationService.saveLoanApplication(loanApplication);
            System.out.println("LoanApplication saved successfully! ID: " + savedApplication.getId());
            // Redirect to the dashboard, which will now show the newly created application
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
            LoanApplication application = loanApplicationOptional.get();

            // --- IMPORTANT SECURITY CHECK: Ensure user can only view their own applications ---
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = null;
            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
                currentUserEmail = authentication.getName();
            }

            // Check if the current user's email matches the application's contact email
            if (currentUserEmail != null && currentUserEmail.equals(application.getContactEmail())) {
                model.addAttribute("loanApplication", application);
                return "application-details";
            } else {
                // If the user tries to access an application that doesn't belong to them
                System.err.println("Authorization Denied: User " + currentUserEmail + " attempted to view application " + id + " not associated with their email.");
                // You might want to redirect with a specific error message or to a generic error page
                return "redirect:/applications?error=accessDenied"; // Redirects to dashboard with an error parameter
            }
        } else {
            // Application not found by ID
            System.err.println("Loan application with ID " + id + " not found.");
            return "redirect:/applications?error=notFound"; // Redirects to dashboard with an error parameter
        }
    }
}
