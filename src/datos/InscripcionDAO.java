package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Inscripcion
 */
public class InscripcionDAO {
    
    public List<Inscripcion> listar(String patron) throws SQLException, IOException {
        List<Inscripcion> inscripciones = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM inscripcion ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM inscripcion WHERE CAST(id AS TEXT) LIKE ? OR estado_inscripcion LIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String likePat = "%" + patron + "%";
            stmt.setString(1, likePat);
            stmt.setString(2, likePat);
        }
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Inscripcion i = new Inscripcion();
            i.setId(rs.getInt("id"));
            i.setAlumnoId(rs.getInt("alumno_id"));
            i.setSesionId(rs.getInt("sesion_id"));
            i.setFechaInscripcion(rs.getTimestamp("fecha_inscripcion"));
            i.setEstadoInscripcion(rs.getString("estado_inscripcion"));
            i.setTipoPagoId(rs.getInt("tipo_pago_id"));
            i.setMontoTotal(rs.getDouble("monto_total"));
            i.setCursoId((Integer) rs.getObject("curso_id"));
            i.setObservaciones(rs.getString("observaciones"));
            inscripciones.add(i);
        }
        rs.close();
        stmt.close();
        
        return inscripciones;
    }
    
    /**
     * Inserta inscripción
     * Params: [alumno_id, sesion_id, tipo_pago_id, monto_total, estado_inscripcion, curso_id, observaciones]
     */
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 7) {
            throw new IllegalArgumentException("INSINS requiere 7 parámetros");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO inscripcion (alumno_id, sesion_id, tipo_pago_id, monto_total, " +
                    "estado_inscripcion, curso_id, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(params.get(0)));
        stmt.setInt(2, Integer.parseInt(params.get(1)));
        stmt.setInt(3, Integer.parseInt(params.get(2)));
        stmt.setDouble(4, Double.parseDouble(params.get(3)));
        stmt.setString(5, params.get(4));
        
        if (params.get(5) != null && !params.get(5).trim().isEmpty()) {
            stmt.setInt(6, Integer.parseInt(params.get(5)));
        } else {
            stmt.setNull(6, Types.INTEGER);
        }
        
        stmt.setString(7, params.get(6));
        
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
     * Modifica inscripción
     * Params: [id, alumno_id, sesion_id, tipo_pago_id, monto_total, estado_inscripcion, curso_id, observaciones]
     */
    public boolean modificar(List<String> params) throws SQLException, IOException {
        if (params.size() != 8) {
            throw new IllegalArgumentException("MODINS requiere 8 parámetros (id + 7 campos)");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE inscripcion SET alumno_id=?, sesion_id=?, tipo_pago_id=?, monto_total=?, " +
                    "estado_inscripcion=?, curso_id=?, observaciones=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(params.get(1)));
        stmt.setInt(2, Integer.parseInt(params.get(2)));
        stmt.setInt(3, Integer.parseInt(params.get(3)));
        stmt.setDouble(4, Double.parseDouble(params.get(4)));
        stmt.setString(5, params.get(5));
        
        if (params.get(6) != null && !params.get(6).trim().isEmpty()) {
            stmt.setInt(6, Integer.parseInt(params.get(6)));
        } else {
            stmt.setNull(6, Types.INTEGER);
        }
        
        stmt.setString(7, params.get(7));
        stmt.setInt(8, Integer.parseInt(params.get(0)));
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    /**
     * Elimina inscripción (eliminación lógica)
     */
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE inscripcion SET estado_inscripcion='cancelada' WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
}
