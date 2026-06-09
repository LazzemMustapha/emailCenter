package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Attachment;


@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long>{

}
