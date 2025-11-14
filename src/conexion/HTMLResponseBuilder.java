package conexion;

/**
 * Constructor de respuestas HTML modernas y minimalistas
 * Grupo 17SA - Sistema de Gestión vía Email
 */
public class HTMLResponseBuilder {
    
    private static final String STYLE = 
        "<style>" +
        "body{font-family:'Segoe UI',Tahoma,sans-serif;background:#f5f7fa;margin:0;padding:20px}" +
        ".container{max-width:800px;margin:0 auto;background:#fff;border-radius:12px;box-shadow:0 2px 20px rgba(0,0,0,0.08);overflow:hidden}" +
        ".header{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#fff;padding:30px;text-align:center}" +
        ".header h1{margin:0;font-size:28px;font-weight:600}" +
        ".header p{margin:8px 0 0;opacity:0.9;font-size:14px}" +
        ".content{padding:30px}" +
        ".success{background:#ecfdf5;border-left:4px solid #10b981;padding:16px;border-radius:8px;margin-bottom:20px}" +
        ".error{background:#fef2f2;border-left:4px solid #ef4444;padding:16px;border-radius:8px;margin-bottom:20px}" +
        ".info{background:#eff6ff;border-left:4px solid#3b82f6;padding:16px;border-radius:8px;margin-bottom:20px}" +
        "table{width:100%;border-collapse:collapse;margin:20px 0}" +
        "th{background:#f8fafc;color:#475569;font-weight:600;padding:12px;text-align:left;border-bottom:2px solid #e2e8f0}" +
        "td{padding:12px;border-bottom:1px solid #e2e8f0;color:#334155}" +
        "tr:hover{background:#f8fafc}" +
        ".badge{display:inline-block;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600}" +
        ".badge-success{background:#dcfce7;color:#166534}" +
        ".badge-error{background:#fee2e2;color:#991b1b}" +
        ".badge-info{background:#dbeafe;color:#1e40af}" +
        ".footer{background:#f8fafc;padding:20px;text-align:center;font-size:13px;color:#64748b;border-top:1px solid #e2e8f0}" +
        ".chart-bar{background:#e2e8f0;height:30px;border-radius:4px;margin:8px 0;position:relative;overflow:hidden}" +
        ".chart-fill{background:linear-gradient(90deg,#667eea,#764ba2);height:100%;display:flex;align-items:center;padding:0 12px;color:#fff;font-weight:600;font-size:13px}" +
        ".stat-card{background:#f8fafc;border-radius:8px;padding:20px;margin:10px 0}" +
        ".stat-value{font-size:32px;font-weight:700;color:#667eea;margin:8px 0}" +
        ".stat-label{font-size:14px;color:#64748b;text-transform:uppercase;letter-spacing:0.5px}" +
        "</style>";
    
    public static String wrapHTML(String title, String content) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'>" + STYLE + 
               "</head><body>" +
               "<div class='container'>" +
               "<div class='header'><h1>" + title + "</h1><p>Sistema de Gestión - Grupo 17SA</p></div>" +
               "<div class='content'>" + content + "</div>" +
               "<div class='footer'>TecnoWeb 2025 • Grupo 17SA • Sistema vía Email</div>" +
               "</div></body></html>";
    }
    
    public static String success(String message) {
        return "<div class='success'><strong>✓ Éxito:</strong> " + message + "</div>";
    }
    
    public static String error(String message) {
        return "<div class='error'><strong>✗ Error:</strong> " + message + "</div>";
    }
    
    public static String info(String message) {
        return "<div class='info'><strong>ℹ Info:</strong> " + message + "</div>";
    }
    
    public static String buildTable(String[] headers, String[][] rows) {
        StringBuilder html = new StringBuilder("<table>");
        
        // Headers
        html.append("<tr>");
        for (String header : headers) {
            html.append("<th>").append(header).append("</th>");
        }
        html.append("</tr>");
        
        // Rows
        for (String[] row : rows) {
            html.append("<tr>");
            for (String cell : row) {
                html.append("<td>").append(cell != null ? cell : "-").append("</td>");
            }
            html.append("</tr>");
        }
        
        html.append("</table>");
        return html.toString();
    }
    
    public static String buildBarChart(String label, int value, int max, String color) {
        int percentage = max > 0 ? (value * 100 / max) : 0;
        return "<div style='margin:15px 0'>" +
               "<div style='font-weight:600;margin-bottom:8px;color:#475569'>" + label + "</div>" +
               "<div class='chart-bar'>" +
               "<div class='chart-fill' style='width:" + percentage + "%;background:" + color + "'>" +
               value + "</div>" +
               "</div></div>";
    }
    
    public static String buildStatCard(String label, String value) {
        return "<div class='stat-card'>" +
               "<div class='stat-label'>" + label + "</div>" +
               "<div class='stat-value'>" + value + "</div>" +
               "</div>";
    }
    
    public static String buildProgressCircle(String label, int percentage) {
        String color = percentage >= 75 ? "#10b981" : percentage >= 50 ? "#f59e0b" : "#ef4444";
        return "<div style='text-align:center;margin:20px 0'>" +
               "<div style='position:relative;width:120px;height:120px;margin:0 auto'>" +
               "<svg width='120' height='120'>" +
               "<circle cx='60' cy='60' r='50' fill='none' stroke='#e2e8f0' stroke-width='10'/>" +
               "<circle cx='60' cy='60' r='50' fill='none' stroke='" + color + "' stroke-width='10' " +
               "stroke-dasharray='" + (percentage * 3.14) + " 314' transform='rotate(-90 60 60)'/>" +
               "</svg>" +
               "<div style='position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);font-size:24px;font-weight:700;color:" + color + "'>" +
               percentage + "%</div>" +
               "</div>" +
               "<div style='margin-top:10px;font-weight:600;color:#475569'>" + label + "</div>" +
               "</div>";
    }
}
