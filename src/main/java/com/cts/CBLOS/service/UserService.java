package com.cts.CBLOS.service;

import com.cts.CBLOS.model.User;
import java.util.Optional;

public interface UserService {
    // Method to save a new user (e.g., during signup)
    User saveUser(User user);

    // Method to find a user by their email (used for login and by controllers)
    Optional<User> findByEmail(String email);

    // THIS IS THE NEW METHOD DECLARATION YOU NEED
    // Method to find a user by their ID
    Optional<User> findById(Integer userId);

    // Add any other user-related service methods here as needed
}