package com.financeme.controller;

import com.financeme.model.Account;
import com.financeme.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account("123456789", "John Doe", 1000.00);
    }

    @Test
    void createAccount() {
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        Account createdAccount = accountController.createAccount(account);
        assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber());
        verify(accountService).createAccount(any(Account.class));
    }

    @Test
    void viewAccount() {
        when(accountService.viewAccount(account.getAccountNumber())).thenReturn(account);

        Account foundAccount = accountController.viewAccount(account.getAccountNumber()).getBody();
        assertNotNull(foundAccount);
        assertEquals(account.getAccountHolderName(), foundAccount.getAccountHolderName());
        verify(accountService).viewAccount(account.getAccountNumber());
    }

    @Test
    void getAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account));

        List<Account> accounts = accountController.getAllAccounts();
        assertFalse(accounts.isEmpty());
        assertEquals(1, accounts.size());
        verify(accountService).getAllAccounts();
    }

    // Add more tests for update and delete as needed
}
