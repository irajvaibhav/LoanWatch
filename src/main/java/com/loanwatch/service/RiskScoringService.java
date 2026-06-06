package com.loanwatch.service;

import com.loanwatch.model.Borrower;
import com.loanwatch.model.Loan;
import com.loanwatch.model.Payment;
import com.loanwatch.model.RiskScore;
import com.loanwatch.repository.BorrowerRepository;
import com.loanwatch.repository.LoanRepository;
import com.loanwatch.repository.PaymentRepository;
import com.loanwatch.repository.RiskScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskScoringService {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RiskScoreRepository riskScoreRepository;

    // calculate risk score for one borrower
    public RiskScore calculateRisk(Borrower borrower) {

        List<Loan> loans = loanRepository.findByBorrowerId(borrower.getId());

        int totalPaymentsDue = 0;
        int missedPayments = 0;
        long daysOverdue = 0;

        for (Loan loan : loans) {

            List<Payment> payments = paymentRepository.findByLoanId(loan.getId());
            totalPaymentsDue++;

            if (payments.isEmpty()) {
                missedPayments++;
            }

            // check if loan is overdue
            if (loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDate.now())) {
                long days = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
                daysOverdue = Math.max(daysOverdue, days);
            }
        }

        // factor 1 - payment history 35%
        double paymentScore = totalPaymentsDue == 0 ? 0 :
                ((double) missedPayments / totalPaymentsDue) * 100;

        // factor 2 - days overdue 25%
        double overdueScore = Math.min((daysOverdue / 90.0) * 100, 100);

        // factor 3 - missed payments 20%
        double missedScore = Math.min(missedPayments * 20, 100);

        // factor 4 - loan utilization 10%
        double utilization = borrower.getMonthlyIncome() == 0 ? 0 :
                (loans.size() * 1000.0) / (borrower.getMonthlyIncome() * 12);
        double utilizationScore = Math.min(utilization * 50, 100);

        // factor 5 - number of active loans 10%
        double loanCountScore = Math.min(loans.size() * 10, 100);

        // final weighted score
        double finalScore = (paymentScore * 0.35)
                + (overdueScore * 0.25)
                + (missedScore * 0.20)
                + (utilizationScore * 0.10)
                + (loanCountScore * 0.10);

        finalScore = Math.round(finalScore * 100.0) / 100.0;

        // decide risk level
        String riskLevel;
        if (finalScore >= 70) {
            riskLevel = "HIGH";
        } else if (finalScore >= 40) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        // save to database
        RiskScore riskScore = riskScoreRepository.findByBorrowerId(borrower.getId());
        if (riskScore == null) {
            riskScore = new RiskScore();
            riskScore.setBorrowerId(borrower.getId());
        }
        riskScore.setScore(finalScore);
        riskScore.setRiskLevel(riskLevel);
        riskScore.setCalculatedAt(LocalDateTime.now());

        return riskScoreRepository.save(riskScore);
    }

    // run for all borrowers
    public void calculateRiskForAll() {
        List<Borrower> allBorrowers = borrowerRepository.findAll();
        for (Borrower borrower : allBorrowers) {
            calculateRisk(borrower);
        }
    }

    // get risk score of one borrower
    @Cacheable (value = "riskScore", key = "#borrowerId")
    public RiskScore getRiskScore(Long borrowerId) {
        return riskScoreRepository.findByBorrowerId(borrowerId);
    }
}