package com.example.demo.entitys;




import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class Account {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	private String password;
	private String Email;
	
	private String port;
	private String serveur;
	private LocalDate lastFetchDate;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LocalDate getLastFetchDate() {
		return lastFetchDate;
	}
	
	public void setLastFetchDate(LocalDate lastFetchDate) {
		this.lastFetchDate = lastFetchDate;
	}
	@ManyToOne//optional = false
     @JsonIgnoreProperties("accounts")
    private User user;
	/*@OneToOne
	private ArchivingRule archivingRule;
	@OneToOne
	private ScanRule scanRule;*/
	 @ManyToMany
    @JoinTable(
        name = "account_domain",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
	@JsonIgnore
    private Set<DomainEntity> domains = new HashSet<>();

	 

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServeur() {
		return serveur;
	}
	public void setServeur(String serveur) {
		this.serveur = serveur;
	}
	public Account(Long id, String password, String Email, String port, String serveur,User user) {
		super();
		this.id = id;
		this.password = password;
		this.Email = Email;
		
		this.port = port;
		this.serveur = serveur;
		this.user = user;
	}
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}
   
	

}
