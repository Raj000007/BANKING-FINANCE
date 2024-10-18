package com.financeme.controller;

import com.financeme.model.Account;
import com.financeme.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        Account account1 = new Account("1001", "John Doe", 5000.00);
        Account account2 = new Account("1002", "Jane Smith", 3000.00);
        
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));
        
        assertEquals(2, accountController.getAllAccounts().size());
        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    public void testGetAccountById() {
        Account account = new Account("1001", "John Doe", 5000.00);
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        ResponseEntity<Account> response = accountController.getAccountById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getAccountHolderName());
        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void testGetAccountByIdNotFound() {
        when(accountService.getAccountById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Account> response = accountController.getAccountById(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account("1001", "John Doe", 5000.00);
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        Account createdAccount = accountController.createAccount(account);
        
        assertNotNull(createdAccount);
        assertEquals("John Doe", createdAccount.getAccountHolderName());
        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    public void testDeleteAccount() {
        doNothing().when(accountService).deleteAccount(1L);
        
        ResponseEntity<Void> response = accountController.deleteAccount(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService, times(1)).deleteAccount(1L);
    }
}
