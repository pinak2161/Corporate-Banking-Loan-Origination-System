package com.cts.CBLOS.repository;

import com.cts.CBLOS.model.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Integer> {
    List<Disbursement> findByLoanApplicationApplicationId(Integer applicationId);

    // FIX: Changed method name to explicitly reference 'userId' property in the 'User' entity
    List<Disbursement> findByLoanApplicationUserUserId(Integer userId);
}