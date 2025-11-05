package conexion;

import javax.mail.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Cliente POP3 para leer correos entrantes
 * Simplificado para MailHog (no requiere SSL)
 */
public class POP3Client {
    private Properties config;
    private Store store;
    private Folder inbox;
    
    public POP3Client() throws IOException {
        loadConfig();
    }
    
    private void loadConfig() throws IOException {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
        }
    }
    
    /**
     * Conecta al servidor POP3
     */
    public void connect() throws MessagingException {
        String host = config.getProperty("mail.pop3.host");
        String user = config.getProperty("mail.pop3.user");
        String password = config.getProperty("mail.pop3.password");
        
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.port", config.getProperty("mail.pop3.port", "110"));
        
        Session session = Session.getInstance(props);
        store = session.getStore("pop3");
        
        System.out.println("Conectando a POP3: " + host);
        store.connect(host, user, password);
        
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        
        System.out.println("✓ Conectado a POP3");
    }
    
    /**
     * Obtiene todos los mensajes no leídos
     */
    public Message[] getMessages() throws MessagingException {
        if (inbox == null || !inbox.isOpen()) {
            throw new IllegalStateException("Inbox no está abierto");
        }
        
        return inbox.getMessages();
    }
    
    /**
     * Obtiene la cantidad de mensajes
     */
    public int getMessageCount() throws MessagingException {
        if (inbox == null || !inbox.isOpen()) {
            return 0;
        }
        return inbox.getMessageCount();
    }
    
    /**
     * Marca un mensaje como eliminado
     */
    public void deleteMessage(Message message) throws MessagingException {
        message.setFlag(Flags.Flag.DELETED, true);
    }
    
    /**
     * Cierra la conexión
     */
    public void close() {
        try {
            if (inbox != null && inbox.isOpen()) {
                inbox.close(true); // true = expunge deleted messages
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
            System.out.println("✓ Conexión POP3 cerrada");
        } catch (MessagingException e) {
            System.err.println("Error al cerrar POP3: " + e.getMessage());
        }
    }
}
