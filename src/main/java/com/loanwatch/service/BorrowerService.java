package com.loanwatch.service;

import com.loanwatch.model.Borrower;
import com.loanwatch.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerService {

    @Autowired
    private BorrowerRepository borrowerRepository;

    // add a new borrower
    public Borrower addBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    // get all borrowers
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    // get one borrower by id
    public Borrower getBorrowerById(Long id) {
        return borrowerRepository.findById(id).orElse(null);
    }

    // get borrowers assigned to a specific agent
    public List<Borrower> getBorrowersByAgent(Long agentId) {
        return borrowerRepository.findByAssignedAgentId(agentId);
    }

    // delete a borrower
    public void deleteBorrower(Long id) {
        borrowerRepository.deleteById(id);
    }
}