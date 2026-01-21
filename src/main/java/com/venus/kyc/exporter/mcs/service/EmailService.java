package com.venus.kyc.exporter.mcs.service;

import com.venus.kyc.exporter.mcs.config.ExportProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final ExportProperties exportProperties;

    public EmailService(JavaMailSender mailSender, ExportProperties exportProperties) {
        this.mailSender = mailSender;
        this.exportProperties = exportProperties;
    }

    public void sendFileByEmail(byte[] content, String filename) throws MessagingException {
        ExportProperties.EmailProperties emailProps = exportProperties.getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailProps.getTo());
        helper.setSubject(emailProps.getSubject());
        helper.setText(emailProps.getBody());
        helper.addAttachment(filename, new ByteArrayResource(content));

        mailSender.send(message);
    }
}
