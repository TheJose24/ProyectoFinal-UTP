package Proyecto;

import Proyecto.BoletaVenta.MetodoPago;
import java.time.LocalDateTime;

public class RegistroVenta {
    //Atributos
    private LocalDateTime fecha;
    private MetodoPago metodoPago;
    private String monedaOrigen;
    private String monedaDestino;

    // Constructor de la clase RegistroVenta
    public RegistroVenta(LocalDateTime fecha, MetodoPago metodoPago, String monedaOrigen, String monedaDestino) {
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
    }

    // Getters y Setters
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    } 
    
}
