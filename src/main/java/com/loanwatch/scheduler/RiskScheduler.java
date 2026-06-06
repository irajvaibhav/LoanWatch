package com.loanwatch.scheduler;

import com.loanwatch.service.RiskScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RiskScheduler {

    @Autowired
    private RiskScoringService riskScoringService;

    // runs every night at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void runNightlyRiskScoring() {
        System.out.println("Nightly risk scoring started...");
        riskScoringService.calculateRiskForAll();
        System.out.println("Nightly risk scoring completed.");
    }
}