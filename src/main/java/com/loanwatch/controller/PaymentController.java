package com.loanwatch.controller;

import com.loanwatch.model.Payment;
import com.loanwatch.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // record a new payment
    @PostMapping("/payments")
    public Payment addPayment(@RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }

    // get all payments of a loan
    @GetMapping("/payments/loan/{loanId}")
    public List<Payment> getPaymentsByLoan(@PathVariable Long loanId) {
        return paymentService.getPaymentsByLoan(loanId);
    }
}