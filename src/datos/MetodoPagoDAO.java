package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDAO {
    
    public List<MetodoPago> listar(String patron) throws IOException {
        List<MetodoPago> metodos = new ArrayList<>();
        
        boolean listarTodos = "*".equals(patron);
        String sql = listarTodos
            ? "SELECT * FROM metodo_pago ORDER BY id"
            : "SELECT * FROM metodo_pago WHERE nombre ILIKE ? OR descripcion ILIKE ? ORDER BY id";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!listarTodos) {
                String searchPattern = "%" + patron + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MetodoPago metodo = new MetodoPago();
                metodo.setId(rs.getInt("id"));
                metodo.setNombre(rs.getString("nombre_metodo"));
                metodo.setDescripcion(rs.getString("descripcion"));
                metodo.setActivo(true);
                metodos.add(metodo);
            }
        } catch (SQLException e) {
            throw new IOException("Error al listar métodos de pago: " + e.getMessage());
        }
        return metodos;
    }
    
    public boolean insertar(String nombre, String descripcion) throws IOException {
        String sql = "INSERT INTO metodo_pago (nombre_metodo, descripcion) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al insertar método de pago: " + e.getMessage());
        }
    }
    
    public boolean modificar(int id, String nombre, String descripcion, boolean activo) throws IOException {
        String sql = "UPDATE metodo_pago SET nombre_metodo = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al modificar método de pago: " + e.getMessage());
        }
    }
    
    public boolean eliminar(int id) throws IOException {
        String sql = "DELETE FROM metodo_pago WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IOException("Error al eliminar método de pago: " + e.getMessage());
        }
    }
}
