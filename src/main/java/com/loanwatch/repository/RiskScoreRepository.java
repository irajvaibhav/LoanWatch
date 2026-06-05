package com.loanwatch.repository;

import com.loanwatch.model.RiskScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskScoreRepository extends JpaRepository<RiskScore, Long> {

    // find risk score of a borrower
    RiskScore findByBorrowerId(Long borrowerId);
}