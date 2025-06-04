//package com.example.demo.service;
//
//
//import com.example.demo.model.CreditEvaluation;
//import com.example.demo.model.LoanApplication;
//import com.example.demo.repository.CreditEvaluationRepository;
//import com.example.demo.repository.LoanApplicationRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class LoanApplicationService {
//
// private final LoanApplicationRepository loanApplicationRepository;
// private final CreditEvaluationRepository creditEvaluationRepository;
//
// public LoanApplicationService(LoanApplicationRepository loanApplicationRepository,
//                               CreditEvaluationRepository creditEvaluationRepository) {
//     this.loanApplicationRepository = loanApplicationRepository;
//     this.creditEvaluationRepository = creditEvaluationRepository;
// }
//
// // --- Methods for LoanApplication ---
//
// public List<LoanApplication> getAllLoanApplications() {
//     return loanApplicationRepository.findAll();
// }
//
// public Optional<LoanApplication> getLoanApplicationById(Long id) {
//     return loanApplicationRepository.findById(id);
// }
//
// @Transactional
// public LoanApplication submitNewApplication(LoanApplication application) {
//     // Basic validation (more complex validation can be done with JSR 303 annotations or custom validators)
//     if (application.getCompanyName() == null || application.getCompanyName().isEmpty()) {
//         throw new IllegalArgumentException("Company Name is required.");
//     }
//     if (application.getLoanAmount() == null || application.getLoanAmount().doubleValue() <= 0) {
//         throw new IllegalArgumentException("Loan Amount must be positive.");
//     }
//
//     // Set initial status and dates if they are not already set.
//     // @PrePersist in the entity also handles this, but explicit setting here
//     // ensures consistency before saving, especially if the entity isn't new.
//     if (application.getStatus() == null) {
//         application.setStatus(LoanApplication.ApplicationStatus.PENDING_SUBMISSION);
//     }
//     if (application.getSubmissionDate() == null) {
//         application.setSubmissionDate(LocalDate.now());
//     }
//     if (application.getLastUpdated() == null) {
//          application.setLastUpdated(LocalDate.now());
//     }
//
//     return loanApplicationRepository.save(application);
// }
//
// @Transactional
// public LoanApplication updateApplicationStatus(Long id, LoanApplication.ApplicationStatus newStatus) {
//     Optional<LoanApplication> optionalApplication = loanApplicationRepository.findById(id);
//     if (optionalApplication.isPresent()) {
//         LoanApplication application = optionalApplication.get();
//         application.setStatus(newStatus);
//         // lastUpdated is handled by @PreUpdate in the entity
//         return loanApplicationRepository.save(application);
//     }
//     return null; // Or throw an exception like ApplicationNotFoundException, for better error handling
// }
//
// // --- Methods for CreditEvaluation ---
//
// @Transactional
// public CreditEvaluation addCreditEvaluation(Long applicationId, CreditEvaluation evaluation) {
//     Optional<LoanApplication> optionalApplication = loanApplicationRepository.findById(applicationId);
//     if (optionalApplication.isPresent()) {
//         evaluation.setLoanApplication(optionalApplication.get());
//         // evaluationDate is handled by @PrePersist in the entity
//         return creditEvaluationRepository.save(evaluation);
//     }
//     throw new IllegalArgumentException("Loan Application with ID " + applicationId + " not found.");
// }
//
// public Optional<CreditEvaluation> getLatestCreditEvaluationForApplication(Long applicationId) {
//     return creditEvaluationRepository.findTopByLoanApplication_ApplicationIdOrderByEvaluationDateDesc(applicationId);
// }
//}

//package com.example.demo.service;
//
//
//
//import com.example.demo.model.LoanApplication;
//
//import com.example.demo.repository.LoanApplicationRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LoanApplicationService {
//
//    private final LoanApplicationRepository loanApplicationRepository;
//
//    @Autowired
//    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository) {
//        this.loanApplicationRepository = loanApplicationRepository;
//    }
//
//    public List<LoanApplication> getAllLoanApplications() {
//        return loanApplicationRepository.findAll();
//    }
//
//    public Optional<LoanApplication> getLoanApplicationById(Long id) {
//        return loanApplicationRepository.findById(id);
//    }
//
//    public LoanApplication saveLoanApplication(LoanApplication loanApplication) {
//        return loanApplicationRepository.save(loanApplication);
//    }
//
//    public void deleteLoanApplication(Long id) {
//        loanApplicationRepository.deleteById(id);
//    }
//}

package com.example.demo.service;

import com.example.demo.model.LoanApplication;
import com.example.demo.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    @Autowired
    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
    }

    // This method retrieves ALL loan applications (might be used by an admin)
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    // NEW METHOD: This retrieves loan applications for a specific user's email
    public List<LoanApplication> getLoanApplicationsByContactEmail(String contactEmail) {
        return loanApplicationRepository.findByContactEmail(contactEmail);
    }

    public Optional<LoanApplication> getLoanApplicationById(Long id) {
        return loanApplicationRepository.findById(id);
    }

    @Transactional // Ensures the database operation is atomic
    public LoanApplication saveLoanApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    // Potentially more methods like updateLoanApplication, deleteLoanApplication, etc.
}