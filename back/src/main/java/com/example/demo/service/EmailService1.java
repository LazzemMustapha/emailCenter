package com.example.demo.service;

import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.ComparisonTerm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
import java.util.Set;
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

    // ✅ SCAN AUTOMATIQUE TOUTES LES 3 MINUTES — tous les comptes
   @Autowired
private ConnectedUserService connectedUserService;

@Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
public void scanConnectedUsersEvery3Min() {
    Set<Integer> connectedUsers = connectedUserService.getConnectedUsers();

    if (connectedUsers.isEmpty()) return;

    for (Integer userId : connectedUsers) {
        List<Account> comptes = emailAccountService.getAccountsByUserId(userId);
        for (Account compte : comptes) {
            try {
                fetchAndSaveEmails(compte.getId());
            } catch (Exception e) {
                System.out.println("❌ Erreur fetch : " + e.getMessage());
            }
        }
    }
}
public List<Email> getAllEmailsByAccountId(Long accountId) {
    return emailRepository.findAllByAccountIdWithDetails(accountId);
}
    // ✅ FETCH PRINCIPAL — seulement les nouveaux emails des 3 derniers jours
    @Async
    public void fetchAndSaveEmails(Long emailAccountId) {
        Account emailAccount = emailAccountService.findById(emailAccountId);
        if (emailAccount == null) {
            throw new IllegalArgumentException("Email account not found with ID: " + emailAccountId);
        }

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", emailAccount.getServeur());
        properties.put("mail.imaps.port", emailAccount.getPort());
        properties.put("mail.imaps.starttls.enable", "true");

        Store store = null;
        Folder inbox = null;

        try {
            Session session = Session.getInstance(properties);
            store = session.getStore("imaps");
            store.connect(emailAccount.getEmail(), emailAccount.getPassword());

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // ✅ Filtre : seulement les 3 derniers jours
            Date dateLimite = Date.from(
                LocalDate.now().minusDays(3)
                         .atStartOfDay(ZoneId.systemDefault())
                         .toInstant()
            );
            ReceivedDateTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GE, dateLimite);
            Message[] messages = inbox.search(dateTerm);

            System.out.println("📬 Emails des 3 derniers jours pour compte "
                + emailAccount.getEmail() + " : " + messages.length);

            int nouveaux = 0;
            for (Message message : messages) {
                boolean estNouveau = saveMessageToDatabase(message, emailAccount);
                saveDomaineToDatabase(message, emailAccount);
                if (estNouveau) nouveaux++;
            }
            System.out.println("✅ " + nouveaux + " nouveaux emails sauvegardés.");

        } catch (javax.mail.AuthenticationFailedException e) {
            System.out.println("❌ Authentification refusée pour : " + emailAccount.getEmail());
        } catch (javax.mail.MessagingException e) {
            System.out.println("❌ Erreur IMAP : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ✅ Fermeture propre dans finally — toujours exécuté
            try { if (inbox != null && inbox.isOpen()) inbox.close(false); } catch (Exception ignored) {}
            try { if (store != null && store.isConnected()) store.close(); } catch (Exception ignored) {}
        }
    }

    // ✅ Retourne true si l'email est nouveau et a été sauvegardé, false si doublon
    private boolean saveMessageToDatabase(Message message, Account account) {
        try {
            String subject = message.getSubject();
            String sender = ((InternetAddress) message.getFrom()[0]).getAddress();

            LocalDate date = null;
            Date sentDate = message.getSentDate();
            if (sentDate != null) {
                date = sentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            // ✅ Vérification doublon : même sujet + même date = déjà enregistré
            List<Email> existingEmail = emailRepository.findBySubjectAndDate(subject, date);
            if (existingEmail != null && !existingEmail.isEmpty()) {
                return false; // doublon, on skip
            }

            // Extraire le corps et les pièces jointes
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
                            String folderPath = subject + (date != null ? date.toString() : "");
                            String path = saveAttachment(bodyPart, folderPath);
                            attachments.add(new Attachment(path));
                        } catch (IOException | MessagingException e) {
                            System.out.println("⚠️ Pièce jointe ignorée : " + e.getMessage());
                        }
                    } else if (bodyPart.getContentType().contains("TEXT/HTML")) {
                        bodyBuilder.append(bodyPart.getContent().toString());
                    }
                }
            }

            String recipients = getAddressString(message.getAllRecipients());

            Email email = new Email();
            email.setAccount(account);
            email.setSubject(subject);
            email.setSender(sender);
            email.setBody(bodyBuilder.toString());
            email.setDate(date);
            email.setRecipients(recipients);
            email.setAttachments(attachments);

            emailRepository.save(email);
            return true; // nouveau email sauvegardé

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    private void saveDomaineToDatabase(Message message, Account emailAccount) {
        try {
            String sender = ((InternetAddress) message.getFrom()[0]).getAddress();
            String domainName = extractDomain(sender);

            DomainEntity existingDomain = domainEntityRepository.findByDomainName(domainName);
            if (existingDomain == null) {
                existingDomain = new DomainEntity();
                existingDomain.setDomainName(domainName);
                domainEntityRepository.save(existingDomain);
            }

            if (!emailAccount.getDomains().contains(existingDomain)) {
                emailAccount.getDomains().add(existingDomain);
                accountRepository.save(emailAccount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractDomain(String email) {
        String[] parts = email.split("@");
        return parts.length == 2 ? parts[1] : null;
    }

    private String saveAttachment(BodyPart bodyPart, String folderPath) throws IOException, MessagingException {
        String destDir = "attachments/" + folderPath + "/";
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        String fileName = bodyPart.getFileName();
        File file = new File(destDir + File.separator + fileName);
        try (FileOutputStream output = new FileOutputStream(file)) {
            ((MimeBodyPart) bodyPart).saveFile(file);
        }
        return file.getAbsolutePath();
    }

    private String getAddressString(Address[] addresses) {
        if (addresses == null || addresses.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Address address : addresses) {
            if (address instanceof InternetAddress) {
                sb.append(((InternetAddress) address).getAddress()).append(", ");
            }
        }
        String result = sb.toString();
        return result.length() >= 2 ? result.substring(0, result.length() - 2) : result;
    }

    public List<Email> getEmailsByDomain(String domainName, Long accountId) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        return emailRepository.findBySenderContainingIgnoreCaseAndAccountId(domainName, accountId);
    }

    public List<Email> searchEmails(Long accountId, String sender, String subject, LocalDate startDate, LocalDate endDate) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        if (sender == null) sender = "";
        if (subject == null) subject = "";
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        return emailRepository.findBySenderContainingIgnoreCaseAndSubjectContainingIgnoreCaseAndDateBetweenAndAccount(
                sender, subject, startDate, endDate, account);
    }

    public List<Email> searchByEmail(String email, Long accountId) {
        Account account = emailAccountService.findById(accountId);
        if (account == null) throw new IllegalArgumentException("Email account not found with ID: " + accountId);
        return emailRepository.findBySenderAndAccountId(email, accountId);
    }

    public void deleteEmail(Long emailId, Long accountId) {
        Optional<Email> optionalEmail = emailRepository.findById(emailId);
        if (optionalEmail.isPresent()) {
            emailRepository.delete(optionalEmail.get());
        } else {
            throw new IllegalArgumentException("Email not found with ID: " + emailId);
        }
    }

    public List<Email> getEmailsByAccountId(Long accountId) {
        return emailRepository.findByAccountId(accountId);
    }

    public void archiveEmail(Long emailId, Long accountId) {
        try {
            Email email = emailRepository.findByIdAndAccountId(emailId, accountId);
            if (email == null) throw new IllegalArgumentException("Email not found for id: " + emailId);

            ArchivedEmail archivedEmail = new ArchivedEmail(
                email.getSender(), email.getRecipients(), email.getSubject(),
                email.getBody(), email.getDate(), new ArrayList<>(email.getAttachments()),
                email.getAccount()
            );
            archivedEmailRepository.save(archivedEmail);
            emailRepository.delete(email);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to archive email", e);
        }
    }

    public Email getEmailById(Long emailId) {
        return emailRepository.findById(emailId).orElse(null);
    }
}