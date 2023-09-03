package Proyecto;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Proyecto.BoletaVenta.MetodoPago;
import java.io.BufferedWriter;
import javax.swing.JOptionPane;

public class BoletaVenta {
    //Atributos
    private double monto;
    private String monedaOrigen;
    private String monedaDestino;
    private Date fecha;
    private double montoEnMonedaDestino;
    private double tasaCambio;
    private String nombre;
    private String dni;
    public MetodoPago metodoPago;
    private List<RegistroVenta> historialVentas;
    private String nombreArchivo;
    private CasaDeCambio casaDeCambio;

    //Lista para almacenar el historial de boletas
    private static List<BoletaVenta> historialBoletas = new ArrayList<>();
    
    //Constructor parametrizado
    public BoletaVenta(double monto, String monedaOrigen, String monedaDestino, Date fecha, double montoEnMonedaDestino, String nombre, String dni, MetodoPago metodoPago, CasaDeCambio casaDeCambio) {
        this.monto = monto;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.fecha = fecha;
        this.montoEnMonedaDestino = montoEnMonedaDestino;
        this.nombre = nombre;
        this.dni = dni;
        this.metodoPago = metodoPago;
        historialVentas = new ArrayList<>();
        historialBoletas.add(this);
        
        //Genera un nombre de archivo unico para la boleta basado en la fecha y hora
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        nombreArchivo = "boleta_" + timeStamp;
        this.nombreArchivo = generarNombreArchivo();
        this.casaDeCambio = casaDeCambio;

        // Guardar los datos de la boleta en el archivo
        guardarBoletaEnArchivo();
    }
    
    //Metodo para imprimir la boleta de venta en la pantalla
    public void imprimir() {
        System.out.println("Boleta de Venta");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Fecha: " + dateFormat.format(fecha));
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Monto: " + monto + " " + monedaOrigen);
        System.out.println("Monto convertido: " + monto * tasaCambio + " " + monedaDestino);
    }

    // Método para crear una boleta y guardarla en un archivo .txt
    public void crearBoleta(String ruta) {
        try {
            FileWriter writer = new FileWriter(ruta);
            StringBuilder boletaTexto = new StringBuilder();
            //Contenido de la boleta
            boletaTexto.append("===========================================\n");
            boletaTexto.append("             BOLETA DE VENTA\n");
            boletaTexto.append("===========================================\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            boletaTexto.append("Fecha: ").append(dateFormat.format(fecha)).append("\n");
            boletaTexto.append("Nombre: ").append(nombre).append("\n");
            boletaTexto.append("DNI: ").append(dni).append("\n");
            boletaTexto.append("Monto: ").append(monto).append(" ").append(monedaOrigen).append("\n");
            boletaTexto.append("Monto convertido: ").append(montoEnMonedaDestino).append(" ").append(monedaDestino).append("\n");
            boletaTexto.append("===========================================\n");
            boletaTexto.append("         ¡GRACIAS POR SU COMPRA!\n");
            boletaTexto.append("===========================================\n");
            //Escribir el contenido en el archivo .txt
            writer.write(boletaTexto.toString());
            writer.close();
            System.out.println("La boleta se ha creado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir la boleta: " + e.getMessage());
        }
    }

    // Método para registrar la venta en el historial
    public void registrarVenta(LocalDateTime fecha, MetodoPago metodoPago, String monedaOrigen, String monedaDestino) {
        RegistroVenta registroVenta = new RegistroVenta(fecha, metodoPago, monedaOrigen, monedaDestino);
        historialVentas.add(registroVenta);
    }

    // Método para obtener el historial de ventas
    public static List<BoletaVenta> obtenerHistorialBoletas() {
        return historialBoletas;
    }
    
    // Enumeración para los métodos de pago posibles
    public enum MetodoPago {
        EFECTIVO,
        TARJETA
    }

    // Método para guardar los datos de la boleta en el archivo
    private void guardarBoletaEnArchivo() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
            // Escribir los datos de la boleta en el archivo
            writer.write(toString());
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la boleta en el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método toString() para obtener una representación de la boleta en forma de texto
    @Override
    public String toString() {
        StringBuilder boletaTexto = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //Contenido de la boleta
        boletaTexto.append("Fecha: ").append(dateFormat.format(fecha)).append("\n");
        boletaTexto.append("Método de Pago: ").append(metodoPago).append("\n");
        boletaTexto.append("Moneda Origen: ").append(monedaOrigen).append("\n");
        boletaTexto.append("Moneda Destino: ").append(monedaDestino).append("\n");
        
        return boletaTexto.toString();
    }

    // Método para generar el nombre único del archivo basado en la fecha y otros atributos
    private String generarNombreArchivo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        String fechaHora = dateFormat.format(fecha);

        return "boleta_" + fechaHora + "_" + monto + "_" + monedaOrigen + "_a_" + monedaDestino;

    }

    // Método para obtener el monto en la moneda local utilizando el tipo de cambio de la Casa de Cambio
    public double getMontoEnMonedaLocal() {
        double tipoCambioMonedaOrigen = casaDeCambio.tiposDeCambio.getOrDefault(monedaOrigen, 1.0);
        return monto / tipoCambioMonedaOrigen;
    }

    // Método para obtener el nombre del archivo generado para la boleta
    public String getNombreArchivo() {
        return nombreArchivo;
    }

}
