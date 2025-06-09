package com.cts.CBLOS.service;

import java.util.List;

import com.cts.CBLOS.model.Disbursement;

public interface DisbursementService {
    List<Disbursement> getDisbursementsByApplicationId(Integer applicationId);

    List<Disbursement> getAllDisbursements();

    Disbursement scheduleDisbursement(Disbursement disbursement);

    // ADDED: Method to find all disbursements for a given user ID
    List<Disbursement> findDisbursementsByUserId(Integer userId);
}