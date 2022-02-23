package tech.xigam.onelineofcode.utils;

import lombok.Setter;

import javax.annotation.Nullable;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public final class MailUtil {
    public final static Properties defaultProperties = new Properties();
    
    static {
        defaultProperties.put("mail.smtp.host", "smtp.gmail.com");
        defaultProperties.put("mail.smtp.socketFactory.port", "465");
        defaultProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        defaultProperties.put("mail.smtp.auth", "true");
        defaultProperties.put("mail.smtp.port", "465");
    }
    
    public static Session createSession(Properties properties, String username, String password) {
        return Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
    
    @Nullable
    public static Message craftMessage(Session session, MessageData rawContent) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(rawContent.from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(rawContent.to));
            message.setSubject(rawContent.subject); message.setText(rawContent.rawContent);
            return message;
        } catch (MessagingException ignored) {
            return null;
        }
    }
    
    public static class MessageData {
        public String from;
        public String to;
        public String subject;
        public String rawContent;
        
        public MessageData setFrom(String from) {
            this.from = from; return this;
        }
        
        public MessageData setTo(String to) {
            this.to = to; return this;
        }
        
        public MessageData setSubject(String subject) {
            this.subject = subject; return this;
        }
        
        public MessageData setRawContent(String rawContent) {
            this.rawContent = rawContent; return this;
        }
    }
}