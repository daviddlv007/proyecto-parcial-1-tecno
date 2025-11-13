package datos;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAO {
    
    public List<Actividad> listar(String patron) throws SQLException, IOException {
        List<Actividad> actividades = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM actividad ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM actividad WHERE nombre ILIKE ? OR descripcion ILIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String busqueda = "%" + patron + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Actividad a = new Actividad();
            a.setId(rs.getInt("id"));
            a.setNombre(rs.getString("nombre"));
            a.setDescripcion(rs.getString("descripcion"));
            a.setTipoActividadId(rs.getInt("tipo_actividad_id"));
            a.setDuracionHoras(rs.getDouble("duracion_horas"));
            a.setEstadoActividad(rs.getString("estado_actividad"));
            a.setNivel(rs.getString("nivel"));
            actividades.add(a);
        }
        
        rs.close();
        stmt.close();
        
        return actividades;
    }
    
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 5) {
            throw new IllegalArgumentException("INSACT requiere 5 parámetros: nombre, descripcion, tipo_actividad_id, duracion_horas, nivel");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO actividad (nombre, descripcion, tipo_actividad_id, duracion_horas, estado_actividad, nivel) " +
                     "VALUES (?, ?, ?, ?, 'activa', ?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, params.get(0));
        stmt.setString(2, params.get(1));
        stmt.setInt(3, Integer.parseInt(params.get(2)));
        stmt.setDouble(4, Double.parseDouble(params.get(3)));
        stmt.setString(5, params.get(4));
        
        stmt.executeUpdate();
        
        int id = -1;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        
        rs.close();
        stmt.close();
        
        return id;
    }
    
    public boolean modificar(List<String> params) throws SQLException, IOException {
        if (params.size() != 6) {
            throw new IllegalArgumentException("MODACT requiere 6 parámetros: id, nombre, descripcion, tipo_actividad_id, duracion_horas, nivel");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE actividad SET nombre=?, descripcion=?, tipo_actividad_id=?, duracion_horas=?, nivel=? WHERE id=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, params.get(1));
        stmt.setString(2, params.get(2));
        stmt.setInt(3, Integer.parseInt(params.get(3)));
        stmt.setDouble(4, Double.parseDouble(params.get(4)));
        stmt.setString(5, params.get(5));
        stmt.setInt(6, Integer.parseInt(params.get(0)));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE actividad SET estado_actividad='inactiva' WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
}
