package com.example.service;

import javax.security.auth.message.AuthException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private boolean validateAccount(Account a) {

        return !a.getUsername().isBlank() && a.getPassword().length() >= 4;
    }

    public Account registerUser(Account a) throws Exception {
        if (!validateAccount(a)) throw new Exception("Invalid Account Body");
        if (accountRepository.findByUsername(a.getUsername()) != null) throw new IllegalArgumentException("Username already taken.");
        return accountRepository.save(a);
    }

    public Account loginUser(Account a) throws AuthException {
        Account response = accountRepository.findByUsernameAndPassword(a.getUsername(), a.getPassword());
        if (response == null) throw new AuthException("Incorrect username or password");
        return response;
    }
}
