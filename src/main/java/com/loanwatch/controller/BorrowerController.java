package com.loanwatch.controller;

import com.loanwatch.model.Borrower;
import com.loanwatch.model.RiskScore;
import com.loanwatch.repository.RiskScoreRepository;
import com.loanwatch.service.BorrowerService;
import com.loanwatch.service.RiskScoringService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@SecurityRequirement(name = "Bearer Authentication")
public class BorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private RiskScoringService riskScoringService;

    @Autowired
    private RiskScoreRepository riskScoreRepository;

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

    // assign agent to borrower
    @PutMapping("/borrowers/{id}/assign-agent")
    public Borrower assignAgent(@PathVariable Long id, @RequestParam Long agentId) {
        return borrowerService.assignAgent(id, agentId);
    }

    // run risk engine manually
    @PostMapping("/run-risk-engine")
    public String runRiskEngine() {
        riskScoringService.calculateRiskForAll();
        return "Risk engine ran successfully";
    }

    // get risk score of a borrower
    @GetMapping("/borrowers/{id}/risk")
    public RiskScore getRiskScore(@PathVariable Long id) {
        return riskScoringService.getRiskScore(id);
    }

    // get all risk scores in one call
    @GetMapping("/risk-scores")
    public List<RiskScore> getAllRiskScores() {
        return riskScoreRepository.findAll();
    }
}