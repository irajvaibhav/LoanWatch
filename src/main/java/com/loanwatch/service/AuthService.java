package com.loanwatch.service;

import com.loanwatch.model.User;
import com.loanwatch.repository.UserRepository;
import com.loanwatch.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // register a new user
    public String register(String name, String email, String password, String role) {

        // check if email already exists
        User existing = userRepository.findByEmail(email);
        if (existing != null) {
            return "Email already registered";
        }

        // create new user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        // encrypt password before saving
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
        return "User registered successfully";
    }

    // login and return token
    public String login(String email, String password) {

        // find user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "User not found";
        }

        // check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Wrong password";
        }

        // generate and return JWT token
        return jwtUtil.generateToken(email, user.getRole());
    }
}