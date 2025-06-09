package com.cts.CBLOS.service;
import org.springframework.transaction.annotation.Transactional; // <-- ADD THIS LINE

import com.cts.CBLOS.model.Document;
import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.repository.DocumentRepository;
import com.cts.CBLOS.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.cts.CBLOS.model.User;
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository; // Assuming you have this repository
    

    

    
    @Override
    @Transactional
    public void validateDocument(Integer documentId, boolean isValid) {
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            document.setIsValid(isValid); // Set to true for approved, false for rejected
            documentRepository.save(document); // Save the updated document
            System.out.println("Document " + documentId + " validation status set to: " + (isValid ? "Approved" : "Rejected"));
        } else {
            throw new RuntimeException("Document with ID " + documentId + " not found.");
        }
    }
    @Override
    public long countByIsValidIsNull() {
        return documentRepository.countByIsValidIsNull();
    }
    @Override
    public List<Document> getDocumentsByUser(User user) {
        return documentRepository.findByLoanApplicationUser(user);
    }

    // You might have other methods here, e.g., to save a new document:
    // @Override
    // public Document saveDocument(Document document) {
    //     return documentRepository.save(document);
    // }
 @Override
    public Document uploadDocument(MultipartFile file, String documentType, Integer applicationId, Integer userId) throws IOException {
        // Optional: Check if the application exists and belongs to the user
        Optional<LoanApplication> loanApplicationOpt = loanApplicationRepository.findById(applicationId);
        if (loanApplicationOpt.isEmpty() || !loanApplicationOpt.get().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Loan application not found or does not belong to the user.");
        }

        Document document = new Document(
                file.getOriginalFilename(),
                file.getContentType(),
                documentType,
                file.getBytes(),
                LocalDateTime.now(),
                userId,
                applicationId
        );
        return documentRepository.save(document);
    }
    
    // --- NEW METHOD IMPLEMENTATIONS ---

    @Override
    public long countPendingDocuments() {
        // Assuming 'isValid' field is null for pending documents
        return documentRepository.countByIsValidIsNull();
    }
    @Override
    public long count() {
        // JpaRepository provides a built-in count() method
        return documentRepository.count();
    }
    // --- END NEW METHOD IMPLEMENTATIONS ---

    @Override
    public Document getDocumentById(Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));
    }

    @Override
    public List<Document> getDocumentsByUserId(Integer userId) {
        return documentRepository.findByUserId(userId);
    }

    @Override
    public List<Document> getDocumentsByApplicationId(Integer applicationId) {
        return documentRepository.findByApplicationId(applicationId);
    }

    @Override
    public void deleteDocument(Integer documentId) {
        documentRepository.deleteById(documentId);
    }

    @Override
    public List<Document> getAllDocumentsForAdmin() {
        return documentRepository.findAll();
    }

    @Override
    public void updateDocumentValidity(Integer documentId, boolean isValid, String comments) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));
        document.setIsValid(isValid);
        document.setComments(comments); // Admin comments
        documentRepository.save(document);
    }

    @Override
    public Document updateDocumentMetadata(Integer documentId, String documentType, String userComments, Integer userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        // Security check: Ensure the logged-in user owns this document
        if (!document.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to modify this document.");
        }

        document.setDocumentType(documentType);
        document.setUserComments(userComments); // User's own comments
        return documentRepository.save(document);
        
    }
    
    
}