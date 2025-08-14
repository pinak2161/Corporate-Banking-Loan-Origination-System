package com.cts.CBLOS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cts.CBLOS.model.Disbursement;
import com.cts.CBLOS.service.DisbursementService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/disbursement")
public class DisbursementController {

    private final DisbursementService disbursementService;

    @Autowired
    public DisbursementController(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    // You've chosen to comment out the single-application view, which simplifies things.
    // If you need it back, uncomment and ensure applicationId is correctly set in session.

    @GetMapping("/viewMyAllDisbursements")
    @PreAuthorize("hasRole('USER')") // Uncomment if using Spring Security
    public String viewAllMyDisbursements(HttpSession session, Model model) {
        // Assuming 'userId' is stored in the session upon successful login
        Integer userId = (Integer) session.getAttribute("userId");

        // IMPORTANT: Add null check for userId
        if (userId == null) {
            // Handle case where user ID is not found in session (e.g., user not logged in or session expired)
            // Redirect to login page or an error page
            return "redirect:/login"; // Adjust this path to your actual login endpoint
            // Alternatively, you could add an error message to the model and return a specific error view
        }

        List<Disbursement> disbursements = disbursementService.findDisbursementsByUserId(userId);
        model.addAttribute("disbursements", disbursements);
        model.addAttribute("userId", userId); // Optional: Pass userId to the view for display
        return "report"; // Use the new report template for user-specific data
    }

    // Your commented-out POST/GET mappings for admin functionality remain as they were.
    // Uncomment them if you plan to implement that functionality later.
}