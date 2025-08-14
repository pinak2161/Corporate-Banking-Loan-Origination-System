package com.cts.CBLOS.service;

import com.cts.CBLOS.model.User;
import com.cts.CBLOS.repository.UserRepository; // Make sure UserRepository is imported
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Assuming you use this for password hashing
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false) // Make it optional for initial setup if PasswordEncoder isn't fully configured yet
    private PasswordEncoder passwordEncoder; // Used for hashing passwords

    @Override
    public User saveUser(User user) {
        // Hash the password before saving for security
        if (passwordEncoder != null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
             // Handle case where passwordEncoder is null (e.g., during testing or incomplete config)
             System.err.println("Warning: PasswordEncoder is not available. Saving raw password for user: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // THIS IS THE NEW METHOD IMPLEMENTATION YOU NEED
    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }
}