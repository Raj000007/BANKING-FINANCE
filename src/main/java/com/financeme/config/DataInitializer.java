package com.financeme.config;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        // Make sure these account numbers are unique
        accountRepository.save(new Account("ACC001", "John Doe", 5000.00));
        accountRepository.save(new Account("ACC002", "Jane Smith", 3000.00));
        // Add more accounts as needed
    }
}
