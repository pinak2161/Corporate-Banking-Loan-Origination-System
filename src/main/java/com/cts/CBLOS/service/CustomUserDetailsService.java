package com.cts.CBLOS.service; // Adjust package as needed

import com.cts.CBLOS.model.User;
import com.cts.CBLOS.model.User.UserRole; // Import the inner enum
import com.cts.CBLOS.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Using email as username
        // Assuming findByEmail in UserRepository as your User entity uses 'email' as identifier
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add the base role from the UserRole enum
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        // --- IMPORTANT: How to use maxApprovalLevel with the new enum ---
        // Option A: maxApprovalLevel is now redundant for DOC_ADMIN vs ADMIN distinction
        // If DOC_ADMIN is now a primary role from the enum, maxApprovalLevel might not be needed for this specific split.
        // If maxApprovalLevel was meant for further sub-divisions *within* ADMIN (e.g., ADMIN_LEVEL_1, ADMIN_LEVEL_2),
        // then you'd check user.getRole() == UserRole.ADMIN && user.getMaxApprovalLevel() == X

        // Option B: maxApprovalLevel *refines* the ADMIN role (less common if DOC_ADMIN is already an enum value)
        // If ADMIN role still implies maxApprovalLevel distinctions, you might do something like this:
        /*
        if (user.getRole() == UserRole.ADMIN && user.getMaxApprovalLevel() != null) {
            if (user.getMaxApprovalLevel() == 1) {
                // This would mean 'ADMIN' with level 1 is essentially a 'DOC_ADMIN'
                // This might be confusing if DOC_ADMIN is also an enum value.
                // Best to directly use the enum role now.
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN_LEVEL_1")); // Or similar
            } else if (user.getMaxApprovalLevel() == 2) {
                 authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN_LEVEL_2")); // Or similar
            }
        }
        */

        // For simplicity, now that you have DOC_ADMIN directly in the enum,
        // let's assume we primarily rely on `user.getRole()` for the main role.
        // `maxApprovalLevel` might be used for other internal business logic or for *even finer-grained* permissions
        // than basic Spring Security roles, but not for the primary role assignment.

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email as the principal's username
                user.getPassword(), // Ensure this is encoded!
                authorities
        );
    }
}