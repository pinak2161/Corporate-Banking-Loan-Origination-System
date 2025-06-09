package com.cts.CBLOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.CBLOS.model.Disbursement;
import com.cts.CBLOS.repository.DisbursementRepository;

@Service
public class DisbursementServiceImpl implements DisbursementService {

    private final DisbursementRepository disbursementRepository; // Changed to final for clarity

    @Autowired // Best practice to use constructor injection for required dependencies
    public DisbursementServiceImpl(DisbursementRepository disbursementRepository) {
        this.disbursementRepository = disbursementRepository;
    }

    @Override
    public List<Disbursement> getDisbursementsByApplicationId(Integer applicationId) {
        return disbursementRepository.findByLoanApplicationApplicationId(applicationId);
    }

    @Override
    public Disbursement scheduleDisbursement(Disbursement disbursement) {
        return disbursementRepository.save(disbursement);
    }

    @Override
    public List<Disbursement> getAllDisbursements() {
        return disbursementRepository.findAll();
    }

    // ADDED: Implementation for finding disbursements by user ID
    @Override
    public List<Disbursement> findDisbursementsByUserId(Integer userId) {
        return disbursementRepository.findByLoanApplicationUserUserId(userId); // <--- CHANGE IS HERE
    }
}