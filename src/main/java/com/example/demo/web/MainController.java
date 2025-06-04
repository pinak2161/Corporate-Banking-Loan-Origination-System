package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Needed for adding attributes like 'error'
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Import PostMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Needed to capture form inputs

// Assuming you have an AuthenticationService or similar to handle actual login logic
// import com.example.demo.service.AuthenticationService; 

@Controller
@RequestMapping("/")
public class MainController {

    // If you have a custom AuthenticationService, you'd inject it here:
    // private final AuthenticationService authenticationService;

    // public MainController(AuthenticationService authenticationService) {
    //     this.authenticationService = authenticationService;
    // }

    @GetMapping
    public String home() {
        return "main-landing";
    }
    
    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logout", "You have been logged out.");
        }
        return "login";
    }

    // This POST mapping is crucial for handling the login form submission
    // Spring Security usually handles this automatically if you use its default login page.
    // However, if you have a custom login processing (e.g., for non-Spring Security login, or advanced features),
    // you would implement custom authentication here.
    // For a standard Spring Security setup, this @PostMapping("/login") method might not be strictly needed
    // if Spring Security's default form handling is active and correctly configured.
    // But it's good to have if you intend to add custom logic.
    /*
    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // This is a placeholder for your actual authentication logic.
        // In a real application, you'd use a service to authenticate against your user store.
        boolean isAuthenticated = false; 

        // Example: Replace with actual authentication service call
        if ("testuser".equals(username) && "testpassword".equals(password)) { // Dummy credentials
            isAuthenticated = true;
        }

        if (isAuthenticated) {
            // Spring Security typically handles redirection after success.
            // If you are relying on Spring Security's default, this redirect might be redundant.
            // But if you're customizing authentication flow, this is where you'd redirect.
            return "redirect:/user_dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password. Please try again.");
            return "login"; // Go back to login page with error
        }
    }
    */
    
//    @GetMapping("/applications")
//    public String user_dashboard() {
//        return "user_dashboard";
//    }
    
}