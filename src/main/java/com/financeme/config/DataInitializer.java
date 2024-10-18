package com.financeme.config;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            accountRepository.save(new Account("1001", "John Doe", 5000.00));
            accountRepository.save(new Account("1002", "Jane Smith", 3000.00));
            accountRepository.save(new Account("1003", "Alice Brown", 12000.00));
        };
    }
}
