package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.ArchivedEmail;
import com.example.demo.entitys.ArchivingRule;
import com.example.demo.entitys.Email;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ArchivedEmailRepository;
import com.example.demo.repository.ArchivingRuleRepository;
import com.example.demo.repository.EmailRepository;
@EnableScheduling
@Service
public class ArchivedEmailServiceImpl implements ArchivedEmailService {
    
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ArchivedEmailRepository archivedEmailRepository;

    @Autowired
    private ArchivingRuleRepository archivingRuleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Scheduled(cron = "@daily")
    public void archiverEmailsSelonRegles() {
        List<ArchivingRule> rules = archivingRuleRepository.findAll();
            for (ArchivingRule rule : rules) {
                switch (rule.getRetentionPeriod()) {
                    case DAILY:
                            archiverEmails();
                        break;
                        case WEEKLY:
                            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
                            if(dayOfWeek == DayOfWeek.MONDAY){
                                archiverEmails();
                            }
                        break;
                        case MONTHLY:
                            Integer dayOfMonth = LocalDate.now().getDayOfMonth();
                            if(dayOfMonth == 1){
                                archiverEmails();
                            }
                        break;
                
                    default:
                        break;
                }    
            }
       
    }

    private void archiverEmails() {
        List<Email> emails = emailRepository.findAll();
        for (Email email : emails) {
            Long accountId = email.getAccount().getId();
            LocalDate dateLimite = LocalDate.now().minus(1, ChronoUnit.YEARS);
            //LocalDate dateLimite = LocalDate.now().minus(12, ChronoUnit.DAYS);
            List<Email> emailsAarchiver = emailRepository.findByDateBeforeAndAccountId(dateLimite, accountId);
            for (Email emailAarchiver : emailsAarchiver) {
                ArchivedEmail ar = new ArchivedEmail(
                    emailAarchiver.getSender(),
                    emailAarchiver.getRecipients(),
                    emailAarchiver.getSubject(),
                    emailAarchiver.getBody(),
                    emailAarchiver.getDate(),
                    emailAarchiver.getAttachments(),
                    emailAarchiver.getAccount()
                );
                archivedEmailRepository.save(ar);
                emailRepository.delete(emailAarchiver);
            }
        }
    }

    @Override
    public List<ArchivedEmail> getAllArchivedEmails(Long accountId) {
        return archivedEmailRepository.findByAccountId(accountId);
    }

    @Override
    public List<ArchivedEmail> getArchivedEmailsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate) {
        return archivedEmailRepository.findByDateBetweenAndAccountId(startDate, endDate, accountId);
    }

    @Override
    public List<ArchivedEmail> getArchivedEmailsBySenderAndDateRange(Long accountId, String sender, LocalDate startDate, LocalDate endDate) {
        return archivedEmailRepository.findBySenderAndDateBetweenAndAccountId(sender, startDate, endDate, accountId);
    }

    @Override
    public ArchivedEmail getArchivedEmailById(Long accountId, Long id) {
        return archivedEmailRepository.findByIdAndAccountId(id, accountId).orElse(null);
    }

    @Override
    public void deleteArchivedEmail(Long accountId, ArchivedEmail archivedEmail) {
        if (archivedEmail.getAccount().getId().equals(accountId)) {
            archivedEmailRepository.delete(archivedEmail);
        }
    }

    @Override
    @Transactional
    public void deleteArchivedEmailsOlderThanOneYear(Long accountId) {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        archivedEmailRepository.deleteAllByDateBeforeAndAccountId(oneYearAgo, accountId);
    }

    private static final Logger logger = LoggerFactory.getLogger(ArchivedEmailService.class);

    

    public void unarchiveEmail(Long archivedEmailId, Long accountId) {
        logger.debug("Unarchiving email ID: {} for account ID: {}", archivedEmailId, accountId);

        ArchivedEmail archivedEmail = archivedEmailRepository.findById(archivedEmailId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid archived email ID: " + archivedEmailId));
        
        Email email = new Email();
        email.setBody(archivedEmail.getBody());
        email.setDate(archivedEmail.getDate());
        email.setRecipients(archivedEmail.getRecipients());
        email.setSender(archivedEmail.getSender());
        email.setSubject(archivedEmail.getSubject());

        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid account ID: " + accountId));
        email.setAccount(account);

        logger.debug("Saving unarchived email with account ID: {}", email.getAccount().getId());

        emailRepository.save(email);
        archivedEmailRepository.delete(archivedEmail);
    }


    public ArchivedEmail getArchivedEmailById(Long emailId) {
        Optional<ArchivedEmail> archivedEmail = archivedEmailRepository.findById(emailId);
        return archivedEmail.orElse(null);
    }
}



