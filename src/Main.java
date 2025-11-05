/**
 * Punto de entrada principal del sistema
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("   SISTEMA DE GESTIÓN VÍA E-MAIL - PROYECTO 1");
        System.out.println("   Tecnología Web - INF513");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println();
        
        try {
            // Crear y arrancar el servicio
            servicio.MailService mailService = new servicio.MailService();
            
            // Agregar hook para shutdown graceful
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n🛑 Deteniendo sistema...");
                mailService.stop();
                
                // Cerrar conexión a DB
                try {
                    datos.DBConnection.getInstance().close();
                } catch (Exception e) {
                    System.err.println("Error cerrando DB: " + e.getMessage());
                }
            }));
            
            // Iniciar loop infinito
            mailService.start();
            
        } catch (Exception e) {
            System.err.println("❌ Error fatal al iniciar el sistema:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
