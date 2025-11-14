package conexion;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Cliente para enviar correos usando MailerSend API (puerto 443 HTTP)
 * Alternativa cuando todos los puertos SMTP están bloqueados
 * Gratis: 12,000 correos/mes (400/día)
 * NO requiere librerías externas - JSON manual
 */
public class MailerSendClient {
    private String apiToken;
    private String fromEmail;
    private String fromName;
    
    public MailerSendClient(String apiToken, String fromEmail, String fromName) {
        this.apiToken = apiToken;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }
    
    /**
     * Envía un correo usando la API de MailerSend
     * Usa puerto 443 (HTTP) que NUNCA está bloqueado
     */
    public void sendEmail(String to, String subject, String htmlBody) throws IOException {
        try {
            // URL del API de MailerSend
            URL url = new URL("https://api.mailersend.com/v1/email");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Configurar conexión
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiToken);
            conn.setDoOutput(true);
            
            // Crear JSON manualmente (sin librerías)
            String jsonBody = buildJsonRequest(to, subject, htmlBody);
            
            // Enviar petición
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Leer respuesta
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✓ Correo enviado exitosamente vía MailerSend API");
                System.out.println("  Destinatario: " + to);
            } else {
                // Leer error
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                
                throw new IOException("Error MailerSend API (" + responseCode + "): " + response.toString());
            }
            
        } catch (Exception e) {
            throw new IOException("Error enviando correo vía MailerSend: " + e.getMessage(), e);
        }
    }
    
    /**
     * Construye el JSON manualmente sin librerías externas
     */
    private String buildJsonRequest(String to, String subject, String htmlBody) {
        // Escapar comillas y caracteres especiales
        String safeSubject = escapeJson(subject);
        String safeHtml = escapeJson(htmlBody);
        String safeTo = escapeJson(to);
        
        return "{"
            + "\"from\":{\"email\":\"" + fromEmail + "\",\"name\":\"" + fromName + "\"},"
            + "\"to\":[{\"email\":\"" + safeTo + "\"}],"
            + "\"subject\":\"" + safeSubject + "\","
            + "\"html\":\"" + safeHtml + "\""
            + "}";
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
