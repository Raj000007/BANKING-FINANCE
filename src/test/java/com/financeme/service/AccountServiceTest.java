package com.financeme.service;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account("123456789", "John Doe", 1000.00);
    }

    @Test
    void createAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);
        assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void viewAccount() {
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(account);

        Account foundAccount = accountService.viewAccount(account.getAccountNumber());
        assertNotNull(foundAccount);
        assertEquals(account.getAccountHolderName(), foundAccount.getAccountHolderName());
        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    // Add more tests for update and delete as needed
}
