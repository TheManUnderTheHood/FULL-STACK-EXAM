package com.eventmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public void sendHTMLEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
        }
    }

    public void sendTicketWithQRCode(String to, String userName, String eventName, 
                                     String qrCodeBase64, String registrationToken) {
        String subject = "Your Event Ticket - " + eventName;
        String htmlBody = String.format(
            "<html>" +
            "<body>" +
            "<h2>Event Registration Confirmation</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Thank you for registering for <strong>%s</strong>!</p>" +
            "<p>Your QR Code ticket is shown below. Please present this at the entrance.</p>" +
            "<img src='data:image/png;base64,%s' width='300' height='300' />" +
            "<p><strong>Registration Token:</strong> %s</p>" +
            "<p>Please do not share this QR code with anyone else.</p>" +
            "<p>Best regards,<br>Event Management Team</p>" +
            "</body>" +
            "</html>",
            userName, eventName, qrCodeBase64, registrationToken
        );
        sendHTMLEmail(to, subject, htmlBody);
    }

    public void sendPaymentConfirmation(String to, String userName, String eventName, 
                                       Double amount, String transactionId) {
        String subject = "Payment Confirmation - " + eventName;
        String htmlBody = String.format(
            "<html>" +
            "<body>" +
            "<h2>Payment Confirmation</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Your payment for <strong>%s</strong> has been processed successfully.</p>" +
            "<p><strong>Amount:</strong> ₹%.2f</p>" +
            "<p><strong>Transaction ID:</strong> %s</p>" +
            "<p>Please check your email for your event ticket.</p>" +
            "<p>Best regards,<br>Event Management Team</p>" +
            "</body>" +
            "</html>",
            userName, eventName, amount, transactionId
        );
        sendHTMLEmail(to, subject, htmlBody);
    }
}
