package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.DomainEntity;
import com.example.demo.service.DomainEntityService;

@CrossOrigin(origins = "http://localhost:8088")
@RestController
@RequestMapping("/api/v1/auth")
public class DomainEntityController {
    @Autowired
    private DomainEntityService domainEntityService;

    @PostMapping("/domains")
    public DomainEntity createDomain(@RequestBody DomainEntity domainEntity) {
        return domainEntityService.createDomain(domainEntity);
    }
     @GetMapping("/allDomain")
    public ResponseEntity<List<DomainEntity>> getAllDomainEntities() {
        List<DomainEntity> domainEntities = domainEntityService.getAllDomainEntities();
        return ResponseEntity.ok(domainEntities);
    }
     @DeleteMapping("/domain/{id}")
    public ResponseEntity<String> deleteDomainEntity(@PathVariable Long id) {
        DomainEntity domainEntity = domainEntityService.getDomainEntityById(id);
        if (domainEntity != null) {
            domainEntityService.deleteDomainEntity(domainEntity);
            return ResponseEntity.ok("DomainEntity with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{accountId}/domain-entities")
    public ResponseEntity<List<DomainEntity>> getAllDomainEntitiesByAccountId(@PathVariable Long accountId) {
        List<DomainEntity> domainEntities = domainEntityService.getAllDomainEntitiesByAccountId(accountId);
        return ResponseEntity.ok(domainEntities);
    }
}
