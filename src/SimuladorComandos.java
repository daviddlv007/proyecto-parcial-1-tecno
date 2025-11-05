import conexion.CommandParser;
import conexion.SMTPClient;
import negocio.*;
import datos.Persona;

import java.util.List;

/**
 * Simulador de comandos por correo para testing local
 * NO requiere POP3 - simula el flujo completo
 */
public class SimuladorComandos {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("   SIMULADOR DE COMANDOS - Testing Local");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println();
        
        try {
            SMTPClient smtp = new SMTPClient();
            PersonaService service = new PersonaService();
            
            // Simulación de correos recibidos
            String[] comandosPrueba = {
                "AYUDA",
                "LISPER[\"*\"]",
                "INSPER[\"Pedro\",\"López\"]",
                "LISPER[\"Pedro\"]",
                "MODPER[\"4\",\"Pedro Antonio\",\"López García\"]",
                "LISPER[\"*\"]",
                "DELPER[\"4\"]"
            };
            
            String destinoRespuestas = "test@localhost";
            
            for (int i = 0; i < comandosPrueba.length; i++) {
                String subject = comandosPrueba[i];
                
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println("📧 Comando " + (i+1) + "/" + comandosPrueba.length + ": " + subject);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                // Parsear comando
                CommandParser.Comando cmd = CommandParser.parse(subject);
                
                if (cmd.tieneError()) {
                    System.out.println("❌ Error: " + cmd.getError());
                    smtp.sendEmail(destinoRespuestas, "Error: " + subject, 
                        HTMLGenerator.generarError("Error", cmd.getError()));
                    continue;
                }
                
                // Ejecutar comando
                String respuesta = ejecutarComando(cmd, service);
                System.out.println("✅ Respuesta enviada a MailHog");
                
                smtp.sendEmail(destinoRespuestas, "Resultado: " + cmd.getNombre(), respuesta);
                
                // Pausa para que se vea bien en MailHog
                Thread.sleep(500);
            }
            
            System.out.println();
            System.out.println("════════════════════════════════════════════════════════");
            System.out.println("   ✅ SIMULACIÓN COMPLETA");
            System.out.println();
            System.out.println("   Abre http://localhost:8025 para ver los correos");
            System.out.println("   Deberías ver " + comandosPrueba.length + " correos enviados");
            System.out.println("════════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String ejecutarComando(CommandParser.Comando cmd, PersonaService service) throws Exception {
        switch (cmd.getNombre()) {
            case "AYUDA":
                return HTMLGenerator.generarAyuda();
                
            case "LISPER":
                if (cmd.cantidadParametros() != 1) {
                    return HTMLGenerator.generarError("Parámetros Incorrectos", 
                        "LISPER requiere 1 parámetro");
                }
                String patron = cmd.getParametros().get(0);
                List<Persona> personas = service.listar(patron);
                return HTMLGenerator.generarListaPersonas(personas, patron);
                
            case "INSPER":
                if (cmd.cantidadParametros() != 2) {
                    return HTMLGenerator.generarError("Parámetros Incorrectos", 
                        "INSPER requiere 2 parámetros: nombre y apellido");
                }
                String nombre = cmd.getParametros().get(0);
                String apellido = cmd.getParametros().get(1);
                boolean insertado = service.insertar(nombre, apellido);
                
                if (insertado) {
                    return HTMLGenerator.generarExito("Persona Registrada", 
                        "Se registró exitosamente a " + nombre + " " + apellido);
                } else {
                    return HTMLGenerator.generarError("Error", "No se pudo insertar");
                }
                
            case "MODPER":
                if (cmd.cantidadParametros() != 3) {
                    return HTMLGenerator.generarError("Parámetros Incorrectos", 
                        "MODPER requiere 3 parámetros: id, nombre y apellido");
                }
                int id = Integer.parseInt(cmd.getParametros().get(0));
                String nuevoNombre = cmd.getParametros().get(1);
                String nuevoApellido = cmd.getParametros().get(2);
                boolean modificado = service.modificar(id, nuevoNombre, nuevoApellido);
                
                if (modificado) {
                    return HTMLGenerator.generarExito("Persona Modificada", 
                        "Se actualizó exitosamente el registro ID=" + id);
                } else {
                    return HTMLGenerator.generarError("No Encontrado", 
                        "No existe persona con ID=" + id);
                }
                
            case "DELPER":
                if (cmd.cantidadParametros() != 1) {
                    return HTMLGenerator.generarError("Parámetros Incorrectos", 
                        "DELPER requiere 1 parámetro: id");
                }
                int idEliminar = Integer.parseInt(cmd.getParametros().get(0));
                boolean eliminado = service.eliminar(idEliminar);
                
                if (eliminado) {
                    return HTMLGenerator.generarExito("Persona Eliminada", 
                        "Se eliminó exitosamente el registro ID=" + idEliminar);
                } else {
                    return HTMLGenerator.generarError("No Encontrado", 
                        "No existe persona con ID=" + idEliminar);
                }
                
            default:
                return HTMLGenerator.generarError("Comando Desconocido", 
                    "El comando " + cmd.getNombre() + " no está implementado");
        }
    }
}
