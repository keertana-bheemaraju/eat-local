package com.vtc.eatlocal.service;

import org.springframework.stereotype.Service;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class EmailService {

    private  Session session;

    public EmailService() {

        final String username = "kebmrj@gmail.com";
        final String password = "xkjihcasqaiojmil";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

    }

    public void sendCustomerPasswordResetEmail(String recepientEmailAddress) {

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("kebmrj@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recepientEmailAddress)
            );
            message.setSubject("Password reset email");
            message.setText("Please visit this link to reset your Eat Local password http://localhost:3000/customer-password-reset ");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
