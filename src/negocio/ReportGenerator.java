package negocio;

import conexion.HTMLResponseBuilder;
import datos.*;
import java.util.*;

/**
 * Generador de reportes y estadísticas con gráficos HTML
 * Grupo 17SA - 4 comandos de análisis de datos
 */
public class ReportGenerator {
    
    private ActividadDAO actividadDAO = new ActividadDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private PagoDAO pagoDAO = new PagoDAO();
    
    /**
     * REPACT - Reporte de actividades por tipo
     * Muestra gráfico de barras con cantidad de actividades por tipo
     */
    public String reporteActividadesPorTipo() {
        try {
            Map<String, Integer> stats = actividadDAO.contarPorTipo();
            
            if (stats.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Reporte de Actividades",
                    HTMLResponseBuilder.info("No hay actividades registradas"));
            }
            
            StringBuilder content = new StringBuilder();
            content.append("<h2 style='color:#475569;margin-bottom:20px'>📊 Actividades por Tipo</h2>");
            
            // Encontrar el máximo para escalar las barras
            int max = stats.values().stream().mapToInt(Integer::intValue).max().orElse(1);
            
            // Generar gráficos de barras
            String[] colors = {"linear-gradient(90deg,#667eea,#764ba2)", 
                              "linear-gradient(90deg,#f093fb,#f5576c)",
                              "linear-gradient(90deg,#4facfe,#00f2fe)",
                              "linear-gradient(90deg,#43e97b,#38f9d7)",
                              "linear-gradient(90deg,#fa709a,#fee140)"};
            int colorIndex = 0;
            
            int total = 0;
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                String tipo = entry.getKey();
                int cantidad = entry.getValue();
                total += cantidad;
                
                content.append(HTMLResponseBuilder.buildBarChart(
                    tipo, cantidad, max, colors[colorIndex % colors.length]
                ));
                colorIndex++;
            }
            
            content.append(HTMLResponseBuilder.buildStatCard("TOTAL ACTIVIDADES", String.valueOf(total)));
            
            // Agregar gráfico circular SVG
            content.append(generarGraficoCircularSVG(stats, "Distribución de Actividades"));
            
