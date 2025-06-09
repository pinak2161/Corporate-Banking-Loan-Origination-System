package com.cts.CBLOS.service;

import com.cts.CBLOS.model.Document;
import com.cts.CBLOS.model.User;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    // THIS IS THE CORRECT SIGNATURE FOR UPLOAD:
    Document uploadDocument(MultipartFile file, String documentType, Integer applicationId, Integer userId) throws IOException;

    Document getDocumentById(Integer documentId);
    List<Document> getDocumentsByUserId(Integer userId);
    List<Document> getDocumentsByApplicationId(Integer applicationId);
    void deleteDocument(Integer documentId);
    List<Document> getAllDocumentsForAdmin(); // For admin dashboard
    void updateDocumentValidity(Integer documentId, boolean isValid, String comments); // For admin validation
    // --- NEW METHODS YOU NEED TO ADD ---
    long countPendingDocuments();
    long count();
    // Method for users to update document metadata (e.g., document type, user comments)
    Document updateDocumentMetadata(Integer documentId, String documentType, String userComments, Integer userId);
    /**
     * Validates a document by setting its isValid status.
     *
     * @param documentId The ID of the document to validate.
     * @param isValid    True for approval, False for rejection.
     * @throws RuntimeException if the document is not found.
     */
 // NEW: Method for document validation by admin

    // NEW: Method to get documents for a specific user
    List<Document> getDocumentsByUser(User user);

    void validateDocument(Integer documentId, boolean isValid); // <--- ADD THIS METHOD

	long countByIsValidIsNull();

	;

}