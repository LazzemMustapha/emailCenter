package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entitys.Account;
import com.example.demo.service.AccountService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Integer userId, @RequestBody Account account) {
        Account savedAccount = accountService.save(userId, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.findById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllEmailAccountsFromDatabase();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/update/{userId}/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer userId, @PathVariable Long id, @RequestBody Account updatedAccount) {
        try {
            Account updatedAccountResult = accountService.updateAccount(id, userId, updatedAccount);
            return ResponseEntity.ok(updatedAccountResult);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{userId}/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer userId, @PathVariable Long id) {
        try {
            accountService.delete(id, userId);
            return ResponseEntity.ok("Account with ID " + id + " and userId " + userId + " deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Integer userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}/account/{id}")
    public ResponseEntity<Account> getAccountByIdAndUserId(@PathVariable Integer userId, @PathVariable Long id) {
        Account account = accountService.getAccountByIdAndUserId(id, userId);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
