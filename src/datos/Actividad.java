package datos;

public class Actividad {
    private int id;
    private String nombre;
    private String descripcion;
    private int tipoActividadId;
    private double duracionHoras;
    private String estadoActividad;
    private String nivel;
    
    public Actividad() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getTipoActividadId() { return tipoActividadId; }
    public void setTipoActividadId(int tipoActividadId) { this.tipoActividadId = tipoActividadId; }
    
    public double getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(double duracionHoras) { this.duracionHoras = duracionHoras; }
    
    public String getEstadoActividad() { return estadoActividad; }
    public void setEstadoActividad(String estadoActividad) { this.estadoActividad = estadoActividad; }
    
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}
