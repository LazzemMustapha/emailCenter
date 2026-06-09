package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Account save(Integer userId, Account account) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            account.setUser(userOptional.get());
            return accountRepo.save(account);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Override
    public Account findById(Long id) {
        return accountRepo.findById(id).orElse(null);
    }

    @Override
    public List<Account> getAllEmailAccountsFromDatabase() {
        return accountRepo.findAll();
    }

    @Override
    public Account updateAccount(Long accountId, Integer userId, Account updatedAccount) {
        Account existingAccount = getAccountByIdAndUserId(accountId, userId);
        if (existingAccount != null) {
            existingAccount.setPassword(updatedAccount.getPassword());
            existingAccount.setEmail(updatedAccount.getEmail());
            existingAccount.setPort(updatedAccount.getPort());
            existingAccount.setServeur(updatedAccount.getServeur());
            return accountRepo.save(existingAccount);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId + " and userId: " + userId);
        }
    }

    @Override
    public void delete(Long accountId, Integer userId) {
        Account existingAccount = getAccountByIdAndUserId(accountId, userId);
        if (existingAccount != null) {
            accountRepo.delete(existingAccount);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId + " and userId: " + userId);
        }
    }

    @Override
    public List<Account> getAccountsByUserId(Integer userId) {
        return accountRepo.findByUserId(userId);
    }

    @Override
    public Account getAccountByIdAndUserId(Long id, Integer userId) {
        return accountRepo.findByIdAndUserId(id, userId);
    }
}
