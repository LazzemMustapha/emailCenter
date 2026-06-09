package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
    List<Account> findByUserId(Integer userId);
    Account findByIdAndUserId(Long id, Integer userId);

}
