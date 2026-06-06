package com.loanwatch.controller;

import com.loanwatch.model.Borrower;
import com.loanwatch.service.BorrowerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.loanwatch.service.RiskScoringService;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@SecurityRequirement(name = "Bearer Authentication")
public class BorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private RiskScoringService riskScoringService;

    // add new borrower
    @PostMapping("/borrowers")
    public Borrower addBorrower(@RequestBody Borrower borrower) {
        return borrowerService.addBorrower(borrower);
    }

    // get all borrowers
    @GetMapping("/borrowers")
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    // get one borrower
    @GetMapping("/borrowers/{id}")
    public Borrower getBorrower(@PathVariable Long id) {
        return borrowerService.getBorrowerById(id);
    }

    // delete borrower
    @DeleteMapping("/borrowers/{id}")
    public String deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return "Borrower deleted";

    }
    @PostMapping("/run-risk-engine")
    public String runRiskEngine() {
        riskScoringService.calculateRiskForAll();
        return "Risk engine ran successfully";
    }
}