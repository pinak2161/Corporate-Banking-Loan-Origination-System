package com.cts.CBLOS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // This method now directly returns the "main-landing" view for the root URL ("/").
    @GetMapping("/")
    public String mainLandingPage() {
        return "main-landing"; // This will look for src/main/resources/templates/main-landing.html
    }

    // Keep other general-purpose GET mappings here if they are for non-authenticated pages.
    // Ensure they are uncommented and functional if you intend to use them.
}