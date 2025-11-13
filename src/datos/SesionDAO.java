package datos;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SesionDAO {
    
    public List<Sesion> listar(String patron) throws SQLException, IOException {
        List<Sesion> sesiones = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql;
        PreparedStatement stmt;
        
        if ("*".equals(patron)) {
            sql = "SELECT * FROM sesion ORDER BY id";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM sesion WHERE lugar ILIKE ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            String busqueda = "%" + patron + "%";
            stmt.setString(1, busqueda);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Sesion s = new Sesion();
            s.setId(rs.getInt("id"));
            s.setActividadId(rs.getInt("actividad_id"));
            s.setFecha(rs.getString("fecha"));
            s.setHoraInicio(rs.getString("hora_inicio"));
            s.setHoraFin(rs.getString("hora_fin"));
            s.setInstructorId(rs.getInt("instructor_id"));
            s.setVehiculoId(rs.getInt("vehiculo_id"));
            s.setLugar(rs.getString("lugar"));
            s.setEstadoSesion(rs.getString("estado_sesion"));
            s.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
            sesiones.add(s);
        }
        
        rs.close();
        stmt.close();
        
        return sesiones;
    }
    
    public int insertar(List<String> params) throws SQLException, IOException {
        if (params.size() != 8) {
            throw new IllegalArgumentException("INSSES requiere 8 parámetros: actividad_id, fecha, hora_inicio, hora_fin, instructor_id, vehiculo_id, lugar, capacidad_maxima");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "INSERT INTO sesion (actividad_id, fecha, hora_inicio, hora_fin, instructor_id, vehiculo_id, lugar, estado_sesion, capacidad_maxima) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'programada', ?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setInt(1, Integer.parseInt(params.get(0)));
        stmt.setDate(2, Date.valueOf(params.get(1)));
        
        // Agregar :00 si la hora no tiene segundos (HH:MM → HH:MM:00)
        String horaInicio = params.get(2);
        if (horaInicio.length() == 5) horaInicio += ":00";
        stmt.setTime(3, Time.valueOf(horaInicio));
        
        String horaFin = params.get(3);
        if (horaFin.length() == 5) horaFin += ":00";
        stmt.setTime(4, Time.valueOf(horaFin));
        
        stmt.setInt(5, Integer.parseInt(params.get(4)));
        stmt.setInt(6, Integer.parseInt(params.get(5)));
        stmt.setString(7, params.get(6));
        stmt.setInt(8, Integer.parseInt(params.get(7)));
        
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
        if (params.size() != 9) {
            throw new IllegalArgumentException("MODSES requiere 9 parámetros");
        }
        
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE sesion SET actividad_id=?, fecha=?, hora_inicio=?, hora_fin=?, instructor_id=?, vehiculo_id=?, lugar=?, capacidad_maxima=? WHERE id=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setInt(1, Integer.parseInt(params.get(1)));
        stmt.setDate(2, Date.valueOf(params.get(2)));
        
        // Agregar :00 si la hora no tiene segundos
        String horaInicio = params.get(3);
        if (horaInicio.length() == 5) horaInicio += ":00";
        stmt.setTime(3, Time.valueOf(horaInicio));
        
        String horaFin = params.get(4);
        if (horaFin.length() == 5) horaFin += ":00";
        stmt.setTime(4, Time.valueOf(horaFin));
        
        stmt.setInt(5, Integer.parseInt(params.get(5)));
        stmt.setInt(6, Integer.parseInt(params.get(6)));
        stmt.setString(7, params.get(7));
        stmt.setInt(8, Integer.parseInt(params.get(8)));
        stmt.setInt(9, Integer.parseInt(params.get(0)));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    public boolean eliminar(String id) throws SQLException, IOException {
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "UPDATE sesion SET estado_sesion='cancelada' WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(id));
        
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0;
    }
}
