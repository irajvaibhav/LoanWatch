package com.loanwatch.controller;

import com.loanwatch.model.Borrower;
import com.loanwatch.model.RiskScore;
import com.loanwatch.service.BorrowerService;
import com.loanwatch.service.RiskScoringService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@SecurityRequirement(name = "Bearer Authentication")
public class AgentController {

    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private RiskScoringService riskScoringService;

    // agent sees only their assigned borrowers
    @GetMapping("/my-borrowers/{agentId}")
    public List<Borrower> getMyBorrowers(@PathVariable Long agentId) {
        return borrowerService.getBorrowersByAgent(agentId);
    }

    // agent can see risk score of their borrower
    @GetMapping("/borrowers/{borrowerId}/risk")
    public RiskScore getRiskScore(@PathVariable Long borrowerId) {
        return riskScoringService.getRiskScore(borrowerId);
    }
}