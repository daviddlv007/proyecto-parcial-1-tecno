package datos;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {
    
    public List<Vehiculo> listar(String patron) throws SQLException, IOException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM vehiculo ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM vehiculo WHERE placa ILIKE ? OR marca ILIKE ? OR modelo ILIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String busqueda = "%" + patron + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Vehiculo v = new Vehiculo();
            v.setId(rs.getInt("id"));
            v.setPlaca(rs.getString("placa"));
            v.setMarca(rs.getString("marca"));
            v.setModelo(rs.getString("modelo"));
            v.setAnio(rs.getInt("anio"));
            v.setTipoVehiculoId(rs.getInt("tipo_vehiculo_id"));
            v.setEstadoVehiculo(rs.getString("estado_vehiculo"));
            v.setCapacidad(rs.getInt("capacidad"));
            vehiculos.add(v);
        }
        
        rs.close();
        stmt.close();
        
        return vehiculos;
    }
    
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 7) {
            throw new IllegalArgumentException("INSVEH requiere 7 parámetros: placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO vehiculo (placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, params.get(0));
        stmt.setString(2, params.get(1));
        stmt.setString(3, params.get(2));
        stmt.setInt(4, Integer.parseInt(params.get(3)));
        stmt.setInt(5, Integer.parseInt(params.get(4)));
        stmt.setString(6, params.get(5));
        stmt.setInt(7, Integer.parseInt(params.get(6)));
        
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
        if (params.size() != 8) {
            throw new IllegalArgumentException("MODVEH requiere 8 parámetros: id, placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE vehiculo SET placa=?, marca=?, modelo=?, anio=?, tipo_vehiculo_id=?, estado_vehiculo=?, capacidad=? WHERE id=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, params.get(1));
        stmt.setString(2, params.get(2));
        stmt.setString(3, params.get(3));
        stmt.setInt(4, Integer.parseInt(params.get(4)));
        stmt.setInt(5, Integer.parseInt(params.get(5)));
        stmt.setString(6, params.get(6));
        stmt.setInt(7, Integer.parseInt(params.get(7)));
        stmt.setInt(8, Integer.parseInt(params.get(0)));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE vehiculo SET estado_vehiculo='no_disponible' WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }

    // === MÉTODO PARA REPORTES ===
    public java.util.Map<String, Integer> contarPorTipo() {
        java.util.Map<String, Integer> resultado = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT tv.nombre_tipo, COUNT(*) as cantidad " +
                        "FROM vehiculo v " +
                        "JOIN tipo_vehiculo tv ON v.tipo_vehiculo_id = tv.id " +
                        "GROUP BY tv.nombre_tipo ORDER BY cantidad DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                resultado.put(rs.getString("nombre_tipo"), rs.getInt("cantidad"));
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
