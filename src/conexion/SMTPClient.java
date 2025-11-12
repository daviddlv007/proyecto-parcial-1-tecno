package conexion;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Cliente SMTP para enviar respuestas por correo
 */
public class SMTPClient {
    private Properties config;
    private Session session;
    
    public SMTPClient() throws IOException {
        loadConfig();
        setupSession();
    }
    
    private void loadConfig() throws IOException {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
        }
        
        // Cargar variables de entorno si están disponibles
        String smtpHost = System.getenv("SMTP_HOST");
        if (smtpHost != null) config.setProperty("mail.smtp.host", smtpHost);
        
        String smtpPort = System.getenv("SMTP_PORT");
        if (smtpPort != null) config.setProperty("mail.smtp.port", smtpPort);
    }
    
    private void setupSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", config.getProperty("mail.smtp.port", "25"));
        props.put("mail.smtp.auth", config.getProperty("mail.smtp.auth", "false"));
        props.put("mail.smtp.starttls.enable", config.getProperty("mail.smtp.starttls", "false"));
        
        session = Session.getInstance(props);
        System.out.println("✓ SMTP Client configurado");
    }
    
    /**
     * Envía un correo HTML
     * @param to Destinatario
     * @param subject Asunto
     * @param htmlBody Cuerpo en HTML
     */
    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        String from = config.getProperty("mail.smtp.user", "sistema@localhost");
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        
        // Contenido HTML
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(htmlBody, "text/html; charset=utf-8");
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        
        message.setContent(multipart);
        
        Transport.send(message);
        
        System.out.println("✓ Correo enviado a: " + to);
        System.out.println("  Subject: " + subject);
    }
    
    /**
     * Envía un correo de texto plano
     */
    public void sendTextEmail(String to, String subject, String body) throws MessagingException {
        String from = config.getProperty("mail.smtp.user", "sistema@localhost");
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        
        Transport.send(message);
        
        System.out.println("✓ Correo enviado a: " + to);
    }
}
