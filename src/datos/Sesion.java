package datos;

public class Sesion {
    private int id;
    private int actividadId;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private int instructorId;
    private int vehiculoId;
    private String lugar;
    private String estadoSesion;
    private int capacidadMaxima;
    
    public Sesion() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getActividadId() { return actividadId; }
    public void setActividadId(int actividadId) { this.actividadId = actividadId; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }
    
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    
    public String getEstadoSesion() { return estadoSesion; }
    public void setEstadoSesion(String estadoSesion) { this.estadoSesion = estadoSesion; }
    
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
}
