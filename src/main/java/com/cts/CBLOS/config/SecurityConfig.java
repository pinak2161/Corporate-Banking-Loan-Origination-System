package com.cts.CBLOS.config;

import com.cts.CBLOS.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Optional but Recommended: Define Role Hierarchy (e.g., ADMIN implies USER)
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER"; // ADMIN has all privileges of USER
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    // Optional but Recommended: Link Role Hierarchy to Web Security Expressions
    @Bean
    public SecurityExpressionHandler webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF is enabled by default in Spring Security.
            // Use ignoringRequestMatchers for paths where CSRF checks should be bypassed (e.g., H2 console).
            // Do NOT use .disable() unless you fully understand the security implications.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Example: Ignore H2 console for easier access
            )
            .authorizeHttpRequests(authorize -> authorize
            	    // 1. PUBLICLY ACCESSIBLE PAGES (keep these at the top)
            	
            	    .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**", "/Bankbanner.jpg", "/features.png", "/uploads/**", "/creditEvaluation", "/h2-console/**").permitAll()
            	    
            	    // 2. SPECIFIC ADMIN PATHS (MOST SPECIFIC FIRST)
            	    // These paths are under /admin/ and can be accessed by both ADMIN and DOC_ADMIN.
            	    // This includes the doc admin dashboard and document-related actions.
            	    .requestMatchers(
            	        "/admin/docadmin_dash",              // Exact path for Doc Admin dashboard
            	        "/admin/documents/**",               // For viewing/approving/rejecting documents
            	        "/admin/view/**",                    // For viewing individual documents (e.g., PDF)
            	        "/admin/validate/**",                // For document validation actions
            	        "/admin/loan-applications/**",       // If doc admin needs to see loan application details
            	        "/admin/review"                      // If there's a general review page for documents
            	    ).hasAnyRole("ADMIN", "DOC_ADMIN")

            	    // 3. SUPER ADMIN DASHBOARD (Exact match for /admin)
            	    // This comes after the more specific /admin/ paths, but is itself very specific.
            	    .requestMatchers("/admin").hasRole("ADMIN") // This ensures only ADMIN gets the root /admin dashboard

            	    // 4. USER-SPECIFIC PAGES (keep these as needed)
            	    .requestMatchers("/loan-applications/**", "/approval/**", "/documents/**").hasRole("USER")

            	    // 5. ANY OTHER AUTHENTICATED ACCESS (fallback)
            	    // This ensures any other path not explicitly permitted or role-restricted requires authentication.
            	    .anyRequest().authenticated()
            	)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        // Apply the Role Hierarchy to the expression handler (optional but good practice)
        http.authenticationProvider(authenticationProvider());
        http.setSharedObject(SecurityExpressionHandler.class, webExpressionHandler());


        return http.build();
    }
}