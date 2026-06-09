package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Account;
import com.example.demo.entitys.ArchivedEmail;
import com.example.demo.entitys.Attachment;
import com.example.demo.entitys.DomainEntity;
import com.example.demo.entitys.Email;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ArchivedEmailRepository;
import com.example.demo.repository.DomainEntityRepository;
import com.example.demo.repository.EmailRepository;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Service
public class EmailService1 {
    
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private AccountService emailAccountService;
    @Autowired
    private ArchivedEmailRepository archivedEmailRepository;
    @Autowired
    private DomainEntityRepository domainEntityRepository;
    @Autowired
    private AccountRepository accountRepository;


    public List<Email> getEmailsByDomain(String domainName,Long accountId) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        }
        return emailRepository.findBySenderContainingIgnoreCaseAndAccountId(domainName,accountId);
    }
    
    public void fetchAndSaveEmails(Long emailAccountId) {
        // Retrieve the email account information from the database
        Account emailAccount = emailAccountService.findById(emailAccountId);
        if (emailAccount == null) {
            throw new IllegalArgumentException("Email account not found with ID: " + emailAccountId);
        }
    
        // Configure properties for IMAP access
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", emailAccount.getServeur());
        properties.put("mail.imaps.port", emailAccount.getPort());
        properties.put("mail.imaps.starttls.enable", "true");
    
        try {
            // Create a session with the configured properties
            Session session = Session.getInstance(properties);
            Store store = session.getStore("imaps");
            store.connect(emailAccount.getEmail(), emailAccount.getPassword());
    
            // Open the INBOX folder in read-only mode
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
    
            // Determine the date to fetch emails from
            LocalDate referenceDate = emailAccount.getLastFetchDate();
            if (referenceDate == null) {
                // If no last fetch date, fetch all emails
                referenceDate = LocalDate.of(1970, 1, 1);
            }
            Date fromDate = Date.from(referenceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    
            // Search for messages after the reference date
            ReceivedDateTerm receivedDateTerm = new ReceivedDateTerm(javax.mail.search.ComparisonTerm.GT, fromDate);
            Message[] messages = inbox.search(receivedDateTerm);
    
            // Process the messages
            for (Message message : messages) {
                saveMessageToDatabase(message, emailAccount);
                saveDomaineToDatabase(message, emailAccount);
            }
    
            // Update the last fetch date
            if (messages.length > 0) {
                Message latestMessage = messages[messages.length - 1];
                LocalDate latestMessageDate = latestMessage.getReceivedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                emailAccount.setLastFetchDate(latestMessageDate);
                accountRepository.save(emailAccount); // Save the updated account with the new last fetch date
                System.out.println("Updated last fetch date to: " + latestMessageDate);
            } else {
                System.out.println("No new messages found.");
            }
    
            // Close the inbox folder and store
            inbox.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Transactional
    private void saveDomaineToDatabase(Message message, Account emailAccount) {
        try {
            String sender = ((InternetAddress) message.getFrom()[0]).getAddress();
            String domainName = extractDomain(sender);

            // Vérifier si le domaine existe déjà dans la base de données
            DomainEntity existingDomain = domainEntityRepository.findByDomainName(domainName);
            if (existingDomain == null) {
                // Le domaine n'existe pas encore, donc nous pouvons l'ajouter à la base de données
                existingDomain = new DomainEntity();
                existingDomain.setDomainName(domainName);
                domainEntityRepository.save(existingDomain);
            }

            // Ajouter le domaine à l'utilisateur si ce n'est pas déjà fait
            if (!emailAccount.getDomains().contains(existingDomain)) {
                emailAccount.getDomains().add(existingDomain);
                accountRepository.save(emailAccount);  // Sauvegarder les modifications dans l'utilisateur
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    private String extractDomain(String email) {
        // Extract the domain from the email address
        String[] parts = email.split("@");
        return parts.length == 2 ? parts[1] : null;
    }
    private String extractTextFromHtml(String htmlContent) {
        Document doc = Jsoup.parseBodyFragment(htmlContent);
        return doc.text();
    }
    private String extractTextWithCss(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
    
        // Sélectionnez les éléments de texte (en excluant les balises de script et de style)
        Elements textElements = doc.select(":not(script):not(style)");
    
        StringBuilder sb = new StringBuilder();
        for (Element element : textElements) {
            // Obtenez le texte de cet élément et ajoutez-le au StringBuilder
            sb.append(element.text()).append(" ");
        }
    
        return sb.toString().trim(); // Supprimer les espaces blancs inutiles à la fin
    }
    
    private void saveMessageToDatabase(Message message, Account account) {
        try {
            // Extraire les informations de l'e-mail
            String subject = message.getSubject();
            String sender = ((InternetAddress) message.getFrom()[0]).getAddress();

            // Extraire la date du message
            LocalDate date = null;
            Date sentDate = message.getSentDate();
            if (sentDate != null) {
                date = sentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            // Vérifier si un e-mail avec le même sujet et la même date existe déjà dans la base de données
            List<Email> existingEmail = emailRepository.findBySubjectAndDate(subject, date);
            if (existingEmail != null && !existingEmail.isEmpty()) {
                // Si un e-mail existe déjà avec le même sujet et la même date, ne rien faire
                return;
            }
       
        // Extraire le corps du message
        StringBuilder bodyBuilder = new StringBuilder();
        List<Attachment> attachments = new ArrayList<>();
        Object content = message.getContent();
        if (content instanceof String) {
            bodyBuilder.append((String) content);
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                   try {
                    String folderPath = subject + date.toString();
                    String path = saveAttachment(bodyPart, folderPath);
                    attachments.add(new Attachment(path));
                   }catch(IOException e) {

                   }catch(MessagingException ef){

                   }
                } else if(bodyPart.getContentType().contains("TEXT/HTML")){
                    bodyBuilder.append(bodyPart.getContent().toString()); 
                }
            }
        }

        // Convertir les builders en chaînes de caractères
        String body = bodyBuilder.toString();

            // Extraire les destinataires
            String recipients = getAddressString(message.getAllRecipients());

            // Extraire les pièces jointes
           /*  String attachments = "";
            if (message instanceof MimeMessage) {
                MimeMessage mimeMessage = (MimeMessage) message;
                if (mimeMessage.getContent() instanceof Multipart) {
                    Multipart multipart = (Multipart) mimeMessage.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            attachments += bodyPart.getFileName() + ", ";
                        }
                    }
                    // Supprimer la virgule et l'espace à la fin
                    attachments = attachments.substring(0, attachments.length() - 2);
                }
            }*/

            // Créer une entité Email et la sauvegarder dans la base de données
            Email email = new Email();
            email.setAccount(account);
            email.setSubject(subject);
            email.setSender(sender);
            email.setBody(body);
            email.setDate(date);
            email.setRecipients(recipients);
            email.setAttachments(attachments);

            // Enregistrez maintenant l'e-mail dans votre base de données
            emailRepository.save(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveAttachment(BodyPart bodyPart, String folderPath) throws IOException, MessagingException {
        String destDir = "attachments/"+folderPath+"/";
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = bodyPart.getFileName();
        File file = new File(destDir + File.separator + fileName);
        try (FileOutputStream output = new FileOutputStream(file)) {
            ((MimeBodyPart) bodyPart).saveFile(file);
        }
        System.out.println("Saved attachment: " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }
    
    // Méthode utilitaire pour convertir Address[] en String
    private String getAddressString(Address[] addresses) {
        if (addresses == null || addresses.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Address address : addresses) {
            if (address instanceof InternetAddress) {
                InternetAddress internetAddress = (InternetAddress) address;
                stringBuilder.append(internetAddress.getAddress()).append(", ");
            }
        }
        String result = stringBuilder.toString();
        // Supprimer la virgule et l'espace à la fin
        return result.substring(0, result.length() - 2);
    }
    
    public List<Email> searchEmails(Long accountId, String sender, String subject, LocalDate startDate, LocalDate endDate) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        }
        // Vérifiez si les paramètres sont nuls et ajustez-les si nécessaire
        if (sender == null) sender = "";
        if (subject == null) subject = "";
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
    
        // Appelez la méthode de recherche dans le repository avec les paramètres ajustés
        return emailRepository.findBySenderContainingIgnoreCaseAndSubjectContainingIgnoreCaseAndDateBetweenAndAccount(
                sender, subject, startDate, endDate,account);
    }
    public List<Email> searchByEmail(String email, Long accountId) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        }
        return emailRepository.findBySenderAndAccountId(email,accountId);
    }
    public void deleteEmail(Long emailId, Long accountId) {
        // Recherche de l'e-mail dans la base de données par son ID
        Optional<Email> optionalEmail = emailRepository.findById(emailId);
        if (optionalEmail.isPresent()) {
            Email email = optionalEmail.get();
            // Supprimer l'e-mail de la base de données
            emailRepository.delete(email);
        } else {
            // Gérer le cas où l'e-mail avec cet ID n'existe pas
            throw new IllegalArgumentException("Email not found with ID: " + emailId);
        }
        
    }
    public List<Email> getEmailsByAccountId(Long accountId) {
        return emailRepository.findByAccountId(accountId);
    }
    
    public void archiveEmail(Long emailId, Long accountId) {
        try {
            // Rechercher l'email dans la base de données
            Email email = emailRepository.findByIdAndAccountId(emailId, accountId);
            if (email == null) {
                throw new IllegalArgumentException("Email not found for id: " + emailId);
            }
    
            List<Attachment> attatchmentss = new ArrayList<Attachment>();
            attatchmentss.addAll(email.getAttachments());
            
            // Créer une instance d'ArchivedEmail à partir de l'email à archiver
            ArchivedEmail archivedEmail = new ArchivedEmail(
                email.getSender(),
                email.getRecipients(),
                email.getSubject(),
                email.getBody(),
                email.getDate(),
                attatchmentss,
                email.getAccount()
            );
    
            // Enregistrer l'ArchivedEmail dans la table correspondante
            archivedEmailRepository.save(archivedEmail);
    
            // Supprimer l'email de la table Email
            emailRepository.delete(email);
    
        } catch (IllegalArgumentException e) {
            // Rejeter les exceptions spécifiques
            throw e;
        } catch (Exception e) {
            // Gérer les autres exceptions et imprimer la trace
            e.printStackTrace();
            throw new RuntimeException("Failed to archive email", e);
        }
    }
    
    public Email getEmailById(Long emailId) {
        Optional<Email> email = emailRepository.findById(emailId);
        return email.orElse(null);
    }


    
   
  
    
}
