package conexion;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

/**
 * Cliente POP3 usando SOCKETS PUROS (sin JavaMail)
 * Basado en el ejemplo de ClienteSMTP de la clase del profesor
 * Implementación manual del protocolo POP3
 */
public class POP3Client {
    private String host;
    private int port;
    private String user;
    private String password;
    
    private Socket socket;
    private BufferedReader entrada;
    private DataOutputStream salida;
    
    public POP3Client(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
    
    /**
     * Conecta al servidor POP3 y se autentica
     */
    public void connect() throws IOException {
        // System.out.println("[POP3] Conectando a " + host + ":" + port);
        
        socket = new Socket(host, port);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new DataOutputStream(socket.getOutputStream());
        
        // Leer saludo del servidor
        String response = entrada.readLine();
        // System.out.println("S: " + response);
        
        // Autenticación USER
        String comando = "USER " + user + "\r\n";
        // System.out.print("C: " + comando.trim());
        salida.writeBytes(comando);
        response = entrada.readLine();
        // System.out.println(" -> S: " + response);
        
        // Autenticación PASS
        comando = "PASS " + password + "\r\n";
        // System.out.print("C: PASS ****");
        salida.writeBytes(comando);
        response = entrada.readLine();
        // System.out.println(" -> S: " + response);
        
        if (!response.startsWith("+OK")) {
            throw new IOException("Error de autenticación POP3");
        }
        
        // System.out.println("✓ Conectado y autenticado en POP3");
    }
    
    /**
     * Obtiene la cantidad de mensajes
     */
    public int getMessageCount() throws IOException {
        String comando = "STAT\r\n";
        salida.writeBytes(comando);
        String response = entrada.readLine();
        
        // Formato: +OK num size
        if (response.startsWith("+OK")) {
            String[] parts = response.split(" ");
            if (parts.length >= 2) {
                return Integer.parseInt(parts[1]);
            }
        }
        return 0;
    }
    
    /**
     * Lee un mensaje completo por número
     */
    public EmailMessage getMessage(int numero) throws IOException {
        String comando = "RETR " + numero + "\r\n";
        salida.writeBytes(comando);
        
        String response = entrada.readLine();
        if (!response.startsWith("+OK")) {
            return null;
        }
        
        // Leer todo el mensaje hasta encontrar línea con solo "."
        StringBuilder mensaje = new StringBuilder();
        String linea;
        while ((linea = entrada.readLine()) != null) {
            if (".".equals(linea)) {
                break;
            }
            mensaje.append(linea).append("\n");
        }
        
        return parseEmail(mensaje.toString(), numero);
    }
    
    /**
     * Parsea el contenido RAW del email
     */
    private EmailMessage parseEmail(String raw, int numero) {
        String from = "";
        String subject = "";
        String body = "";
        
        String[] lineas = raw.split("\n");
        boolean enCuerpo = false;
        StringBuilder bodyBuilder = new StringBuilder();
        StringBuilder subjectBuilder = new StringBuilder();
        boolean leyendoSubject = false;
        
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            
            if (enCuerpo) {
                bodyBuilder.append(linea).append("\n");
            } else if (linea.isEmpty()) {
                enCuerpo = true;
                // Finalizar subject si se estaba leyendo
                if (leyendoSubject) {
                    subject = subjectBuilder.toString().trim();
                    subject = decodeMimeEncodedWord(subject);
                    leyendoSubject = false;
                }
            } else if (leyendoSubject) {
                // Continuar leyendo subject si la línea empieza con espacio/tab (folding)
                if (linea.startsWith(" ") || linea.startsWith("\t")) {
                    subjectBuilder.append(linea.trim());
                } else {
                    // Terminó el subject, procesar otro header
                    subject = subjectBuilder.toString().trim();
                    subject = decodeMimeEncodedWord(subject);
                    leyendoSubject = false;
                    // Continuar procesando esta línea como nuevo header
                    i--;
                }
            } else if (linea.toLowerCase().startsWith("from:")) {
                from = linea.substring(5).trim();
                // Extraer email si está en formato "Name <email>"
                if (from.contains("<")) {
                    from = from.substring(from.indexOf("<") + 1, from.indexOf(">"));
                }
            } else if (linea.toLowerCase().startsWith("subject:")) {
                subjectBuilder = new StringBuilder();
                subjectBuilder.append(linea.substring(8).trim());
                leyendoSubject = true;
            }
        }
        
        // Si terminó el bucle mientras leía subject
        if (leyendoSubject && subject.isEmpty()) {
            subject = subjectBuilder.toString().trim();
            subject = decodeMimeEncodedWord(subject);
        }
        
        body = bodyBuilder.toString().trim();
        
        return new EmailMessage(numero, from, subject, body);
    }
    
