package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entitys.ScanRule;
import com.example.demo.service.ScanRuleService;



@RestController
@RequestMapping("/api/scan-rules")
public class ScanRuleController {
	@Autowired
    ScanRuleService scanRuleService;
    @PostMapping
    public ResponseEntity<ScanRule> addScanRule(@RequestBody ScanRule scanRule) {
        ScanRule newScanRule = scanRuleService.addScanRule(scanRule.getHour(), scanRule.getMinute());
        return ResponseEntity.ok(newScanRule);
    }
      @PutMapping("/{id}")
    public ResponseEntity<ScanRule> updateScanRule(
            @PathVariable Long id,
            @RequestBody ScanRule scanRule) {
        ScanRule updatedScanRule = scanRuleService.updateScanRule(id, scanRule.getHour(), scanRule.getMinute());
        return ResponseEntity.ok(updatedScanRule);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScanRule(@PathVariable Long id) {
        scanRuleService.deleteScanRule(id);
        return ResponseEntity.noContent().build();
    }
     @GetMapping
    public ResponseEntity<List<ScanRule>> getAllScanRules() {
        List<ScanRule> scanRules = scanRuleService.getAllScanRules();
        return new ResponseEntity<>(scanRules, HttpStatus.OK);
    }
}