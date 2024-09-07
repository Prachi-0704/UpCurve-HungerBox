package com.example.HungerBox_Backend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    // Injects the JavaMailSender bean that is configured to send emails
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email with the provided recipient, subject, and body.
     *
     * @param toEmail  The recipient's email address
     * @param subject  The subject of the email
     * @param body     The body of the email (HTML content supported)
     * @throws MessagingException If there is an error while creating or sending the email
     */
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // Create a new MimeMessage object to represent the email message
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // MimeMessageHelper is used to configure the message properties and content
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // 'true' indicates this is a multipart message

        // Set the "from" email address
        helper.setFrom("prachi.patil7404@gmail.com");

        // Set the recipient's email address
        helper.setTo(toEmail);

        // Set the subject of the email
        helper.setSubject(subject);

        // Set the body of the email and indicate that it is in HTML format ('true' for HTML)
        helper.setText(body, true);

        // Send the email using the configured JavaMailSender
        mailSender.send(mimeMessage);

        // Log to the console to indicate successful sending of the email
        System.out.println("Mail Sent Successfully...");
    }
}
