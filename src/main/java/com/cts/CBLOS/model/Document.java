package com.cts.CBLOS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;

    private String fileName;
    private String contentType;
    private String documentType; // e.g., "Aadhar Card", "PAN Card"

    @Lob // For large binary data
    @Column(columnDefinition = "LONGBLOB") // Or BLOB depending on database
    private byte[] data;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    // Status fields for admin validation
    @Column(name = "is_valid")
    private Boolean isValid; // true for approved, false for rejected, null for pending

    private String comments; // Comments from admin during validation

    // NEW FIELD: User's own comments on the document
    @Column(name = "user_comments")
    private String userComments;

    @Column(name = "user_id")
    private Integer userId; // Foreign key to User

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user; // Relationship with User entity

    @Column(name = "application_id")
    private Integer applicationId; // Foreign key to LoanApplication

    @ManyToOne
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private LoanApplication loanApplication; // Relationship with LoanApplication

    // --- Constructors ---
    public Document() {
    }

    public Document(String fileName, String contentType, String documentType, byte[] data, LocalDateTime uploadDate, Integer userId, Integer applicationId) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.documentType = documentType;
        this.data = data;
        this.uploadDate = uploadDate;
        this.isValid = null; // Default to pending
        this.userId = userId;
        this.applicationId = applicationId;
    }

    // --- Getters ---
    public Integer getDocumentId() {
        return documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public byte[] getData() {
        return data;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public String getComments() {
        return comments;
    }

    public String getUserComments() {
        return userComments;
    }

    public Integer getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    // --- Setters ---
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }
}