            return HTMLResponseBuilder.wrapHTML("Reporte de Actividades", content.toString());
            
        } catch (Exception e) {
            return HTMLResponseBuilder.wrapHTML("Error", 
                HTMLResponseBuilder.error("Error al generar reporte: " + e.getMessage()));
        }
    }
    
    /**
     * REPUSU - Reporte de usuarios por rol
     * Muestra distribución de usuarios por rol con gráfico circular
     */
    public String reporteUsuariosPorRol() {
        try {
            Map<String, Integer> stats = usuarioDAO.contarPorRol();
            
            if (stats.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Reporte de Usuarios",
                    HTMLResponseBuilder.info("No hay usuarios registrados"));
            }
            
            StringBuilder content = new StringBuilder();
            content.append("<h2 style='color:#475569;margin-bottom:20px'>👥 Usuarios por Rol</h2>");
            
            int total = stats.values().stream().mapToInt(Integer::intValue).sum();
            
            // Gráficos circulares de porcentaje
            content.append("<div style='display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:20px;margin:20px 0'>");
            
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                String rol = entry.getKey();
                int cantidad = entry.getValue();
                int porcentaje = total > 0 ? (cantidad * 100 / total) : 0;
                
                content.append(HTMLResponseBuilder.buildProgressCircle(
                    rol + " (" + cantidad + ")", porcentaje
                ));
            }
            
            content.append("</div>");
            content.append(HTMLResponseBuilder.buildStatCard("TOTAL USUARIOS", String.valueOf(total)));
            
            // Agregar gráfico de dona SVG
            content.append(generarGraficoCircularSVG(stats, "Distribución de Usuarios"));
            
            return HTMLResponseBuilder.wrapHTML("Reporte de Usuarios", content.toString());
            
        } catch (Exception e) {
            return HTMLResponseBuilder.wrapHTML("Error",
                HTMLResponseBuilder.error("Error al generar reporte: " + e.getMessage()));
        }
    }
    
    /**
     * REPVEH - Reporte de vehículos por tipo
     * Muestra distribución de vehículos con gráfico de barras
     */
    public String reporteVehiculosPorTipo() {
        try {
            Map<String, Integer> stats = vehiculoDAO.contarPorTipo();
            
            if (stats.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Reporte de Vehículos",
                    HTMLResponseBuilder.info("No hay vehículos registrados"));
            }
            
            StringBuilder content = new StringBuilder();
            content.append("<h2 style='color:#475569;margin-bottom:20px'>🚗 Vehículos por Tipo</h2>");
            
            int max = stats.values().stream().mapToInt(Integer::intValue).max().orElse(1);
            String[] colors = {"linear-gradient(90deg,#667eea,#764ba2)", 
                              "linear-gradient(90deg,#f093fb,#f5576c)",
                              "linear-gradient(90deg,#4facfe,#00f2fe)"};
            int colorIndex = 0;
            int total = 0;
            
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                String tipo = entry.getKey();
                int cantidad = entry.getValue();
                total += cantidad;
                
                content.append(HTMLResponseBuilder.buildBarChart(
                    tipo, cantidad, max, colors[colorIndex % colors.length]
                ));
                colorIndex++;
            }
            
            content.append(HTMLResponseBuilder.buildStatCard("TOTAL VEHÍCULOS", String.valueOf(total)));
            
            // Agregar gráfico circular SVG
            content.append(generarGraficoCircularSVG(stats, "Distribución de Vehículos"));
            
            return HTMLResponseBuilder.wrapHTML("Reporte de Vehículos", content.toString());
            
        } catch (Exception e) {
            return HTMLResponseBuilder.wrapHTML("Error",
                HTMLResponseBuilder.error("Error al generar reporte: " + e.getMessage()));
        }
    }
    
    /**
     * REPPAG - Reporte de pagos por método
     * Muestra distribución de pagos y total recaudado
     */
    public String reportePagosPorMetodo() {
        try {
            Map<String, Object[]> stats = pagoDAO.estadisticasPorMetodo(); // {metodo -> [cantidad, total]}
            
            if (stats.isEmpty()) {
                return HTMLResponseBuilder.wrapHTML("Reporte de Pagos",
                    HTMLResponseBuilder.info("No hay pagos registrados"));
            }
            
            StringBuilder content = new StringBuilder();
            content.append("<h2 style='color:#475569;margin-bottom:20px'>💰 Pagos por Método</h2>");
            
            // Tabla de pagos
            String[] headers = {"Método de Pago", "Cantidad", "Total Recaudado (Bs.)"};
            java.util.List<String[]> rows = new java.util.ArrayList<>();
            
            double totalGeneral = 0;
            int cantidadTotal = 0;
            
            for (Map.Entry<String, Object[]> entry : stats.entrySet()) {
                String metodo = entry.getKey();
                Object[] data = entry.getValue();
                int cantidad = (Integer) data[0];
                double total = (Double) data[1];
                
                totalGeneral += total;
                cantidadTotal += cantidad;
                
                rows.add(new String[]{
                    metodo,
                    String.valueOf(cantidad),
                    String.format("%.2f", total)
                });
            }
            
            content.append(HTMLResponseBuilder.buildTable(headers, 
                rows.toArray(new String[rows.size()][])));
            
            // Gráfico de barras horizontal para métodos de pago
            content.append("<h3 style='color:#475569;margin-top:30px'>Comparación Visual</h3>");
            double maxTotal = stats.values().stream()
                .mapToDouble(data -> (Double)data[1])
                .max().orElse(1.0);
            
            String[] colores = {"#667eea", "#f093fb", "#4facfe", "#43e97b", "#fa709a"};
            int idx = 0;
            for (Map.Entry<String, Object[]> entry : stats.entrySet()) {
                String metodo = entry.getKey();
                double total = (Double) entry.getValue()[1];
                int porcentaje = (int)((total / maxTotal) * 100);
                
                content.append(String.format(
                    "<div style='margin:15px 0'>" +
                    "<div style='display:flex;justify-content:space-between;margin-bottom:5px'>" +
                    "<span style='font-weight:600;color:#475569'>%s</span>" +
                    "<span style='color:#64748b'>Bs. %.2f</span></div>" +
                    "<div style='background:#e2e8f0;border-radius:10px;height:25px;overflow:hidden'>" +
                    "<div style='background:%s;height:100%%;width:%d%%;border-radius:10px;transition:width 0.3s'></div>" +
                    "</div></div>",
                    metodo, total, colores[idx % colores.length], porcentaje
                ));
                idx++;
            }
            
            // Tarjetas de resumen
            content.append("<div style='display:grid;grid-template-columns:1fr 1fr;gap:15px;margin-top:20px'>");
            content.append(HTMLResponseBuilder.buildStatCard("TOTAL PAGOS", String.valueOf(cantidadTotal)));
            content.append(HTMLResponseBuilder.buildStatCard("TOTAL RECAUDADO", 
                String.format("Bs. %.2f", totalGeneral)));
            content.append("</div>");
            
            return HTMLResponseBuilder.wrapHTML("Reporte de Pagos", content.toString());
            
        } catch (Exception e) {
            return HTMLResponseBuilder.wrapHTML("Error",
                HTMLResponseBuilder.error("Error al generar reporte: " + e.getMessage()));
        }
    }
    
    /**
     * Genera un gráfico circular (pie chart) en SVG embebido en HTML
     * Compatible con clientes de correo
     */
    private String generarGraficoCircularSVG(Map<String, Integer> data, String titulo) {
        if (data.isEmpty()) return "";
        
        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return "";
        
        StringBuilder svg = new StringBuilder();
        svg.append("<div style='margin-top:30px;text-align:center'>");
        svg.append("<h3 style='color:#475569;margin-bottom:15px'>").append(titulo).append("</h3>");
        svg.append("<svg width='300' height='300' viewBox='0 0 200 200' style='max-width:100%;height:auto'>");
        
        String[] colores = {"#667eea", "#f093fb", "#4facfe", "#43e97b", "#fa709a", "#feca57", "#ff6b6b", "#48dbfb"};
        
        double currentAngle = 0;
        int colorIdx = 0;
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey();
            int valor = entry.getValue();
            double porcentaje = (valor * 100.0) / total;
            double angle = (valor * 360.0) / total;
            
            // Calcular coordenadas del arco
            double startAngle = currentAngle * Math.PI / 180;
            double endAngle = (currentAngle + angle) * Math.PI / 180;
            
            double x1 = 100 + 80 * Math.cos(startAngle);
            double y1 = 100 + 80 * Math.sin(startAngle);
            double x2 = 100 + 80 * Math.cos(endAngle);
            double y2 = 100 + 80 * Math.sin(endAngle);
            
            int largeArc = angle > 180 ? 1 : 0;
            
            String color = colores[colorIdx % colores.length];
            
            svg.append(String.format(
                "<path d='M100,100 L%.2f,%.2f A80,80 0 %d,1 %.2f,%.2f Z' fill='%s' stroke='#fff' stroke-width='2'>" +
                "<title>%s: %d (%.1f%%)</title></path>",
                x1, y1, largeArc, x2, y2, color, label, valor, porcentaje
            ));
            
            currentAngle += angle;
            colorIdx++;
        }
        
        // Círculo central para efecto "dona"
        svg.append("<circle cx='100' cy='100' r='50' fill='white'/>");
        svg.append("<text x='100' y='105' text-anchor='middle' font-size='20' font-weight='bold' fill='#475569'>");
        svg.append(total).append("</text>");
        
        svg.append("</svg>");
        
        // Leyenda
        svg.append("<div style='margin-top:15px;display:inline-block;text-align:left'>");
        colorIdx = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey();
            int valor = entry.getValue();
            double porcentaje = (valor * 100.0) / total;
            String color = colores[colorIdx % colores.length];
            
            svg.append(String.format(
                "<div style='margin:5px 0;font-size:14px'>" +
                "<span style='display:inline-block;width:12px;height:12px;background:%s;margin-right:8px;border-radius:2px'></span>" +
                "<span style='color:#475569'>%s: <strong>%d</strong> (%.1f%%)</span></div>",
                color, label, valor, porcentaje
            ));
            colorIdx++;
        }
        svg.append("</div></div>");
        
        return svg.toString();
    }
}
