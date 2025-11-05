package datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Persona
 * Implementa CRUD básico
 */
public class PersonaDAO {
    
    /**
     * Lista todas las personas o filtra por patrón
     * @param patron "*" para todos, o un nombre/apellido para buscar
     */
    public List<Persona> listar(String patron) throws SQLException, java.io.IOException {
        List<Persona> personas = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT id, nombre, apellido FROM persona ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT id, nombre, apellido FROM persona " +
                  "WHERE LOWER(nombre) LIKE LOWER(?) OR LOWER(apellido) LIKE LOWER(?) " +
                  "ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + patron + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getInt("id"));
            p.setNombre(rs.getString("nombre"));
            p.setApellido(rs.getString("apellido"));
            personas.add(p);
        }
        
        rs.close();
        stmt.close();
        
        return personas;
    }
    
    /**
     * Inserta una nueva persona
     */
    public boolean insertar(String nombre, String apellido) throws SQLException, java.io.IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO persona (nombre, apellido) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, apellido);
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    /**
     * Modifica una persona existente
     */
    public boolean modificar(int id, String nombre, String apellido) throws SQLException, java.io.IOException {
        // Verificar que existe
        if (!existe(id)) {
            return false;
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE persona SET nombre = ?, apellido = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, apellido);
        stmt.setInt(3, id);
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    /**
     * Elimina una persona por ID
     */
    public boolean eliminar(int id) throws SQLException, java.io.IOException {
        // Verificar que existe
        if (!existe(id)) {
            return false;
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "DELETE FROM persona WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        
        int rows = stmt.executeUpdate();
        stmt.close();
        
        return rows > 0;
    }
    
    /**
     * Verifica si existe una persona con el ID dado
     */
    public boolean existe(int id) throws SQLException, java.io.IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "SELECT COUNT(*) FROM persona WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        
        rs.close();
        stmt.close();
        
        return count > 0;
    }
    
    /**
     * Obtiene una persona por ID
     */
    public Persona obtener(int id) throws SQLException, java.io.IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "SELECT id, nombre, apellido FROM persona WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        
        Persona p = null;
        if (rs.next()) {
            p = new Persona();
            p.setId(rs.getInt("id"));
            p.setNombre(rs.getString("nombre"));
            p.setApellido(rs.getString("apellido"));
        }
        
        rs.close();
        stmt.close();
        
        return p;
    }
}
