//package com.example.demo.repository;
//
//
//import com.example.demo.model.CreditEvaluation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
////No changes needed from previous version.
//@Repository
//public interface CreditEvaluationRepository extends JpaRepository<CreditEvaluation, Long> {
// // Custom method to find all evaluations for a specific application, ordered by date descending
// List<CreditEvaluation> findByLoanApplication_ApplicationIdOrderByEvaluationDateDesc(Long applicationId);
//
// // Custom method to find the top (latest) evaluation for a specific application
// Optional<CreditEvaluation> findTopByLoanApplication_ApplicationIdOrderByEvaluationDateDesc(Long applicationId);
//}