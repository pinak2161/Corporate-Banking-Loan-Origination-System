package com.cts.CBLOS.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isSuperAdmin = authorities.stream()
                                         .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isDocAdmin = authorities.stream()
                                       .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOC_ADMIN"));

        if (isSuperAdmin) {
            // CHANGE THIS LINE: Redirect to the URL your controller maps, which is "/admin"
            response.sendRedirect("/admin");
        } else if (isDocAdmin) {
            response.sendRedirect("/admin/docadmin_dash");
        } else {
            response.sendRedirect("/home");
        }
    }
}