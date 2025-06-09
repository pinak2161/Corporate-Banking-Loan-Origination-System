// src/main/java/com/cts/CBLOS/repository/CreditEvaluationRepository.java
package com.cts.CBLOS.repository;

import com.cts.CBLOS.model.CreditEvaluation;
import com.cts.CBLOS.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditEvaluationRepository extends JpaRepository<CreditEvaluation, Integer> {

    // Method to find a CreditEvaluation by its associated LoanApplication
    Optional<CreditEvaluation> findByLoanApplication(LoanApplication loanApplication);

    // Or more directly by application ID if needed, though findByLoanApplication is more idiomatic JPA
    // Optional<CreditEvaluation> findByLoanApplication_ApplicationId(Integer applicationId);
}