package com.cts.CBLOS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;

import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.User;
import com.cts.CBLOS.repository.LoanApplicationRepository;
import com.cts.CBLOS.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    // Login page: Handles display of login form and messages from Spring Security
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(name = "error", required = false) String error,
                                @RequestParam(name = "logout", required = false) String logout,
                                CsrfToken csrfToken) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("logout", "You have been logged out successfully.");
        }

        // Ensure _csrf token is available for Thymeleaf form
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }

        return "login";
    }

    // Signup page
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered. Please use a different email.");
            return "signup";
        }
        user.setRole(User.UserRole.USER); // Default role for new signups
        userService.saveUser(user);
        return "redirect:/login?signupSuccess=true"; // Redirect to login with success message
    }

    // User home page (after successful login)
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName(); // This is typically the email or username

        // Retrieve the User object from the database using the authenticated email/username
        User user = userService.findByEmail(currentUserName).orElse(null);

        if (user != null) {
            // Store the userId in the session for later retrieval (e.g., by DisbursementController)
            session.setAttribute("userId", user.getUserId()); // <--- ADDED THIS CRUCIAL LINE
            System.out.println("Stored userId in session: " + user.getUserId() + " for user: " + currentUserName);

            // This part ensures userEmail is always in session and matches, though userId is more unique.
            // Keeping it for consistency with your existing logic.
            String userEmailInSession = (String) session.getAttribute("userEmail");
            if (userEmailInSession == null || !userEmailInSession.equals(currentUserName)) {
                session.setAttribute("userEmail", currentUserName);
                System.out.println("Stored userEmail in session: " + currentUserName);
            }

            model.addAttribute("message", "Welcome to your home page, " + currentUserName + "!");
            System.out.println("Accessing regular user home for: " + currentUserName);

            // Your existing logic for setting applicationId (if it's not already set)
            // This part is specific to linking to a *single* loan application for some features.
            if (session.getAttribute("applicationId") == null) {
                List<LoanApplication> userApplications = loanApplicationRepository.findByUser(user);

                if (!userApplications.isEmpty()) {
                    session.setAttribute("applicationId", userApplications.get(0).getApplicationId());
                    System.out.println("Fetched applicationId from DB: " + userApplications.get(0).getApplicationId() + " (first application found)");
                } else {
                    System.out.println("No loan application found for user: " + currentUserName);
                }
            }
        } else {
            // This case indicates a serious issue: authenticated user not found in DB
            // This shouldn't happen if authentication is successful and user exists.
            System.err.println("Error: Authenticated user '" + currentUserName + "' not found in database! Forcing logout.");
            // Force logout or redirect to login, as the user data is inconsistent.
            return "redirect:/logout";
        }
        return "home";
    }
}