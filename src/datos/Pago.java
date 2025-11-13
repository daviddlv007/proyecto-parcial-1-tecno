package datos;

import java.sql.Timestamp;

/**
 * Entidad Pago
 */
public class Pago {
    private int id;
    private int alumnoId;
    private int inscripcionId;
    private Timestamp fecha;
    private double monto;
    private int metodoPagoId;
    private String comprobante;
    private String observaciones;
    
    // Constructor vacío
    public Pago() {}
    
    // Constructor completo
    public Pago(int id, int alumnoId, int inscripcionId, Timestamp fecha,
                double monto, int metodoPagoId, String comprobante, String observaciones) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.inscripcionId = inscripcionId;
        this.fecha = fecha;
        this.monto = monto;
        this.metodoPagoId = metodoPagoId;
        this.comprobante = comprobante;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    
    public int getInscripcionId() { return inscripcionId; }
    public void setInscripcionId(int inscripcionId) { this.inscripcionId = inscripcionId; }
    
    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
    
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    
    public int getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(int metodoPagoId) { this.metodoPagoId = metodoPagoId; }
    
    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
