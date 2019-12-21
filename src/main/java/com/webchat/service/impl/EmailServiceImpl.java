package com.webchat.service.impl;

import com.webchat.config.MailProperties;
import com.webchat.model.User;
import com.webchat.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final MailProperties mailProperties;
    private final Configuration templates;

    @Autowired
    public EmailServiceImpl(MailProperties mailProperties, Configuration templates) {
        this.mailProperties = mailProperties;
        this.templates = templates;
    }

    @Override
    public void sendVerificationMail(User user, String token) {
        String subject = "Please, verify your email";
        Map<String, String> emailVariables = new HashMap<>();
        emailVariables.put("VERIFICATION_URL", mailProperties.getUrl() + token);
        emailVariables.put("USERNAME", user.getUsername());

        String body;

        try {
            Template template = templates.getTemplate("email-verification.ftl");
            body = FreeMarkerTemplateUtils.processTemplateIntoString(template, emailVariables);

        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        sendEmail(user.getEmail(), subject, body);
    }

    private void sendEmail(String recipientEmail, String subject, String body) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", mailProperties.getSmtp().getPort());
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(body, "text/html");

            Transport transport = session.getTransport();
            transport.connect(
                    mailProperties.getSmtp().getHost(),
                    mailProperties.getSmtp().getUsername(),
                    mailProperties.getSmtp().getApiKey()
            );
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
