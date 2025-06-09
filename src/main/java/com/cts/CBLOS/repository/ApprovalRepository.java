package com.cts.CBLOS.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.CBLOS.model.Approval;
import com.cts.CBLOS.repository.ApprovalRepository;
import java.time.LocalDate; // Make sure this is imported for LocalDate.now()
import com.cts.CBLOS.model.Approval;
 
@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer>{
}