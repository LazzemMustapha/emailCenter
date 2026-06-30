package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entitys.ArchivingRule;
import com.example.demo.service.ArchivingRuleService;


@RestController
@RequestMapping("/archivingRules")
public class ArchivingRuleController {

    @Autowired
    private ArchivingRuleService archivingRuleService;

    @PostMapping
    public ResponseEntity<ArchivingRule> addArchivingRule(@RequestBody ArchivingRule archivingRule) {
        ArchivingRule newArchivingRule = archivingRuleService.addArchivingRule(archivingRule);
        return ResponseEntity.ok(newArchivingRule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArchivingRule> updateArchivingRule(@PathVariable Long id, @RequestBody ArchivingRule archivingRuleDetails) {
        ArchivingRule updatedArchivingRule = archivingRuleService.updateArchivingRule(id, archivingRuleDetails);
        return ResponseEntity.ok(updatedArchivingRule);
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ArchivingRule>> getAllArchivingRulesByAccountId(@PathVariable Long accountId) {
        List<ArchivingRule> archivingRules = archivingRuleService.getAllArchivingRules(accountId);
        return ResponseEntity.ok(archivingRules);
    }
}
