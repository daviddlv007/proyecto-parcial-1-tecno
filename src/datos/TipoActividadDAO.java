package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoActividadDAO {
    
    public List<TipoActividad> listar(String patron) throws IOException {
        List<TipoActividad> tipos = new ArrayList<>();
        
        boolean listarTodos = "*".equals(patron);
        String sql = listarTodos
            ? "SELECT * FROM tipo_actividad ORDER BY id"
            : "SELECT * FROM tipo_actividad WHERE nombre ILIKE ? OR descripcion ILIKE ? ORDER BY id";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!listarTodos) {
                String searchPattern = "%" + patron + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoActividad tipo = new TipoActividad();
                tipo.setId(rs.getInt("id"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                tipo.setDescripcion(rs.getString("descripcion"));
                tipo.setActivo(true);
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            throw new IOException("Error al listar tipos de actividad: " + e.getMessage());
        }
        return tipos;
    }
    
    public boolean insertar(String nombre, String descripcion) throws IOException {
        String sql = "INSERT INTO tipo_actividad (nombre_tipo, descripcion) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al insertar tipo de actividad: " + e.getMessage());
        }
    }
    
    public boolean modificar(int id, String nombre, String descripcion, boolean activo) throws IOException {
        String sql = "UPDATE tipo_actividad SET nombre_tipo = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al modificar tipo de actividad: " + e.getMessage());
        }
    }
    
    public boolean eliminar(int id) throws IOException {
        String sql = "DELETE FROM tipo_actividad WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al eliminar tipo de actividad: " + e.getMessage());
        }
    }
}
