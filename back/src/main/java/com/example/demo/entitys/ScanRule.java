package com.example.demo.entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity

public class ScanRule  {

     
    @Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
    
    private Integer hour;
    private Integer minute;

  

    public ScanRule() {
        super();
    }

    public ScanRule(Integer hour, Integer minute) {
        this.hour = hour;
        this.minute = minute;
       
    }
	
}
