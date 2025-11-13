import conexion.*;
import negocio.CommandProcessor;
import datos.DBConnection;
import java.io.*;
import java.util.Properties;

/**
 * Sistema de Gestión vía E-Mail - Proyecto 1
 * Grupo 17SA - INF513 TecnoWeb
 * Usa sockets puros (sin JavaMail)
 */
public class Main {

    private static final boolean HABILITAR_SMTP_RESPUESTA = false;
    private static final int INTERVALO_REVISION_SEGUNDOS = 5;
    private static Properties config;
    private static CommandProcessor processor;
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("   SISTEMA DE GESTIÓN VÍA E-MAIL - Grupo 17SA");
        System.out.println("════════════════════════════════════════════════════════════");
        
        try {
            cargarConfiguracion();
            probarConexionBD();
            processor = new CommandProcessor();
            
            System.out.println("✓ Sistema iniciado (intervalo: " + INTERVALO_REVISION_SEGUNDOS + "s)");
            System.out.println("Esperando correos...");
            System.out.println("════════════════════════════════════════════════════════════");
            System.out.println();
            
            while (true) {
                revisarCorreos();
                Thread.sleep(INTERVALO_REVISION_SEGUNDOS * 1000);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error fatal en el sistema:");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void revisarCorreos() {
        POP3Client pop3 = null;
        try {
            String pop3Host = config.getProperty("mail.pop3.host");
            int pop3Port = Integer.parseInt(config.getProperty("mail.pop3.port", "110"));
            String pop3User = config.getProperty("mail.pop3.user");
            String pop3Pass = config.getProperty("mail.pop3.password");
            
            pop3 = new POP3Client(pop3Host, pop3Port, pop3User, pop3Pass);
            pop3.connect();
            
            int cantidad = pop3.getMessageCount();
            if (cantidad == 0) {
                pop3.close();
                return;
            }
            
            System.out.println("📬 " + cantidad + " correo(s) pendiente(s)");
            
            for (int i = 1; i <= cantidad; i++) {
                procesarCorreo(pop3, i);
            }
            
            pop3.close();
            
        } catch (java.net.ConnectException e) {
            System.err.println("❌ No se pudo conectar al servidor POP3: " + e.getMessage());
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                System.err.println("❌ Error de autenticación POP3 - verificar credenciales");
            } else {
                System.err.println("❌ Error de conexión POP3: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("❌ Error al revisar correos: " + e.getMessage());
            // Si es error de conexión, intentar reconectar en la próxima iteración
        } finally {
            if (pop3 != null) {
                try {
                    pop3.close();
                } catch (Exception closeEx) {
                    // Ignorar errores al cerrar - puede estar ya cerrado
                }
            }
        }
    }
    
    private static void procesarCorreo(POP3Client pop3, int numero) {
        try {
            POP3Client.EmailMessage email = pop3.getMessage(numero);
            
            if (email == null) {
                System.err.println("❌ No se pudo leer el correo #" + numero);
                return;
            }
            
            System.out.println("┌─────────────────────────────────────────");
            System.out.println("│ De: " + email.getFrom());
            System.out.println("│ Asunto: " + email.getSubject());
            System.out.println("└─────────────────────────────────────────");
            
            CommandParser.Comando comando = CommandParser.parse(email.getSubject());
            
            if (comando.tieneError()) {
                String errorMsg = comando.getMensajeError();
                System.out.println("❌ " + errorMsg);
                enviarRespuesta(email.getFrom(), "Error en comando", 
                    "<h3>Error</h3><p>" + errorMsg + "</p>");
                pop3.deleteMessage(numero);
                return;
            }
            
            if (!comando.esValido()) {
                String errorMsg = "Comando '" + comando.getNombre() + "' no reconocido";
                System.out.println("❌ " + errorMsg);
                enviarRespuesta(email.getFrom(), "Error - Comando no válido", 
                    "<h3>Error</h3><p>" + errorMsg + "</p>");
                pop3.deleteMessage(numero);
                return;
            }
            
            String respuestaHTML = processor.procesarComando(comando);
            
            String subjectRespuesta = "Re: " + comando.getNombre() + " - Éxito";
            if (respuestaHTML.contains("Error")) {
                subjectRespuesta = "Re: " + comando.getNombre() + " - Error";
            }
            
            enviarRespuesta(email.getFrom(), subjectRespuesta, respuestaHTML);
            pop3.deleteMessage(numero);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void enviarRespuesta(String destinatario, String subject, String htmlBody) {
        if (HABILITAR_SMTP_RESPUESTA) {
            try {
                String smtpHost = config.getProperty("mail.smtp.host");
                String smtpUser = config.getProperty("mail.smtp.user");
                
                SMTPClientSocket smtp = new SMTPClientSocket(smtpHost, smtpUser);
                smtp.sendEmail(destinatario, subject, htmlBody);
                
                System.out.println("  ✉ Respuesta enviada por SMTP a: " + destinatario);
                
            } catch (Exception e) {
                System.err.println("  Error enviando SMTP: " + e.getMessage());
                mostrarRespuestaEnConsola(destinatario, subject, htmlBody);
            }
        } else {
            mostrarRespuestaEnConsola(destinatario, subject, htmlBody);
        }
    }
    
    private static void mostrarRespuestaEnConsola(String destinatario, String subject, String htmlBody) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════");
        System.out.println("  ║ RESPUESTA (Consola - SMTP deshabilitado)");
        System.out.println("  ╠══════════════════════════════════════════");
        System.out.println("  ║ Para:    " + destinatario);
        System.out.println("  ║ Asunto:  " + subject);
        System.out.println("  ╠══════════════════════════════════════════");
        System.out.println("  ║ Contenido:");
        System.out.println("  ║");
        
        String textoSimple = htmlBody
            .replaceAll("<[^>]+>", "")
            .replaceAll("&nbsp;", " ")
            .replaceAll("&quot;", "\"")
            .trim();
        
        String[] lineas = textoSimple.split("\n");
        for (String linea : lineas) {
            String lineaLimpia = linea.trim();
            if (!lineaLimpia.isEmpty()) {
                System.out.println("  ║     " + lineaLimpia);
            }
        }
        
        System.out.println("  ║");
        System.out.println("  ╚══════════════════════════════════════════");
        System.out.println();
    }
    
    private static void cargarConfiguracion() throws IOException {
        config = new Properties();
        
        File configFile = new File("config.properties");
        if (!configFile.exists()) {
            throw new FileNotFoundException("No se encontró config.properties");
        }
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            config.load(fis);
        }
        
        System.out.println("✓ Configuración cargada desde config.properties");
    }
    
    private static void probarConexionBD() {
        try {
            DBConnection db = DBConnection.getInstance();
            if (db.getConnection() != null) {
                System.out.println("✓ Conexión a BD PostgreSQL establecida");
                System.out.println("  └─ Servidor: " + config.getProperty("db.url"));
            } else {
                throw new Exception("No se pudo conectar a la BD");
            }
        } catch (Exception e) {
            System.err.println("Error de conexión a BD:");
            System.err.println("   " + e.getMessage());
            throw new RuntimeException("No se puede iniciar sin BD", e);
        }
    }
}
