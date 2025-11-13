package datos;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Usuario usando JDBC puro (sin frameworks)
 */
public class UsuarioDAO {
    
    /**
     * Lista todos los usuarios o filtra por patrón
     */
    public List<Usuario> listar(String patron) throws SQLException, IOException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM usuario ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM usuario WHERE nombre ILIKE ? OR apellido ILIKE ? OR email ILIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String busqueda = "%" + patron + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setFechaNacimiento(rs.getString("fecha_nacimiento"));
            u.setGenero(rs.getString("genero"));
            u.setTipoDocumento(rs.getString("tipo_documento"));
            u.setNumeroDocumento(rs.getString("numero_documento"));
            u.setEmail(rs.getString("email"));
            u.setTelefono(rs.getString("telefono"));
            u.setDireccion(rs.getString("direccion"));
            u.setRolId(rs.getInt("rol_id"));
            u.setEstadoUsuario(rs.getString("estado_usuario"));
            usuarios.add(u);
        }
        
        rs.close();
        stmt.close();
        
        return usuarios;
    }
    
        /**
     * Inserta un nuevo usuario
     * Params: [nombre, apellido, email, telefono, direccion, contrasena, rol_id]
     */
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 11) {
            throw new IllegalArgumentException("INSUSU requiere 11 parámetros");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO usuario (nombre, apellido, fecha_nacimiento, genero, tipo_documento, " +
                     "numero_documento, email, telefono, direccion, contrasena, rol_id, estado_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'activo')";
        
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, params.get(0)); // nombre
        stmt.setString(2, params.get(1)); // apellido
        stmt.setDate(3, params.get(2).isEmpty() ? null : Date.valueOf(params.get(2))); // fecha_nacimiento
        stmt.setString(4, params.get(3)); // genero
        stmt.setString(5, params.get(4)); // tipo_documento
        stmt.setString(6, params.get(5)); // numero_documento
        stmt.setString(7, params.get(6)); // email
        stmt.setString(8, params.get(7)); // telefono
        stmt.setString(9, params.get(8)); // direccion
        stmt.setString(10, params.get(9)); // contrasena
        stmt.setInt(11, Integer.parseInt(params.get(10))); // rol_id
        
        int rowsAffected = stmt.executeUpdate();
        
        int id = -1;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        
        rs.close();
        stmt.close();
        
        return id;
    }
    
    /**
     * Modifica un usuario existente
     * Parámetros: id, nombre, apellido, fecha_nac, genero, tipo_doc, num_doc, email, telefono, direccion, contrasena, rol_id
     */
    public boolean modificar(List<String> params) throws SQLException, IOException {
        if (params.size() != 12) {
            throw new IllegalArgumentException("MODUSU requiere 12 parámetros (id + 11 campos)");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE usuario SET nombre=?, apellido=?, fecha_nacimiento=?, genero=?, " +
                     "tipo_documento=?, numero_documento=?, email=?, telefono=?, direccion=?, " +
                     "contrasena=?, rol_id=? WHERE id=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, params.get(1)); // nombre
        stmt.setString(2, params.get(2)); // apellido
        stmt.setDate(3, params.get(3).isEmpty() ? null : Date.valueOf(params.get(3))); // fecha_nacimiento
        stmt.setString(4, params.get(4)); // genero
        stmt.setString(5, params.get(5)); // tipo_documento
        stmt.setString(6, params.get(6)); // numero_documento
        stmt.setString(7, params.get(7)); // email
        stmt.setString(8, params.get(8)); // telefono
        stmt.setString(9, params.get(9)); // direccion
        stmt.setString(10, params.get(10)); // contrasena
        stmt.setInt(11, Integer.parseInt(params.get(11))); // rol_id
        stmt.setInt(12, Integer.parseInt(params.get(0))); // id (WHERE)
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Elimina un usuario (eliminación lógica)
     */
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        // Eliminación lógica
        String sql = "UPDATE usuario SET estado_usuario='inactivo' WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
}
