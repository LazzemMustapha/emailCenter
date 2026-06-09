package com.example.demo.entitys;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter


@Entity

public class ArchivedEmail {
    @Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    private String sender;
    
	
    private String recipients;
    

    private String subject;
    @Lob
    private String body;
    private LocalDate date;
     @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;
    @ManyToOne
	private Account account;
	public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public List<Attachment> getAttachments() {
        return attachments;
    }
   
    public ArchivedEmail(String sender, String recipients, String subject, String body, LocalDate date, List<Attachment> attachments ,Account account) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.date = date;
        this.attachments = attachments;
        this.account=account;

       
    }
    public ArchivedEmail(){
        super();

    }

}
