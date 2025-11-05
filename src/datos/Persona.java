package datos;

/**
 * Clase POJO para Persona
 */
public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    
    public Persona() {
    }
    
    public Persona(int id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    @Override
    public String toString() {
        return String.format("Persona[id=%d, nombre=%s, apellido=%s]", id, nombre, apellido);
    }
}
