package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.ArchivedEmail;


@Repository
public interface ArchivedEmailRepository extends JpaRepository<ArchivedEmail,Long>{
     List<ArchivedEmail> findByAccountId(Long accountId);
    List<ArchivedEmail> findByDateBetweenAndAccountId(LocalDate startDate, LocalDate endDate, Long accountId);
    List<ArchivedEmail> findBySenderAndDateBetweenAndAccountId(String sender, LocalDate startDate, LocalDate endDate, Long accountId);
    void deleteAllByDateBeforeAndAccountId(LocalDate date, Long accountId);
    java.util.Optional<ArchivedEmail> findByIdAndAccountId(Long id, Long accountId);
   

}
