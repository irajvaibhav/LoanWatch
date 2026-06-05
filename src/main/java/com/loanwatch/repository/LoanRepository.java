package com.loanwatch.repository;

import com.loanwatch.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // find all loans of a borrower
    List<Loan> findByBorrowerId(Long borrowerId);
}