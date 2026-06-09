package com.example.demo.repository;

import java.time.LocalDate;

import java.util.List;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email,Long>{
	
	List<Email> findBySubjectAndDate(String subject, LocalDate date);
	List<Email> findBySenderContainingIgnoreCaseAndSubjectContainingIgnoreCaseAndDateBetweenAndAccount(
		String sender, String subject, LocalDate startDate, LocalDate endDate, Account account);
	List<Email> findBySenderAndAccountId(String sender, Long accountId);
	//List<Email> findByDateBeforeAndArchivedFalse(LocalDate dateLimite);
	//@Query("UPDATE Email e SET e.archived = true WHERE e.date < ?1")
    //void archiveEmailsBeforeDate(LocalDate date);
	//List<Email> findByArchivedTrue();
	List<Email> findByDateBeforeAndAccountId(LocalDate date, Long accountId);

	List<Email> findByAccountId(Long accountId);
    Email findByIdAndAccountId(Long emailId, Long accountId);
	List<Email> findBySenderContainingIgnoreCaseAndAccountId(String domainName, Long accountId);
	Email getEmailById(Long emailId);
}

   
	
	
	


