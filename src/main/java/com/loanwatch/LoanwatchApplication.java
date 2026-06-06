package com.loanwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class LoanwatchApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(LoanwatchApplication.class, args);
    }
}