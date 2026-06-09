package com.example.demo.entitys;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Setter;
import lombok.Getter;


@Setter
@Getter
@Entity
public class ArchivingRule {
	
    @Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    private RetentionPeriodEnum retentionPeriod;

}
