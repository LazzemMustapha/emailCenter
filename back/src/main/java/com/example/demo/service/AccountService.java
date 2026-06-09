package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.Account;

public interface AccountService {
	
	Account save(Integer userId, Account account);
    Account findById(Long id);
    List<Account> getAllEmailAccountsFromDatabase();
    Account updateAccount(Long accountId, Integer userId, Account updatedAccount);
    void delete(Long accountId, Integer userId);
    List<Account> getAccountsByUserId(Integer userId);
    Account getAccountByIdAndUserId(Long id, Integer userId);

}
