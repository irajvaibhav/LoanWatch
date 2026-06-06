package com.loanwatch.config;

import com.loanwatch.model.Borrower;
import com.loanwatch.model.Loan;
import com.loanwatch.repository.BorrowerRepository;
import com.loanwatch.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public void run(String... args) throws Exception {

        // only load data if database is empty
        if (borrowerRepository.count() > 0) {
            System.out.println("Data already loaded, skipping...");
            return;
        }

        System.out.println("Loading Kaggle dataset...");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/Loan_Default.csv")
                )
        );

        String line;
        int count = 0;

        // skip header line
        reader.readLine();

        while ((line = reader.readLine()) != null && count < 100) {

            try {
                String[] col = line.split(",");

                // create borrower from CSV data
                Borrower borrower = new Borrower();
                borrower.setName("Borrower " + col[0].trim());
                borrower.setPhone("99000" + count);
                borrower.setAddress("Region " + col[31].trim());

                // income column
                String incomeStr = col[23].trim();
                double income = incomeStr.isEmpty() ? 5000 : Double.parseDouble(incomeStr);
                borrower.setMonthlyIncome(income / 12);
                borrower.setAssignedAgentId(1L);

                borrower = borrowerRepository.save(borrower);

                // create loan from CSV data
                Loan loan = new Loan();
                loan.setBorrowerId(borrower.getId());

                String amountStr = col[10].trim();
                double amount = amountStr.isEmpty() ? 10000 : Double.parseDouble(amountStr);
                loan.setAmount(amount);

                loan.setDisbursedDate(LocalDate.now().minusMonths(6));
                loan.setDueDate(LocalDate.now().minusDays(count * 3));

                // status column
                String status = col[32].trim();
                loan.setStatus(status.equals("1") ? "DEFAULTED" : "ACTIVE");

                loanRepository.save(loan);
                count++;

            } catch (Exception e) {
                // skip bad rows
                continue;
            }
        }

        reader.close();
        System.out.println("Loaded " + count + " borrowers from Kaggle dataset!");
    }
}