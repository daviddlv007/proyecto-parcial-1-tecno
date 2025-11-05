import conexion.SMTPClient;

/**
 * Test simple para verificar que MailHog funciona
 */
public class TestMailHog {
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("   TEST DE MAILHOG - Envío de Correo");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println();
        
        try {
            // Crear cliente SMTP
            SMTPClient smtp = new SMTPClient();
            
            // Enviar correo de prueba
            System.out.println("📧 Enviando correo de prueba a MailHog...");
            
            String htmlBody = "<html><body>" +
                "<h2>🎉 ¡Test Exitoso!</h2>" +
                "<p>Si ves este correo, significa que MailHog está funcionando correctamente.</p>" +
                "<p><strong>Próximo paso:</strong> Abre <a href='http://localhost:8025'>http://localhost:8025</a> para ver este correo en la interfaz web.</p>" +
                "</body></html>";
            
            smtp.sendEmail(
                "test@localhost", 
                "✅ Test MailHog - Sistema Funcional", 
                htmlBody
            );
            
            System.out.println("✅ ¡Correo enviado exitosamente!");
            System.out.println();
            System.out.println("════════════════════════════════════════════════════════");
            System.out.println("   INSTRUCCIONES:");
            System.out.println("   1. Abre tu navegador");
            System.out.println("   2. Ve a: http://localhost:8025");
            System.out.println("   3. Deberías ver el correo de prueba");
            System.out.println("════════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
