package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.DomainEntity;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.DomainEntityRepository;
@Service
public class DomainEntityService {
    @Autowired
    private DomainEntityRepository domainEntityRepository;
     @Autowired
    private AccountRepository accountRepository;
    public DomainEntity createDomain(DomainEntity domainEntity) {
        // Vous pouvez utiliser directement le domaineEntity reçu du contrôleur
        return domainEntityRepository.save(domainEntity);
    }
    
    public List<DomainEntity> getAllDomainEntities() {
        return domainEntityRepository.findAll();
    }
    
    public DomainEntity getDomainEntityById(Long id) {
        Optional<DomainEntity> domainEntity = domainEntityRepository.findById(id);
        return domainEntity.orElse(null);
    }
    
    public void deleteDomainEntity(DomainEntity domainEntity) {
        domainEntityRepository.delete(domainEntity);
    }

    
    public List<DomainEntity> getAllDomainEntitiesByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        return account != null ? new ArrayList<>(account.getDomains()) : null;
    }
    

}
