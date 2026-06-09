package com.example.demo.repository;

import com.example.demo.entitys.ArchivingRule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ArchivingRuleRepository extends JpaRepository<ArchivingRule, Long> {
   public ArchivingRule findByaccountId(Long accountId);

public List<ArchivingRule> findByAccountId(Long accountId);
   
   
}
