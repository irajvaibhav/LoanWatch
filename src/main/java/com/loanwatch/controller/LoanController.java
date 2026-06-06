package com.loanwatch.controller;

import com.loanwatch.model.Loan;
import com.loanwatch.service.LoanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@SecurityRequirement(name = "Bearer Authentication")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // add new loan
    @PostMapping("/loans")
    public Loan addLoan(@RequestBody Loan loan) {
        return loanService.addLoan(loan);
    }

    // get all loans of a borrower
    @GetMapping("/loans/borrower/{borrowerId}")
    public List<Loan> getLoansByBorrower(@PathVariable Long borrowerId) {
        return loanService.getLoansByBorrower(borrowerId);
    }

    // get one loan
    @GetMapping("/loans/{id}")
    public Loan getLoan(@PathVariable Long id) {
        return loanService.getLoanById(id);
    }

    // delete loan
    @DeleteMapping("/loans/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return "Loan deleted";
    }
}