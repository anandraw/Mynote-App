package org.anand.mynoteapp.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.anand.mynoteapp.dto.EmailReuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendEmail(EmailReuest emailReq) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(mailFrom, emailReq.getTitle());
        helper.setTo(emailReq.getTo());
        helper.setSubject(emailReq.getSubject());
        helper.setText(emailReq.getMessage(),true);
        mailSender.send(message);
    }

}
