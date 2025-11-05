package servicio;

import conexion.*;
import negocio.*;
import datos.Persona;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Servicio principal que lee correos, procesa comandos y responde
 * Loop infinito que chequea nuevos correos cada X segundos
 */
public class MailService {
    private POP3Client pop3Client;
    private SMTPClient smtpClient;
    private PersonaService personaService;
    private boolean running;
    private int checkInterval;
    
    public MailService() throws IOException {
        this.pop3Client = new POP3Client();
        this.smtpClient = new SMTPClient();
        this.personaService = new PersonaService();
        this.running = false;
        
        // Cargar intervalo de configuración
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
            this.checkInterval = Integer.parseInt(config.getProperty("mail.check.interval", "10000"));
        }
    }
    
    /**
     * Inicia el servicio de lectura y procesamiento de correos
     */
    public void start() {
        running = true;
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN VÍA E-MAIL - INICIADO                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("⏰ Intervalo de chequeo: " + (checkInterval / 1000) + " segundos");
        System.out.println("📨 Esperando correos entrantes...");
        System.out.println("⌨️  Presiona Ctrl+C para detener");
        System.out.println();
        
        while (running) {
            try {
                procesarCorreos();
                
                // Esperar antes del siguiente ciclo
                Thread.sleep(checkInterval);
                
            } catch (InterruptedException e) {
                System.out.println("⚠️  Servicio interrumpido");
                break;
            } catch (Exception e) {
                System.err.println("❌ Error en el ciclo principal: " + e.getMessage());
                e.printStackTrace();
                
                // Esperar un poco antes de reintentar
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
        
        System.out.println("🛑 Servicio detenido");
    }
    
    /**
     * Procesa todos los correos pendientes
     */
    private void procesarCorreos() throws Exception {
        pop3Client.connect();
        
        Message[] messages = pop3Client.getMessages();
        
        if (messages.length > 0) {
            System.out.println("📬 " + messages.length + " correo(s) recibido(s)");
        }
        
        for (Message message : messages) {
            try {
                procesarMensaje(message);
                
                // Marcar como procesado (eliminar)
                pop3Client.deleteMessage(message);
                
            } catch (Exception e) {
                System.err.println("❌ Error procesando mensaje: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        pop3Client.close();
    }
    
    /**
     * Procesa un mensaje individual
     */
    private void procesarMensaje(Message message) throws Exception {
        String from = message.getFrom()[0].toString();
        String subject = message.getSubject();
        
        System.out.println("\n┌─ Procesando correo ─────────────────────────");
        System.out.println("│ De: " + from);
        System.out.println("│ Asunto: " + subject);
        
        // Parsear comando
        CommandParser.Comando comando = CommandParser.parse(subject);
        
        if (comando.tieneError()) {
            System.out.println("│ ❌ Error de parseo: " + comando.getError());
            enviarRespuesta(from, "Error: Comando inválido", 
                HTMLGenerator.generarError("Error de Sintaxis", comando.getError()));
            return;
        }
        
        System.out.println("│ Comando: " + comando.getNombre());
        System.out.println("│ Parámetros: " + comando.cantidadParametros());
        
        // Validar comando
        if (!CommandParser.esComandoValido(comando.getNombre())) {
            System.out.println("│ ❌ Comando no válido");
            enviarRespuesta(from, "Error: Comando no encontrado",
                HTMLGenerator.generarError("Comando No Encontrado",
                    "El comando '" + comando.getNombre() + "' no existe. Envía AYUDA para ver los comandos disponibles."));
            return;
        }
        
        // Ejecutar comando
        ejecutarComando(from, comando);
        
        System.out.println("└─────────────────────────────────────────────");
    }
    
    /**
     * Ejecuta el comando parseado
     */
    private void ejecutarComando(String from, CommandParser.Comando comando) {
        try {
            switch (comando.getNombre()) {
                case "AYUDA":
                    ejecutarAyuda(from);
                    break;
                    
                case "LISPER":
                    ejecutarListar(from, comando);
                    break;
                    
                case "INSPER":
                    ejecutarInsertar(from, comando);
                    break;
                    
                case "MODPER":
                    ejecutarModificar(from, comando);
                    break;
                    
                case "DELPER":
                    ejecutarEliminar(from, comando);
                    break;
                    
                default:
                    enviarRespuesta(from, "Error: Comando no implementado",
                        HTMLGenerator.generarError("No Implementado", "Este comando aún no está implementado."));
            }
        } catch (Exception e) {
            System.err.println("│ ❌ Error ejecutando comando: " + e.getMessage());
            enviarRespuesta(from, "Error: " + comando.getNombre(),
                HTMLGenerator.generarError("Error de Ejecución", e.getMessage()));
        }
    }
    
    private void ejecutarAyuda(String from) {
        System.out.println("│ ✓ Generando ayuda...");
        String html = HTMLGenerator.generarAyuda();
        enviarRespuesta(from, "Comandos Disponibles - Sistema de Gestión", html);
    }
    
    private void ejecutarListar(String from, CommandParser.Comando comando) throws Exception {
        // LISPER necesita 1 parámetro
        if (comando.cantidadParametros() != 1) {
            enviarRespuesta(from, "Error: LISPER - Parámetros incorrectos",
                HTMLGenerator.generarError("Parámetros Incorrectos",
                    "LISPER requiere 1 parámetro. Ejemplo: LISPER[\"*\"]"));
            return;
        }
        
        String patron = comando.getParametros().get(0);
        System.out.println("│ 🔍 Listando con patrón: " + patron);
        
        List<Persona> personas = personaService.listar(patron);
        String html = HTMLGenerator.generarListaPersonas(personas, patron);
        
        enviarRespuesta(from, "Resultado: LISPER - " + personas.size() + " persona(s) encontrada(s)", html);
        System.out.println("│ ✓ Enviadas " + personas.size() + " persona(s)");
    }
    
    private void ejecutarInsertar(String from, CommandParser.Comando comando) throws Exception {
        // INSPER necesita 2 parámetros
        if (comando.cantidadParametros() != 2) {
            enviarRespuesta(from, "Error: INSPER - Parámetros incorrectos",
                HTMLGenerator.generarError("Parámetros Incorrectos",
                    "INSPER requiere 2 parámetros: nombre y apellido. Ejemplo: INSPER[\"Juan\",\"Pérez\"]"));
            return;
        }
        
        String nombre = comando.getParametros().get(0);
        String apellido = comando.getParametros().get(1);
        
        System.out.println("│ ➕ Insertando: " + nombre + " " + apellido);
        
        boolean exito = personaService.insertar(nombre, apellido);
        
        if (exito) {
            String html = HTMLGenerator.generarExito("Persona Registrada",
                "Se registró exitosamente a " + nombre + " " + apellido + ".");
            enviarRespuesta(from, "Éxito: Persona registrada", html);
            System.out.println("│ ✓ Persona insertada correctamente");
        } else {
            enviarRespuesta(from, "Error: No se pudo insertar",
                HTMLGenerator.generarError("Error de Inserción", "No se pudo insertar el registro."));
        }
    }
    
    private void ejecutarModificar(String from, CommandParser.Comando comando) throws Exception {
        // MODPER necesita 3 parámetros
        if (comando.cantidadParametros() != 3) {
            enviarRespuesta(from, "Error: MODPER - Parámetros incorrectos",
                HTMLGenerator.generarError("Parámetros Incorrectos",
                    "MODPER requiere 3 parámetros: id, nombre y apellido. Ejemplo: MODPER[\"1\",\"Juan\",\"López\"]"));
            return;
        }
        
        try {
            int id = Integer.parseInt(comando.getParametros().get(0));
            String nombre = comando.getParametros().get(1);
            String apellido = comando.getParametros().get(2);
            
            System.out.println("│ ✏️  Modificando persona ID=" + id);
            
            boolean exito = personaService.modificar(id, nombre, apellido);
            
            if (exito) {
                String html = HTMLGenerator.generarExito("Persona Modificada",
                    "Se actualizó exitosamente el registro ID=" + id + " a " + nombre + " " + apellido + ".");
                enviarRespuesta(from, "Éxito: Persona modificada", html);
                System.out.println("│ ✓ Persona modificada correctamente");
            } else {
                enviarRespuesta(from, "Error: Persona no encontrada",
                    HTMLGenerator.generarError("No Encontrado", "No existe una persona con ID=" + id + "."));
            }
            
        } catch (NumberFormatException e) {
            enviarRespuesta(from, "Error: ID inválido",
                HTMLGenerator.generarError("Parámetro Inválido", "El ID debe ser un número entero."));
        }
    }
    
    private void ejecutarEliminar(String from, CommandParser.Comando comando) throws Exception {
        // DELPER necesita 1 parámetro
        if (comando.cantidadParametros() != 1) {
            enviarRespuesta(from, "Error: DELPER - Parámetros incorrectos",
                HTMLGenerator.generarError("Parámetros Incorrectos",
                    "DELPER requiere 1 parámetro: id. Ejemplo: DELPER[\"1\"]"));
            return;
        }
        
        try {
            int id = Integer.parseInt(comando.getParametros().get(0));
            
            System.out.println("│ 🗑️  Eliminando persona ID=" + id);
            
            boolean exito = personaService.eliminar(id);
            
            if (exito) {
                String html = HTMLGenerator.generarExito("Persona Eliminada",
                    "Se eliminó exitosamente el registro ID=" + id + ".");
                enviarRespuesta(from, "Éxito: Persona eliminada", html);
                System.out.println("│ ✓ Persona eliminada correctamente");
            } else {
                enviarRespuesta(from, "Error: Persona no encontrada",
                    HTMLGenerator.generarError("No Encontrado", "No existe una persona con ID=" + id + "."));
            }
            
        } catch (NumberFormatException e) {
            enviarRespuesta(from, "Error: ID inválido",
                HTMLGenerator.generarError("Parámetro Inválido", "El ID debe ser un número entero."));
        }
    }
    
    /**
     * Envía una respuesta por correo
     */
    private void enviarRespuesta(String to, String subject, String htmlBody) {
        try {
            smtpClient.sendEmail(to, subject, htmlBody);
        } catch (MessagingException e) {
            System.err.println("│ ❌ Error enviando respuesta: " + e.getMessage());
        }
    }
    
    /**
     * Detiene el servicio
     */
    public void stop() {
        running = false;
    }
}
