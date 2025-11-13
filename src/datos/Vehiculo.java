package datos;

public class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private int tipoVehiculoId;
    private String estadoVehiculo;
    private int capacidad;
    
    public Vehiculo() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    
    public int getTipoVehiculoId() { return tipoVehiculoId; }
    public void setTipoVehiculoId(int tipoVehiculoId) { this.tipoVehiculoId = tipoVehiculoId; }
    
    public String getEstadoVehiculo() { return estadoVehiculo; }
    public void setEstadoVehiculo(String estadoVehiculo) { this.estadoVehiculo = estadoVehiculo; }
    
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}
