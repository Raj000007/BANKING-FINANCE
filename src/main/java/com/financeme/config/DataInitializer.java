package com.financeme.config;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AccountRepository accountRepository) {
        return args -> {
            accountRepository.save(new Account("123456789", "John Doe", 1000.00));
            accountRepository.save(new Account("987654321", "Jane Doe", 1500.00));
        };
    }
}
