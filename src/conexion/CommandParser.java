package conexion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser de comandos desde el Subject del correo
 * Formato esperado: COMANDO["param1","param2",...]
 */
public class CommandParser {
    
    /**
     * Parsea el subject del correo y extrae comando y parámetros
     * @return Comando con nombre y parámetros parseados
     */
    public static Comando parse(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return new Comando("INVALID", new ArrayList<>(), "Subject vacío");
        }
        
        subject = subject.trim();
        
        // Patrón: COMANDO o COMANDO["param1","param2"]
        // Primero extraemos el comando (todo antes del [ o el final)
        Pattern commandPattern = Pattern.compile("^([A-Z]+)");
        Matcher commandMatcher = commandPattern.matcher(subject);
        
        if (!commandMatcher.find()) {
            return new Comando("INVALID", new ArrayList<>(), "No se encontró comando válido");
        }
        
        String comando = commandMatcher.group(1);
        
        // Comando AYUDA no necesita parámetros
        if ("AYUDA".equals(comando)) {
            return new Comando(comando, new ArrayList<>(), null);
        }
        
        // Extraer parámetros entre corchetes
        List<String> parametros = new ArrayList<>();
        
        // Buscar contenido entre [ y ]
        Pattern paramsPattern = Pattern.compile("\\[(.*)\\]");
        Matcher paramsMatcher = paramsPattern.matcher(subject);
        
        if (paramsMatcher.find()) {
            String paramsStr = paramsMatcher.group(1);
            
            // Parsear parámetros separados por coma
            // Formato: "param1","param2","param3"
            Pattern paramPattern = Pattern.compile("\"([^\"]*)\"");
            Matcher paramMatcher = paramPattern.matcher(paramsStr);
            
            while (paramMatcher.find()) {
                parametros.add(paramMatcher.group(1));
            }
        }
        
        return new Comando(comando, parametros, null);
    }
    
    /**
     * Valida que el comando sea uno de los permitidos
     */
    public static boolean esComandoValido(String comando) {
        return comando != null && (
            comando.equals("AYUDA") ||
            comando.equals("LISPER") ||
            comando.equals("INSPER") ||
            comando.equals("MODPER") ||
            comando.equals("DELPER")
        );
    }
    
    /**
     * Clase interna para representar un comando parseado
     */
    public static class Comando {
        private String nombre;
        private List<String> parametros;
        private String error;
        
        public Comando(String nombre, List<String> parametros, String error) {
            this.nombre = nombre;
            this.parametros = parametros;
            this.error = error;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public List<String> getParametros() {
            return parametros;
        }
        
        public boolean tieneError() {
            return error != null;
        }
        
        public String getError() {
            return error;
        }
        
        public int cantidadParametros() {
            return parametros.size();
        }
        
        @Override
        public String toString() {
            return String.format("Comando[%s, params=%d]", nombre, parametros.size());
        }
    }
}
