package datos;

/**
 * Entidad Usuario mapeada a la tabla 'usuario'
 */
public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String genero;
    private String tipoDocumento;
    private String numeroDocumento;
    private String email;
    private String telefono;
    private String direccion;
    private String contrasena;
    private int rolId;
    private String estadoUsuario;
    
    // Constructor vacío
    public Usuario() {}
    
    // Constructor completo
    public Usuario(int id, String nombre, String apellido, String fechaNacimiento, 
                   String genero, String tipoDocumento, String numeroDocumento, 
                   String email, String telefono, String direccion, String contrasena,
                   int rolId, String estadoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.contrasena = contrasena;
        this.rolId = rolId;
        this.estadoUsuario = estadoUsuario;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public int getRolId() { return rolId; }
    public void setRolId(int rolId) { this.rolId = rolId; }
    
    public String getEstadoUsuario() { return estadoUsuario; }
    public void setEstadoUsuario(String estadoUsuario) { this.estadoUsuario = estadoUsuario; }
    
    @Override
    public String toString() {
        return "Usuario{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
