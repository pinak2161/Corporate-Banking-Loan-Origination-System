package com.cts.CBLOS.repository;

import com.cts.CBLOS.model.LoanApplication;
import com.cts.CBLOS.model.LoanApplication.LoanApplicationStatus;
import com.cts.CBLOS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {

    // Find applications by the user associated with them
    List<LoanApplication> findByUser_UserId(Integer userId); // <--- Add this line!

    // Find applications by user object (alternative, but by ID is more common)
    List<LoanApplication> findByUser(User user);

    // Find by status
    List<LoanApplication> findByStatus(LoanApplication.LoanApplicationStatus status);

    long countByStatusIn(Collection<LoanApplicationStatus> statuses);    // Other methods...
    Optional<LoanApplication> findById(Integer applicationId); // Already exists, but for clarity
}