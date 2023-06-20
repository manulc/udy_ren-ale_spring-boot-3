package com.mlorenzo.besttravel.clients;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class EmailClient {
    private final JavaMailSender javaMailSender;

    public void sendMail(final String to, final String name, final String product) {
        final MimeMessage message = javaMailSender.createMimeMessage();
        final String htmlContent = readEmailTemplate(name, product);
        try {
            message.setFrom(new InternetAddress("besttravel@test.com"));
            message.setSubject(String.format("Confirmed %s", product));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
            javaMailSender.send(message);
        }
        catch (MessagingException e) {
            throw new IllegalStateException("Error sending mail", e);
        }
    }

    private String readEmailTemplate(final String name, final String product) {
        try(final Stream<String> lines = Files.lines(Paths.get("src/main/resources/email_template.html"))) {
            final String content = lines.collect(Collectors.joining());
            return content.replace("{name}", name).replace("{product}", product);
        }
        catch(IOException ex) {
            throw new IllegalStateException("Email template reading error", ex);
        }
    }
}
