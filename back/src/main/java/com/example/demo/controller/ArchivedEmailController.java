package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.ArchivedEmail;

import com.example.demo.service.ArchivedEmailService;

@RestController
@RequestMapping("/api/v1/auth/emails")
public class ArchivedEmailController {
     @Autowired
    private ArchivedEmailService archivedEmailService;
    private static final Logger logger = LoggerFactory.getLogger(ArchivedEmailController.class);

    @PostMapping("/archiver/emails")
    public ResponseEntity<String> archiverEmailsSelonRegles() {
        archivedEmailService.archiverEmailsSelonRegles();
        return new ResponseEntity<>("Archivage des e-mails déclenché avec succès.", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ArchivedEmail> getAllArchivedEmails(@RequestParam("accountId") Long accountId) {
        return archivedEmailService.getAllArchivedEmails(accountId);
    }

    @GetMapping("/archived-emails")
    public List<ArchivedEmail> getArchivedEmailsByDateRange(@RequestParam("accountId") Long accountId,
                                                            @RequestParam("startDate") String startDate,
                                                            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return archivedEmailService.getArchivedEmailsByDateRange(accountId, start, end);
    }

    @GetMapping("/archived-emails-by-sender")
    public List<ArchivedEmail> getArchivedEmailsBySenderAndDateRange(@RequestParam("accountId") Long accountId,
                                                                     @RequestParam("sender") String sender,
                                                                     @RequestParam("startDate") String startDate,
                                                                     @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return archivedEmailService.getArchivedEmailsBySenderAndDateRange(accountId, sender, start, end);
    }

    @DeleteMapping("/archived-emails/{accountId}/{id}")
    public ResponseEntity<String> deleteArchivedEmail(@PathVariable Long accountId, @PathVariable Long id) {
       ArchivedEmail archivedEmail = archivedEmailService.getArchivedEmailById(accountId, id);
        if (archivedEmail != null) {
            archivedEmailService.deleteArchivedEmail(accountId,archivedEmail);
            return new ResponseEntity<>("E-mail archivé supprimé avec succès.", HttpStatus.OK);
        }
        return new ResponseEntity<>("E-mail archivé non trouvé.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/archived-emails/delete-old")
    public ResponseEntity<String> deleteArchivedEmailsOlderThanOneYear(@RequestParam("accountId") Long accountId) {
        archivedEmailService.deleteArchivedEmailsOlderThanOneYear(accountId);
        return new ResponseEntity<>("E-mails archivés de plus d'un an supprimés avec succès.", HttpStatus.OK);
    }
    @PostMapping("/unarchive/{archivedEmailId}/{accountId}")
    public ResponseEntity<String> unarchiveEmail(@PathVariable Long archivedEmailId, @PathVariable Long accountId) {
        try {
            archivedEmailService.unarchiveEmail(archivedEmailId, accountId);
            logger.info("Email unarchived successfully. Account ID: {}", accountId);
            return ResponseEntity.ok("Email unarchived successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @GetMapping("/archive/{emailId}")
public ResponseEntity<ArchivedEmail> getArchivedEmailById(@PathVariable Long emailId) {
    ArchivedEmail archivedEmail = archivedEmailService.getArchivedEmailById(emailId);
    if (archivedEmail != null) {
        return ResponseEntity.ok(archivedEmail);
    } else {
        return ResponseEntity.notFound().build();
    }
}


}
