package com.financeme.service;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    public AccountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        Account account1 = new Account("1001", "John Doe", 5000.00);
        Account account2 = new Account("1002", "Jane Smith", 3000.00);
        
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));
        
        assertEquals(2, accountService.getAllAccounts().size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void testGetAccountById() {
        Account account = new Account("1001", "John Doe", 5000.00);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> foundAccount = accountService.getAccountById(1L);
        
        assertTrue(foundAccount.isPresent());
        assertEquals("John Doe", foundAccount.get().getAccountHolderName());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account("1001", "John Doe", 5000.00);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);
        
        assertNotNull(createdAccount);
        assertEquals("John Doe", createdAccount.getAccountHolderName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> accountService.deleteAccount(1L));
        verify(accountRepository, times(1)).deleteById(1L);
    }
}
