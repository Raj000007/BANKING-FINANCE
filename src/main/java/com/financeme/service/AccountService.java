package com.financeme.service;

import com.financeme.model.Account;
import com.financeme.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(String accountNumber, Account accountDetails) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.setAccountHolderName(accountDetails.getAccountHolderName());
            account.setBalance(accountDetails.getBalance());
            return accountRepository.save(account);
        }
        return null; // or throw an exception
    }

    public Account viewAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            accountRepository.delete(account);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
