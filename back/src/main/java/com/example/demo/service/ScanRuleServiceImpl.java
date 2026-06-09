package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.ScanRule;
import com.example.demo.repository.ScanRuleRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityNotFoundException;
@EnableScheduling
@Service
public class ScanRuleServiceImpl implements ScanRuleService {


    @Autowired
    private EmailService1 emailService;
    @Autowired
    private AccountService emailAccountService;
    @Autowired
    private ScanRuleRepository scanRuleRepository;

    //@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    /*@Scheduled(cron = " 1 18 19 * * *")
    public void scanEmails() {
        // Récupérer tous les comptes de messagerie de la base de données
        List<Account> emailAccounts = emailAccountService.getAllEmailAccountsFromDatabase();
        
        // Itérer sur chaque compte et appeler la méthode de récupération d'e-mails
        for (Account emailAccount : emailAccounts) {
            emailService.fetchAndSaveEmails(emailAccount.getId());
        }
    }*/

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void scanEmails() {
        LocalTime now = LocalTime.now();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();

        List<ScanRule> periods = scanRuleRepository.findAll();
        for(ScanRule scanRule : periods ) {
            if(scanRule.getHour() == currentHour && scanRule.getMinute() == currentMinute) {
                 // Récupérer tous les comptes de messagerie de la base de données
            List<Account> emailAccounts = emailAccountService.getAllEmailAccountsFromDatabase();
        
        // Itérer sur chaque compte et appeler la méthode de récupération d'e-mails
        for (Account emailAccount : emailAccounts) {
            emailService.fetchAndSaveEmails(emailAccount.getId());
        }

            }

        }
    }
    public ScanRule addScanRule(Integer hour, Integer minute) {
        ScanRule newRule = new ScanRule(hour, minute);
        return scanRuleRepository.save(newRule);
    }
    public ScanRule updateScanRule(Long id, Integer hour, Integer minute) {
        Optional<ScanRule> optionalScanRule = scanRuleRepository.findById(id);
        if (optionalScanRule.isPresent()) {
            ScanRule scanRule = optionalScanRule.get();
            scanRule.setHour(hour);
            scanRule.setMinute(minute);
            return scanRuleRepository.save(scanRule);
        } else {
            throw new EntityNotFoundException("ScanRule not found with id: " + id);
        }
    }
    public void deleteScanRule(Long id) {
        if (scanRuleRepository.existsById(id)) {
            scanRuleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("ScanRule not found with id: " + id);
        }
    }
    public List<ScanRule> getAllScanRules() {
        return scanRuleRepository.findAll();
    }


}
	

  






