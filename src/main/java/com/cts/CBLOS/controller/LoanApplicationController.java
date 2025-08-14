package com.cts.CBLOS.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cts.CBLOS.dto.LoanApplicationDisplayDTO;
import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus;
import com.cts.CBLOS.model.User;
import com.cts.CBLOS.repository.UserRepository;
import com.cts.CBLOS.service.CreditEvaluationService;
import com.cts.CBLOS.service.DocumentService;
import com.cts.CBLOS.service.LoanApplicationService;
import com.cts.CBLOS.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/loan-applications")
public class LoanApplicationController {

    @Autowired
    private CreditEvaluationService creditEvaluationService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentService documentService;

    @PostMapping("/submit")
    public String submitApplication(@ModelAttribute LoanApplication loanApplication, Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user == null) {
            model.addAttribute("errorMessage", "Error: Authenticated user not found in database.");
            return "loanApplication/submitapplication";
        }

        loanApplication.setUser(user);
        LoanApplication savedApplication = loanApplicationService.applyForLoan(loanApplication);
        session.setAttribute("applicationId", savedApplication.getApplicationId());

        creditEvaluationService.evaluateCredit(savedApplication.getApplicationId());

        return "redirect:/documents/upload-form?applicationId=" + savedApplication.getApplicationId();
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


    @GetMapping("/track/{applicationId}")
    public String trackApplicationDetails(@PathVariable Integer applicationId, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String currentUserEmail = principal.getName();
        User currentUser = userService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found: " + currentUserEmail));

        Optional<LoanApplication> loanApplicationOpt = loanApplicationService.getApplicationById(applicationId);

        if (loanApplicationOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Application not found for ID: " + applicationId);
            return "error";
        }

        LoanApplication loanApplication = loanApplicationOpt.get();

        if (!loanApplication.getUser().getUserId().equals(currentUser.getUserId())) {
            model.addAttribute("errorMessage", "You are not authorized to view this application.");
            return "error";
        }

        model.addAttribute("loanApplication", loanApplication);
        return "loanApplication/trackapplication";
    }

    @GetMapping("/status/{applicationId}")
    @ResponseBody
    public LoanApplicationStatus viewApplicationStatus(@PathVariable Integer applicationId, Principal principal) {
        if (principal == null) {
            return null;
        }

        // Optional: A more robust security check can be added here, similar to /track/{applicationId}
        // User currentUser = userService.findByEmail(principal.getName())
        //         .orElseThrow(() -> new RuntimeException("Logged-in user not found."));
        // Optional<LoanApplication> loanApplicationOpt = loanApplicationService.getApplicationById(applicationId);
        // if (loanApplicationOpt.isEmpty() || !loanApplicationOpt.get().getUser().getUserId().equals(currentUser.getUserId())) {
        //     // Return specific status for unauthorized access or not found
        //     return null;
        // }

        Optional<LoanApplication> loanApplicationOpt = loanApplicationService.getApplicationById(applicationId);
        return loanApplicationOpt.map(LoanApplication::getStatus).orElse(null);
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("loanApplication", new LoanApplication());
        return "loanApplication/submitapplication";
    }
}