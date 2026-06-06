package com.loanwatch.config;

import com.loanwatch.model.Borrower;
import com.loanwatch.model.Loan;
import com.loanwatch.model.User;
import com.loanwatch.repository.BorrowerRepository;
import com.loanwatch.repository.LoanRepository;
import com.loanwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // create demo account for recruiters if it doesn't exist
        if (userRepository.findByEmail("demo@loanwatch.com") == null) {
            User demo = new User();
            demo.setName("Demo Manager");
            demo.setEmail("demo@loanwatch.com");
            demo.setPassword(passwordEncoder.encode("demo123"));
            demo.setRole("MANAGER");
            userRepository.save(demo);
            System.out.println("Demo account created: demo@loanwatch.com / demo123");
        }

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

                Borrower borrower = new Borrower();
                borrower.setName("Borrower " + col[0].trim());
                borrower.setPhone("99000" + count);
                borrower.setAddress("Region " + col[31].trim());

                String incomeStr = col[23].trim();
                double income = incomeStr.isEmpty() ? 5000 : Double.parseDouble(incomeStr);
                borrower.setMonthlyIncome(income / 12);
                borrower.setAssignedAgentId(1L);

                borrower = borrowerRepository.save(borrower);

                Loan loan = new Loan();
                loan.setBorrowerId(borrower.getId());

                String amountStr = col[10].trim();
                double amount = amountStr.isEmpty() ? 10000 : Double.parseDouble(amountStr);
                loan.setAmount(amount);

                loan.setDisbursedDate(LocalDate.now().minusMonths(6));
                loan.setDueDate(LocalDate.now().minusDays(count * 3));

                String status = col[32].trim();
                loan.setStatus(status.equals("1") ? "DEFAULTED" : "ACTIVE");

                loanRepository.save(loan);
                count++;

            } catch (Exception e) {
                continue;
            }
        }

        reader.close();
        System.out.println("Loaded " + count + " borrowers from Kaggle dataset!");
    }
}