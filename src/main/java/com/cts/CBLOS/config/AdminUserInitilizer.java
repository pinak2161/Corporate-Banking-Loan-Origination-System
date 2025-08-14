// src/main/java/com/cts/CBLOS/config/AdminUserInitilizer.java
package com.cts.CBLOS.config;

import com.cts.CBLOS.model.User;
import com.cts.CBLOS.model.User.UserRole; // Import UserRole from User class
import com.cts.CBLOS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminUserInitilizer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Configuration for Level 2 Admin (can approve both documents and loans)
    @Value("${app.admin.email:admin@CTS.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    // Configuration for Level 1 Admin (can approve only documents)
    @Value("${app.doc_admin.email:docadmin@CTS.com}")
    private String docAdminEmail;

    @Value("${app.doc_admin.password:docadmin123}")
    private String docAdminPassword;


    @Override
    public void run(String... args) {
        // --- Initialize Level 2 Admin (Main Admin) ---
        if (!userRepository.findByEmail(adminEmail).isPresent()) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setName("Main Admin");
            adminUser.setRole(UserRole.ADMIN); // Assigns UserRole.ADMIN directly
            adminUser.setMaxApprovalLevel(2); // Still useful for internal logic or further sub-division if needed
            userRepository.save(adminUser);
            System.out.println(">>> Default Level 2 Admin User Created: " + adminEmail + " <<<");
        }

        // --- Initialize Level 1 Admin (Document Admin) ---
        if (!userRepository.findByEmail(docAdminEmail).isPresent()) {
            User docAdminUser = new User();
            docAdminUser.setEmail(docAdminEmail);
            docAdminUser.setPassword(passwordEncoder.encode(docAdminPassword));
            docAdminUser.setName("Document Admin");
            docAdminUser.setRole(UserRole.DOC_ADMIN); // <-- **IMPORTANT CHANGE: Assign UserRole.DOC_ADMIN directly**
            docAdminUser.setMaxApprovalLevel(1); // Still useful for internal logic or clarity, but not strictly needed for basic role
            userRepository.save(docAdminUser);
            System.out.println(">>> Default Level 1 Document Admin User Created: " + docAdminEmail + " <<<");
        }
    }
}