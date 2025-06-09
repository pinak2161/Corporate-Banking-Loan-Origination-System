package com.cts.CBLOS.repository;

import com.cts.CBLOS.model.Document;
import com.cts.CBLOS.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    // Find documents by the ID of the user who uploaded them
    List<Document> findByUserId(Integer userId);
    long countByIsValidIsNull();
    // Find documents by the ID of the loan application they belong to
    List<Document> findByApplicationId(Integer applicationId);
	List<Document> findByLoanApplicationUser(User user);
    

    // You already have findById inherited from JpaRepository
    // Optional<Document> findById(Integer documentId);
}