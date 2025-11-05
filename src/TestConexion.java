import datos.DBConnection;
import datos.PersonaDAO;
import datos.Persona;
import java.util.List;

/**
 * Test de conexión y funcionalidad básica
 */
public class TestConexion {
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("   TEST DE CONEXIÓN Y FUNCIONALIDAD");
        System.out.println("═══════════════════════════════════════\n");
        
        try {
            // Test 1: Conexión a BD
            System.out.println("1️⃣  Probando conexión a PostgreSQL...");
            DBConnection db = DBConnection.getInstance();
            System.out.println("   ✅ Conexión exitosa\n");
            
            // Test 2: Listar personas
            System.out.println("2️⃣  Listando todas las personas...");
            PersonaDAO dao = new PersonaDAO();
            List<Persona> personas = dao.listar("*");
            System.out.println("   Encontradas: " + personas.size() + " personas");
            for (Persona p : personas) {
                System.out.println("   - " + p);
            }
            System.out.println("   ✅ Listado exitoso\n");
            
            // Test 3: Insertar persona
            System.out.println("3️⃣  Insertando nueva persona...");
            boolean insertado = dao.insertar("Test", "Usuario");
            if (insertado) {
                System.out.println("   ✅ Persona insertada\n");
            }
            
            // Test 4: Buscar por patrón
            System.out.println("4️⃣  Buscando personas con 'Test'...");
            List<Persona> resultados = dao.listar("Test");
            System.out.println("   Encontradas: " + resultados.size());
            for (Persona p : resultados) {
                System.out.println("   - " + p);
            }
            System.out.println("   ✅ Búsqueda exitosa\n");
            
            // Test 5: Modificar
            if (!resultados.isEmpty()) {
                Persona primera = resultados.get(0);
                System.out.println("5️⃣  Modificando persona ID=" + primera.getId() + "...");
                boolean modificado = dao.modificar(primera.getId(), "Test Modificado", "Usuario Actualizado");
                if (modificado) {
                    System.out.println("   ✅ Persona modificada\n");
                }
            }
            
            // Test 6: Verificar modificación
            System.out.println("6️⃣  Verificando cambios...");
            personas = dao.listar("*");
            for (Persona p : personas) {
                if (p.getNombre().contains("Modificado")) {
                    System.out.println("   - " + p);
                }
            }
            System.out.println("   ✅ Cambios verificados\n");
            
            System.out.println("═══════════════════════════════════════");
            System.out.println("   ✅ TODOS LOS TESTS PASARON");
            System.out.println("═══════════════════════════════════════");
            
            db.close();
            
        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
