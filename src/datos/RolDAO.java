package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    
    public List<Rol> listar(String patron) throws IOException {
        List<Rol> roles = new ArrayList<>();
        
        // Si patron es "*", listar todos sin filtro
        boolean listarTodos = "*".equals(patron);
        String sql = listarTodos 
            ? "SELECT * FROM rol ORDER BY id"
            : "SELECT * FROM rol WHERE nombre_rol ILIKE ? OR descripcion ILIKE ? ORDER BY id";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!listarTodos) {
                String searchPattern = "%" + patron + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(rs.getInt("id"));
                rol.setNombre(rs.getString("nombre_rol"));
                rol.setDescripcion(rs.getString("descripcion"));
                rol.setActivo(true); // No hay columna activo en BD
                roles.add(rol);
            }
        } catch (SQLException e) {
            throw new IOException("Error al listar roles: " + e.getMessage());
        }
        return roles;
    }
    
    public boolean insertar(String nombre, String descripcion) throws IOException {
        String sql = "INSERT INTO rol (nombre_rol, descripcion) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al insertar rol: " + e.getMessage());
        }
    }
    
    public boolean modificar(int id, String nombre, String descripcion, boolean activo) throws IOException {
        String sql = "UPDATE rol SET nombre_rol = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al modificar rol: " + e.getMessage());
        }
    }
    
    public boolean eliminar(int id) throws IOException {
        // No hay columna activo, así que hacemos DELETE
        String sql = "DELETE FROM rol WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al eliminar rol: " + e.getMessage());
        }
    }
}
