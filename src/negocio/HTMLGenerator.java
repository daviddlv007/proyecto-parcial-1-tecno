package negocio;

import datos.Persona;
import java.util.List;

/**
 * Generador de respuestas HTML para los correos
 */
public class HTMLGenerator {
    
    /**
     * Genera HTML para el comando AYUDA
     */
    public static String generarAyuda() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h2 { color: #333; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top: 10px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #4CAF50; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append(".ejemplo { font-family: 'Courier New', monospace; background-color: #f4f4f4; padding: 5px; }");
        html.append("</style>");
        html.append("</head><body>");
        
        html.append("<h2>📋 Comandos Disponibles - Sistema de Gestión de Personas</h2>");
        
        html.append("<table>");
        html.append("<tr><th>Comando</th><th>Descripción</th><th>Parámetros</th><th>Ejemplo</th></tr>");
        
        // LISPER
        html.append("<tr>");
        html.append("<td><strong>LISPER</strong></td>");
        html.append("<td>Lista personas según un patrón de búsqueda</td>");
        html.append("<td>1. patron (String)<br>- \"*\" = todos<br>- texto = buscar por nombre/apellido</td>");
        html.append("<td class='ejemplo'>LISPER[\"*\"]<br>LISPER[\"Juan\"]</td>");
        html.append("</tr>");
        
        // INSPER
        html.append("<tr>");
        html.append("<td><strong>INSPER</strong></td>");
        html.append("<td>Inserta una nueva persona</td>");
        html.append("<td>1. nombre (String)<br>2. apellido (String)</td>");
        html.append("<td class='ejemplo'>INSPER[\"Carlos\",\"Martínez\"]</td>");
        html.append("</tr>");
        
        // MODPER
        html.append("<tr>");
        html.append("<td><strong>MODPER</strong></td>");
        html.append("<td>Modifica una persona existente</td>");
        html.append("<td>1. id (Número)<br>2. nombre (String)<br>3. apellido (String)</td>");
        html.append("<td class='ejemplo'>MODPER[\"1\",\"Juan\",\"López\"]</td>");
        html.append("</tr>");
        
        // DELPER
        html.append("<tr>");
        html.append("<td><strong>DELPER</strong></td>");
        html.append("<td>Elimina una persona por ID</td>");
        html.append("<td>1. id (Número)</td>");
        html.append("<td class='ejemplo'>DELPER[\"3\"]</td>");
        html.append("</tr>");
        
        // AYUDA
        html.append("<tr>");
        html.append("<td><strong>AYUDA</strong></td>");
        html.append("<td>Muestra esta tabla de comandos</td>");
        html.append("<td>(sin parámetros)</td>");
        html.append("<td class='ejemplo'>AYUDA</td>");
        html.append("</tr>");
        
        html.append("</table>");
        
        html.append("<p style='margin-top: 20px; color: #666;'>");
        html.append("<strong>Nota:</strong> Los parámetros deben ir entre comillas dobles y separados por comas dentro de los corchetes.");
        html.append("</p>");
        
        html.append("</body></html>");
        return html.toString();
    }
    
    /**
     * Genera HTML para listar personas
     */
    public static String generarListaPersonas(List<Persona> personas, String patron) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h2 { color: #333; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top: 10px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
        html.append("th { background-color: #2196F3; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append(".info { background-color: #e7f3ff; padding: 10px; border-left: 4px solid #2196F3; margin-bottom: 15px; }");
        html.append("</style>");
        html.append("</head><body>");
        
        html.append("<h2>👥 Listado de Personas</h2>");
        html.append("<div class='info'>Patrón de búsqueda: <strong>").append(patron).append("</strong></div>");
        
        if (personas.isEmpty()) {
            html.append("<p style='color: #999;'>No se encontraron personas con el patrón especificado.</p>");
        } else {
            html.append("<p>Se encontraron <strong>").append(personas.size()).append("</strong> persona(s):</p>");
            html.append("<table>");
            html.append("<tr><th>ID</th><th>Nombre</th><th>Apellido</th></tr>");
            
            for (Persona p : personas) {
                html.append("<tr>");
                html.append("<td>").append(p.getId()).append("</td>");
                html.append("<td>").append(p.getNombre()).append("</td>");
                html.append("<td>").append(p.getApellido()).append("</td>");
                html.append("</tr>");
            }
            
            html.append("</table>");
        }
        
        html.append("</body></html>");
        return html.toString();
    }
    
    /**
     * Genera HTML para mensaje de éxito
     */
    public static String generarExito(String titulo, String mensaje) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".success { background-color: #d4edda; color: #155724; padding: 15px; border: 1px solid #c3e6cb; border-radius: 5px; }");
        html.append("h2 { margin-top: 0; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<div class='success'>");
        html.append("<h2>✅ ").append(titulo).append("</h2>");
        html.append("<p>").append(mensaje).append("</p>");
        html.append("</div>");
        html.append("</body></html>");
        return html.toString();
    }
    
    /**
     * Genera HTML para mensaje de error
     */
    public static String generarError(String titulo, String mensaje) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".error { background-color: #f8d7da; color: #721c24; padding: 15px; border: 1px solid #f5c6cb; border-radius: 5px; }");
        html.append("h2 { margin-top: 0; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<div class='error'>");
        html.append("<h2>❌ ").append(titulo).append("</h2>");
        html.append("<p>").append(mensaje).append("</p>");
        html.append("</div>");
        html.append("</body></html>");
        return html.toString();
    }
}
