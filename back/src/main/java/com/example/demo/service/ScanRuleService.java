package com.example.demo.service;

import java.util.List;

import com.example.demo.entitys.ScanRule;

public interface ScanRuleService {
	
public void scanEmails();
public ScanRule addScanRule(Integer hour, Integer minute);
public ScanRule updateScanRule(Long id, Integer hour, Integer minute) ;
public void deleteScanRule(Long id) ;
public List<ScanRule> getAllScanRules();

}
