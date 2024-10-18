package com.financeme.controller;

import com.financeme.model.Account;
import com.financeme.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/updateAccount/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @RequestBody Account accountDetails) {
        Account updatedAccount = accountService.updateAccount(accountNumber, accountDetails);
        return updatedAccount != null ? ResponseEntity.ok(updatedAccount) : ResponseEntity.notFound().build();
    }

    @GetMapping("/viewAccount/{accountNumber}")
    public ResponseEntity<Account> viewAccount(@PathVariable String accountNumber) {
        Account account = accountService.viewAccount(accountNumber);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteAccount/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
