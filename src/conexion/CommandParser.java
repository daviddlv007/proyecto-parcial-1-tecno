package conexion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser de comandos desde el Subject del correo
 * Formato esperado: COMANDO["param1","param2",...]
 * 
 * Comandos soportados:
 * - AYUDA (sin parámetros)
 * - LISUSU["*"] o LISUSU["patron"]
 * - INSUSU["nombre","apellido","email",...] (7 parámetros)
 * - MODUSU["id","nombre","apellido",...] (8 parámetros)
 * - DELUSU["id"]
 * - Similar para: ACT, VEH, SES, INS, PAG, etc.
 */
public class CommandParser {
    
    /**
     * Parsea el subject del correo y extrae comando y parámetros
     */
    public static Comando parse(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return new Comando("INVALID", new ArrayList<>(), "Subject vacío");
        }
        
        subject = subject.trim();
        
        // Extraer comando (6 caracteres según clase: 3 orden + 3 tabla)
        Pattern commandPattern = Pattern.compile("^([A-Z]{6})");
        Matcher commandMatcher = commandPattern.matcher(subject);
        
        if (!commandMatcher.find()) {
            // Intentar comando AYUDA (5 letras)
            if (subject.toUpperCase().startsWith("AYUDA")) {
                return new Comando("AYUDA", new ArrayList<>(), null);
            }
            return new Comando("INVALID", new ArrayList<>(), "Formato de comando inválido. Use: COMANDO[\"p1\",\"p2\"]");
        }
        
        String comando = commandMatcher.group(1);
        
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
     * Valida que el comando sea uno de los permitidos (45 comandos totales)
     */
    public static boolean esComandoValido(String comando) {
        return comando != null && (
            // Especial
            comando.equals("AYUDA") ||
            
            // ========== TABLAS CATÁLOGO (20 comandos) ==========
            
            // Rol: LIS ROL, INS ROL, MOD ROL, DEL ROL
            comando.equals("LISROL") ||
            comando.equals("INSROL") ||
            comando.equals("MODROL") ||
            comando.equals("DELROL") ||
            
            // Tipo Vehículo: LIS TVH, INS TVH, MOD TVH, DEL TVH
            comando.equals("LISTVH") ||
            comando.equals("INSTVH") ||
            comando.equals("MODTVH") ||
            comando.equals("DELTVH") ||
            
            // Tipo Actividad: LIS TAC, INS TAC, MOD TAC, DEL TAC
            comando.equals("LISTAC") ||
            comando.equals("INSTAC") ||
            comando.equals("MODTAC") ||
            comando.equals("DELTAC") ||
            
            // Tipo Pago: LIS TPG, INS TPG, MOD TPG, DEL TPG
            comando.equals("LISTPG") ||
            comando.equals("INSTPG") ||
            comando.equals("MODTPG") ||
            comando.equals("DELTPG") ||
            
            // Método Pago: LIS MPG, INS MPG, MOD MPG, DEL MPG
            comando.equals("LISMPG") ||
            comando.equals("INSMPG") ||
            comando.equals("MODMPG") ||
            comando.equals("DELMPG") ||
            
            // ========== TABLAS PRINCIPALES (24 comandos) ==========
            
            // Usuario: LIS USU, INS USU, MOD USU, DEL USU
            comando.equals("LISUSU") ||
            comando.equals("INSUSU") ||
            comando.equals("MODUSU") ||
            comando.equals("DELUSU") ||
            
            // Vehículo: LIS VEH, INS VEH, MOD VEH, DEL VEH
            comando.equals("LISVEH") ||
            comando.equals("INSVEH") ||
            comando.equals("MODVEH") ||
            comando.equals("DELVEH") ||
            
            // Actividad: LIS ACT, INS ACT, MOD ACT, DEL ACT
            comando.equals("LISACT") ||
            comando.equals("INSACT") ||
            comando.equals("MODACT") ||
            comando.equals("DELACT") ||
            
            // Sesión: LIS SES, INS SES, MOD SES, DEL SES
            comando.equals("LISSES") ||
            comando.equals("INSSES") ||
            comando.equals("MODSES") ||
            comando.equals("DELSES") ||
            
            // Inscripción: LIS INS, INS INS, MOD INS, DEL INS
            comando.equals("LISINS") ||
            comando.equals("INSINS") ||
            comando.equals("MODINS") ||
            comando.equals("DELINS") ||
            
            // Pago: LIS PAG, INS PAG, MOD PAG, DEL PAG
            comando.equals("LISPAG") ||
            comando.equals("INSPAG") ||
            comando.equals("MODPAG") ||
            comando.equals("DELPAG") ||
            
            // ========== REPORTES Y ESTADÍSTICAS (4 comandos) ==========
            comando.equals("REPACT") ||
            comando.equals("REPUSU") ||
            comando.equals("REPVEH") ||
            comando.equals("REPPAG")
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
        
        public String getMensajeError() {
            return error;
        }
        
        public boolean esValido() {
            return esComandoValido(nombre);
        }
        
        @Override
        public String toString() {
            return "Comando{" +
                   "nombre='" + nombre + '\'' +
                   ", parametros=" + parametros +
                   ", error='" + error + '\'' +
                   '}';
        }
    }
}
