package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.Email;
import com.example.demo.service.EmailService1;

@RestController
@RequestMapping("/api/v1/auth/emails")
public class EmailController {

    @Autowired
    private EmailService1 emailService;

    @GetMapping("/fetch-and-save/{accountId}")
    public ResponseEntity<String> fetchAndSaveEmails(@PathVariable Long accountId) {
        emailService.fetchAndSaveEmails(accountId);
        return ResponseEntity.ok("Emails fetched and saved successfully.");
    }

    @GetMapping("/search/{accountId}")
    public List<Email> searchEmails(@RequestParam(required = false) String sender,
                                    @RequestParam(required = false) String subject,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    @PathVariable Long accountId) {
        return emailService.searchEmails(accountId, sender, subject, startDate, endDate);
    }

    @GetMapping("/search-by-email/{accountId}")
    public List<Email> searchByEmail(@RequestParam String email, @PathVariable Long accountId) {
        return emailService.searchByEmail(email, accountId);
    }

    @DeleteMapping("/{emailId}/{accountId}")
    public ResponseEntity<String> deleteEmail(@PathVariable Long emailId, @PathVariable Long accountId) {
        try {
            emailService.deleteEmail(emailId, accountId);
            return new ResponseEntity<>("Email deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/{accountId}")
    public List<Email> getAllEmailsByAccountId(@PathVariable Long accountId) {
        return emailService.getEmailsByAccountId(accountId);
    }

    @PutMapping("/archive/{emailId}/{accountId}")
    public ResponseEntity<String> archiveEmail(@PathVariable Long emailId, @PathVariable Long accountId) {
        try {
            emailService.archiveEmail(emailId, accountId);
            return new ResponseEntity<>("Email archived successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to archive email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search-by-domain/{accountId}")
    public List<Email> searchByDomain(@RequestParam String domainName, @PathVariable Long accountId) {
        return emailService.getEmailsByDomain(domainName, accountId);
    }

    @GetMapping("/{emailId}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long emailId) {
        Email email = emailService.getEmailById(emailId);
        if (email != null) {
            return ResponseEntity.ok(email);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
