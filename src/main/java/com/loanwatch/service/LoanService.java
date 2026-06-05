package com.loanwatch.service;

import com.loanwatch.model.Loan;
import com.loanwatch.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    // add a new loan
    public Loan addLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // get all loans of a borrower
    public List<Loan> getLoansByBorrower(Long borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId);
    }

    // get one loan by id
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    // delete a loan
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
}