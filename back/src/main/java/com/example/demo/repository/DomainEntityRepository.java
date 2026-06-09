package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.DomainEntity;

@Repository

public interface DomainEntityRepository extends JpaRepository<DomainEntity,Long>{

    DomainEntity findByDomainName(String senderDomain);
    List<DomainEntity> findAllByAccountId(Long accountId);
      DomainEntity findByDomainNameAndAccount(String domainName, Account account);
     
    

}
