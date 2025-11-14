package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Pago
 */
public class PagoDAO {
    
    public List<Pago> listar(String patron) throws SQLException, IOException {
        List<Pago> pagos = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM pago ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM pago WHERE CAST(id AS TEXT) LIKE ? OR comprobante LIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String likePat = "%" + patron + "%";
            stmt.setString(1, likePat);
            stmt.setString(2, likePat);
        }
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Pago p = new Pago();
            p.setId(rs.getInt("id"));
            p.setAlumnoId(rs.getInt("alumno_id"));
            p.setInscripcionId(rs.getInt("inscripcion_id"));
            p.setFecha(rs.getTimestamp("fecha"));
            p.setMonto(rs.getDouble("monto"));
            p.setMetodoPagoId(rs.getInt("metodo_pago_id"));
            p.setComprobante(rs.getString("comprobante"));
            p.setObservaciones(rs.getString("observaciones"));
            pagos.add(p);
        }
        rs.close();
        stmt.close();
        
        return pagos;
    }
    
    /**
     * Inserta pago
     * Params: [alumno_id, inscripcion_id, monto, metodo_pago_id, comprobante, observaciones]
     */
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 6) {
            throw new IllegalArgumentException("INSPAG requiere 6 parámetros");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO pago (alumno_id, inscripcion_id, monto, metodo_pago_id, " +
                    "comprobante, observaciones) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(params.get(0)));
        stmt.setInt(2, Integer.parseInt(params.get(1)));
        stmt.setDouble(3, Double.parseDouble(params.get(2)));
        stmt.setInt(4, Integer.parseInt(params.get(3)));
        stmt.setString(5, params.get(4));
        stmt.setString(6, params.get(5));
        
        ResultSet rs = stmt.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        
        return id;
    }
    
    /**
     * Modifica pago
     * Params: [id, alumno_id, inscripcion_id, monto, metodo_pago_id, comprobante, observaciones]
     */
    public boolean modificar(List<String> params) throws SQLException, IOException {
        if (params.size() != 7) {
            throw new IllegalArgumentException("MODPAG requiere 7 parámetros (id + 6 campos)");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE pago SET alumno_id=?, inscripcion_id=?, monto=?, metodo_pago_id=?, " +
                    "comprobante=?, observaciones=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(params.get(1)));
        stmt.setInt(2, Integer.parseInt(params.get(2)));
        stmt.setDouble(3, Double.parseDouble(params.get(3)));
        stmt.setInt(4, Integer.parseInt(params.get(4)));
        stmt.setString(5, params.get(5));
        stmt.setString(6, params.get(6));
        stmt.setInt(7, Integer.parseInt(params.get(0)));
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    /**
     * Elimina pago (física)
     */
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "DELETE FROM pago WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    // === MÉTODO PARA REPORTES ===
    public java.util.Map<String, Object[]> estadisticasPorMetodo() {
        java.util.Map<String, Object[]> resultado = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT mp.nombre_metodo, COUNT(*) as cantidad, SUM(p.monto) as total " +
                        "FROM pago p " +
                        "JOIN metodo_pago mp ON p.metodo_pago_id = mp.id " +
                        "GROUP BY mp.nombre_metodo ORDER BY total DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String nombre = rs.getString("nombre_metodo");
                Integer cantidad = rs.getInt("cantidad");
                Double total = rs.getDouble("total");
                resultado.put(nombre, new Object[]{cantidad, total});
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
}

