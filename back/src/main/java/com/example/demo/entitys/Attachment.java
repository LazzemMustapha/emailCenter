package com.example.demo.entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Attachment {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;

    private String path;




	public Attachment() {
		super();
	}

	public Attachment(String path) {
		super();
		this.path = path;
	}

	public Attachment(Long id, String path) {
		super();
		this.id = id;
		this.path = path;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
}

