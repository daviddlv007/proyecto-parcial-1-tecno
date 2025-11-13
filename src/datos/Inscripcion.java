package datos;

import java.sql.Timestamp;

/**
 * Entidad Inscripcion
 */
public class Inscripcion {
    private int id;
    private int alumnoId;
    private int sesionId;
    private Timestamp fechaInscripcion;
    private String estadoInscripcion;
    private int tipoPagoId;
    private double montoTotal;
    private Integer cursoId;
    private String observaciones;
    
    // Constructor vacío
    public Inscripcion() {}
    
    // Constructor completo
    public Inscripcion(int id, int alumnoId, int sesionId, Timestamp fechaInscripcion,
                      String estadoInscripcion, int tipoPagoId, double montoTotal,
                      Integer cursoId, String observaciones) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.sesionId = sesionId;
        this.fechaInscripcion = fechaInscripcion;
        this.estadoInscripcion = estadoInscripcion;
        this.tipoPagoId = tipoPagoId;
        this.montoTotal = montoTotal;
        this.cursoId = cursoId;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    
    public int getSesionId() { return sesionId; }
    public void setSesionId(int sesionId) { this.sesionId = sesionId; }
    
    public Timestamp getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(Timestamp fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    
    public String getEstadoInscripcion() { return estadoInscripcion; }
    public void setEstadoInscripcion(String estadoInscripcion) { this.estadoInscripcion = estadoInscripcion; }
    
    public int getTipoPagoId() { return tipoPagoId; }
    public void setTipoPagoId(int tipoPagoId) { this.tipoPagoId = tipoPagoId; }
    
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
