//package com.cts.CBLOS.service;
// 
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.cts.CBLOS.model.User;
//import com.cts.CBLOS.repository.UserRepository;
// 
//import java.util.Optional;
// 
//@Service
//public class AuthenticationService {
// 
//    @Autowired
//    private UserRepository userRepository;
// 
//    public boolean authenticate(String email, String password) {
//        // Find the user by email
//        Optional<User> userOptional = userRepository.findByEmail(email);
// 
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            // In a real application, you should hash and salt passwords.
//            // For now, it's a direct comparison as per your existing code.
//            System.out.println("Stored Password for " + email + ": " + user.getPassword());
//            System.out.println("Entered Password: " + password);
//            return user.getPassword().equals(password);
//        } else {
//            System.out.println("User with email " + email + " not found!");
//            return false; // User not found, authentication fails
//        }
//    }
//}