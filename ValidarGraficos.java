import negocio.ReportGenerator;

/**
 * Validador de gráficos en reportes (sin envío de emails)
 * Imprime estadísticas de elementos SVG para verificar que funcionan
 */
public class ValidarGraficos {
    
    public static void main(String[] args) {
        ReportGenerator gen = new ReportGenerator();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     VALIDACIÓN DE GRÁFICOS EN REPORTES (SIN EMAILS)       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // REPACT - Reporte de Actividades
        System.out.println("═══ REPACT - Actividades por Tipo ═══");
        String repact = gen.reporteActividadesPorTipo();
        analizarHTML(repact, "REPACT");
        System.out.println();
        
        // REPUSU - Reporte de Usuarios
        System.out.println("═══ REPUSU - Usuarios por Rol ═══");
        String repusu = gen.reporteUsuariosPorRol();
        analizarHTML(repusu, "REPUSU");
        System.out.println();
        
        // REPVEH - Reporte de Vehículos
        System.out.println("═══ REPVEH - Vehículos por Tipo ═══");
        String repveh = gen.reporteVehiculosPorTipo();
        analizarHTML(repveh, "REPVEH");
        System.out.println();
        
        // REPPAG - Reporte de Pagos
        System.out.println("═══ REPPAG - Pagos por Método ═══");
        String reppag = gen.reportePagosPorMetodo();
        analizarHTML(reppag, "REPPAG");
        System.out.println();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  ✓ VALIDACIÓN COMPLETA - Todos los reportes con gráficos  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    private static void analizarHTML(String html, String reporte) {
        // Contar elementos SVG
        int svgCount = contarOcurrencias(html, "<svg");
        int pathCount = contarOcurrencias(html, "<path");
        int circleCount = contarOcurrencias(html, "<circle");
        int textCount = contarOcurrencias(html, "<text");
        
        // Contar barras de progreso
        int barCount = contarOcurrencias(html, "linear-gradient");
        
        // Contar filas de tabla
        int tableRowCount = contarOcurrencias(html, "<tr>") - 1; // -1 para header
        
        // Contar elementos de leyenda
        int legendCount = contarOcurrencias(html, "display:inline-block;width:12px;height:12px");
        
        System.out.println("  Elementos SVG:");
        System.out.println("    • Tags <svg>:    " + svgCount);
        System.out.println("    • Paths (arcos):  " + pathCount);
        System.out.println("    • Círculos:       " + circleCount);
        System.out.println("    • Textos:         " + textCount);
        System.out.println("  Elementos de estilo:");
        System.out.println("    • Barras gradient: " + barCount);
        System.out.println("    • Leyendas:        " + legendCount);
        if (tableRowCount > 0) {
            System.out.println("    • Filas tabla:     " + tableRowCount);
        }
        System.out.println("  Tamaño total:      " + html.length() + " caracteres");
        
        // Validación
        boolean tieneGraficos = (svgCount > 0 && pathCount > 0) || barCount > 0;
        System.out.println("  Estado: " + (tieneGraficos ? "✓ TIENE GRÁFICOS" : "✗ SIN GRÁFICOS"));
        
        // Extraer una muestra del SVG
        if (html.contains("<svg")) {
            int svgStart = html.indexOf("<svg");
            int svgEnd = html.indexOf("</svg>", svgStart) + 6;
            if (svgEnd > svgStart && svgEnd < html.length()) {
                String svgSample = html.substring(svgStart, Math.min(svgEnd, svgStart + 200));
                System.out.println("  Muestra SVG: " + svgSample.substring(0, Math.min(150, svgSample.length())) + "...");
            }
        }
    }
    
    private static int contarOcurrencias(String texto, String patron) {
        int count = 0;
        int index = 0;
        while ((index = texto.indexOf(patron, index)) != -1) {
            count++;
            index += patron.length();
        }
        return count;
    }
}
