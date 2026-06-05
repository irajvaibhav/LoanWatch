package com.loanwatch.repository;

import com.loanwatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // find user by email
    User findByEmail(String email);
}