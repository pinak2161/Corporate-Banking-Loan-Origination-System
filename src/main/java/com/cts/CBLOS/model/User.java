// src/main/java/com/cts/CBLOS/model/User.java
package com.cts.CBLOS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String email;
    private String password;
    private String name;

    @Column(name = "max_approval_level")
    private Integer maxApprovalLevel;

    @Enumerated(EnumType.STRING) // Store enum name as String in DB
    private UserRole role;

    public enum UserRole {
        ADMIN, USER, DOC_ADMIN // You can add DOC_ADMIN here
    }

    // Constructors
    public User() {}

    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // Getters
    public Integer getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public Integer getMaxApprovalLevel() { return maxApprovalLevel; }
    public UserRole getRole() { return role; }

    // Setters
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setMaxApprovalLevel(Integer maxApprovalLevel) { this.maxApprovalLevel = maxApprovalLevel; }
    public void setRole(UserRole role) { this.role = role; }

    // equals(), hashCode(), toString() - (provided in your file)
    @Override
    public boolean equals(Object o) { /* ... */ return true;}
    @Override
    public int hashCode() { /* ... */ return 0;}
    @Override
    public String toString() { /* ... */ return "";}
}