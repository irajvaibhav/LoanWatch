package com.loanwatch.repository;

import com.loanwatch.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    // find all borrowers assigned to a specific agent
    List<Borrower> findByAssignedAgentId(Long agentId);
}