    /**
     * Decodifica MIME encoded-words (RFC 2047)
     * Formato: =?charset?encoding?encoded-text?=
     * Ejemplo: =?UTF-8?B?SU5TVElW...?= (Base64)
     *          =?UTF-8?Q?INSTIV=5B...?= (Quoted-Printable)
     */
    private String decodeMimeEncodedWord(String input) {
        if (input == null || !input.contains("=?")) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        int pos = 0;
        
        while (pos < input.length()) {
            int start = input.indexOf("=?", pos);
            if (start == -1) {
                result.append(input.substring(pos));
                break;
            }
            
            // Agregar texto no codificado antes del encoded-word
            result.append(input.substring(pos, start));
            
            int end = input.indexOf("?=", start + 2);
            if (end == -1) {
                result.append(input.substring(start));
                break;
            }
            
            // Parsear: =?charset?encoding?encoded-text?=
            String encodedWord = input.substring(start + 2, end);
            String[] parts = encodedWord.split("\\?", 3);
            
            if (parts.length == 3) {
                String charset = parts[0];
                String encoding = parts[1].toUpperCase();
                String encodedText = parts[2];
                
                try {
                    String decoded;
                    if ("B".equals(encoding)) {
                        // Base64
                        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
                        decoded = new String(decodedBytes, charset);
                    } else if ("Q".equals(encoding)) {
                        // Quoted-Printable
                        decoded = decodeQuotedPrintable(encodedText);
                    } else {
                        decoded = encodedText;
                    }
                    result.append(decoded);
                } catch (Exception e) {
                    // Si falla la decodificación, mantener original
                    result.append(input.substring(start, end + 2));
                }
            } else {
                result.append(input.substring(start, end + 2));
            }
            
            pos = end + 2;
        }
        
        return result.toString();
    }
    
    /**
     * Decodifica Quoted-Printable (simplificado)
     */
    private String decodeQuotedPrintable(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '_') {
                result.append(' ');
            } else if (c == '=' && i + 2 < input.length()) {
                String hex = input.substring(i + 1, i + 3);
                try {
                    int value = Integer.parseInt(hex, 16);
                    result.append((char) value);
                    i += 2;
                } catch (NumberFormatException e) {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    /**
     * Marca un mensaje para eliminación
     */
    public void deleteMessage(int numero) throws IOException {
        String comando = "DELE " + numero + "\r\n";
        salida.writeBytes(comando);
        String response = entrada.readLine();
        // System.out.println("DELE " + numero + ": " + response);
    }
    
    /**
     * Cierra la conexión
     */
    public void close() {
        try {
            if (salida != null) {
                salida.writeBytes("QUIT\r\n");
                entrada.readLine();
            }
            
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            
            // System.out.println("✓ Conexión POP3 cerrada");
        } catch (IOException e) {
            // Ignorar error "Socket closed" - es normal cuando el socket ya estaba cerrado
            // System.err.println("❌ Error cerrando POP3: " + e.getMessage());
        }
    }
    
    /**
     * Clase interna para representar un email
     */
    public static class EmailMessage {
        private int numero;
        private String from;
        private String subject;
        private String body;
        
        public EmailMessage(int numero, String from, String subject, String body) {
            this.numero = numero;
            this.from = from;
            this.subject = subject;
            this.body = body;
        }
        
        public int getNumero() { return numero; }
        public String getFrom() { return from; }
        public String getSubject() { return subject; }
        public String getBody() { return body; }
    }
}
