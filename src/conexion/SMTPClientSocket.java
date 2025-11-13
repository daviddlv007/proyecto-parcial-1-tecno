package conexion;

import java.io.*;
import java.net.*;

/**
 * Cliente SMTP usando SOCKETS PUROS (sin JavaMail)
 * Basado EXACTAMENTE en el ejemplo ClienteSMTP.java de la clase del profesor
 * Implementación manual del protocolo SMTP
 */
public class SMTPClientSocket {
    private String servidor;
    private int puerto;
    private String user_emisor;
    
    private Socket socket;
    private BufferedReader entrada;
    private DataOutputStream salida;
    
    /**
     * Constructor para usar puerto 25 (TecnoWeb)
     */
    public SMTPClientSocket(String servidor, String user_emisor) {
        this(servidor, 25, user_emisor);
    }
    
    /**
     * Constructor configurable
     */
    public SMTPClientSocket(String servidor, int puerto, String user_emisor) {
        this.servidor = servidor;
        this.puerto = puerto;
        this.user_emisor = user_emisor;
    }
    
    /**
     * Conecta al servidor SMTP y envía un correo
     * Implementación EXACTA del ejemplo de clase
     */
    public void sendEmail(String user_receptor, String subject, String body) throws IOException {
        try {
            // Establecer conexión abriendo socket
            socket = new Socket(servidor, puerto);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new DataOutputStream(socket.getOutputStream());
            
            // Escribimos datos en el canal de salida
            if (socket != null && entrada != null && salida != null) {
                System.out.println("S: " + entrada.readLine());
                
                // EHLO
                String comando = "EHLO " + servidor + " \r\n";
                System.out.print("C: " + comando);
                salida.writeBytes(comando);
                System.out.println("S: " + getMultiline(entrada));
                
                // MAIL FROM
                comando = "MAIL FROM: " + user_emisor + " \r\n";
                System.out.print("C: " + comando);
                salida.writeBytes(comando);
                System.out.println("S: " + entrada.readLine());
                
                // RCPT TO
                comando = "RCPT TO: " + user_receptor + " \r\n";
                System.out.print("C: " + comando);
                salida.writeBytes(comando);
                System.out.println("S: " + entrada.readLine());
                
                // DATA
                comando = "DATA\n";
                System.out.print("C: " + comando);
                salida.writeBytes(comando);
                System.out.println("S: " + getMultiline(entrada));
                
                // Subject + Body
                comando = "Subject: " + subject + "\r\n" +
                         "Content-Type: text/html; charset=utf-8\r\n" +
                         "\r\n" +
                         body + "\n" +
                         ".\r\n";
                System.out.print("C: [MENSAJE ENVIADO]\n");
                salida.writeBytes(comando);
                System.out.println("S: " + entrada.readLine());
                
                // QUIT
                comando = "QUIT\r\n";
                System.out.print("C: " + comando);
                salida.writeBytes(comando);
                System.out.println("S: " + entrada.readLine());
                
                System.out.println("✓ Correo enviado a: " + user_receptor);
            }
            
            // Cerramos flujos
            salida.close();
            entrada.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            System.out.println("S: No se pudo conectar con el servidor indicado");
            throw e;
        } catch (IOException e) {
            System.out.println("S: Error de I/O: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Permite leer múltiples líneas del Protocolo SMTP
     * Copiado EXACTAMENTE del ejemplo de clase
     */
    protected static String getMultiline(BufferedReader in) throws IOException {
        String lines = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Server closed connection
                throw new IOException("S: Server unawares closed the connection.");
            }
            if (line.charAt(3) == ' ') {
                lines = lines + "\n" + line;
                // No more lines in the server response
                break;
            }
            // Add read line to the list of lines
            lines = lines + "\n" + line;
        }
        return lines;
    }
}
