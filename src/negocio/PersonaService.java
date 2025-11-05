package negocio;

import datos.Persona;
import datos.PersonaDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Lógica de negocio para Persona
 * Valida datos antes de delegar al DAO
 */
public class PersonaService {
    private PersonaDAO dao;
    
    public PersonaService() {
        this.dao = new PersonaDAO();
    }
    
    /**
     * Lista personas según patrón
     */
    public List<Persona> listar(String patron) throws Exception {
        if (patron == null || patron.trim().isEmpty()) {
            patron = "*";
        }
        return dao.listar(patron);
    }
    
    /**
     * Inserta una nueva persona
     * Valida que nombre y apellido no estén vacíos
     */
    public boolean insertar(String nombre, String apellido) throws Exception {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        nombre = nombre.trim();
        apellido = apellido.trim();
        
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        if (apellido.length() > 100) {
            throw new IllegalArgumentException("El apellido no puede exceder 100 caracteres");
        }
        
        return dao.insertar(nombre, apellido);
    }
    
    /**
     * Modifica una persona existente
     */
    public boolean modificar(int id, String nombre, String apellido) throws Exception {
        // Validaciones
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        nombre = nombre.trim();
        apellido = apellido.trim();
        
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        if (apellido.length() > 100) {
            throw new IllegalArgumentException("El apellido no puede exceder 100 caracteres");
        }
        
        return dao.modificar(id, nombre, apellido);
    }
    
    /**
     * Elimina una persona
     */
    public boolean eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        
        return dao.eliminar(id);
    }
    
    /**
     * Verifica si existe una persona
     */
    public boolean existe(int id) throws Exception {
        return dao.existe(id);
    }
}
