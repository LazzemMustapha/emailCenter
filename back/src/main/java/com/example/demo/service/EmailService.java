package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Environment env;


    @Autowired
    public EmailService(JavaMailSender javaMailSender,Environment env) {
        this.javaMailSender = javaMailSender;
        this.env = env;
    }

    public void sendPasswordResetEmail(String recipientEmail, String resetToken) {
        String emailUsername = env.getProperty("alouloumed21@gmail.com");
        String emailPassword = env.getProperty("");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Password Reset");
        message.setText("Click the link below to reset your password:\n\n"
                + "http://localhost:8088/reset-password?token=" + resetToken);

        javaMailSender.send(message);
    }
    
    



}
