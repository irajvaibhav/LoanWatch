package com.loanwatch.repository;

import com.loanwatch.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // find all payments of a loan
    List<Payment> findByLoanId(Long loanId);
}