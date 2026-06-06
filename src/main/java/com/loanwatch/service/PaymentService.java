package com.loanwatch.service;

import com.loanwatch.model.Payment;
import com.loanwatch.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // add a new payment
    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // get all payments of a loan
    public List<Payment> getPaymentsByLoan(Long loanId) {
        return paymentRepository.findByLoanId(loanId);
    }
}