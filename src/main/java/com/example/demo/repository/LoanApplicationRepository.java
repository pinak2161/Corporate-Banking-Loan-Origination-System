//package com.example.demo.repository;
//
//
//
//import com.example.demo.model.LoanApplication;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
////No changes needed from previous version.
////Spring Data JPA automatically provides methods like save(), findById(), findAll(), delete()
//@Repository
//public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
// // You can add custom query methods here if needed, e.g.,
// // List<LoanApplication> findByCompanyName(String companyName);
// // List<LoanApplication> findByStatus(LoanApplication.ApplicationStatus status);
//}
package com.example.demo.repository;

import com.example.demo.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    // No changes needed
}