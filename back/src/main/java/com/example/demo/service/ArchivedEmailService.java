package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entitys.ArchivedEmail;

@Service
public interface ArchivedEmailService {
    public void archiverEmailsSelonRegles();
    public List<ArchivedEmail> getAllArchivedEmails(Long accountId);
    public List<ArchivedEmail> getArchivedEmailsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);
    public List<ArchivedEmail> getArchivedEmailsBySenderAndDateRange(Long accountId, String sender, LocalDate start, LocalDate end);
    public void deleteArchivedEmail(Long accountId, ArchivedEmail archivedEmail);
    public ArchivedEmail getArchivedEmailById(Long accountId, Long id);
    public void deleteArchivedEmailsOlderThanOneYear(Long accountId);
    public void unarchiveEmail(Long archivedEmailId, Long accountId);
    public ArchivedEmail getArchivedEmailById(Long emailId);

}
