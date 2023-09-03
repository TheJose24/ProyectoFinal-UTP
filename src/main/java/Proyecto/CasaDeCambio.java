package Proyecto;

import java.util.*;

public class CasaDeCambio {
    // Atributos
    private List<Moneda> monedas;  // Lista de monedas disponibles en la casa de cambio
    public Map<String, Double> tiposDeCambio;  // Mapa que almacena los tipos de cambio de las monedas
    private List<BoletaVenta> boletasVentas;  // Lista de boletas de venta realizadas en la casa de cambio
    private List<Usuario> usuarios;  // Lista de usuarios registrados en la casa de cambio

    //Constructor
    public CasaDeCambio() {
        monedas = new ArrayList<>();
        tiposDeCambio = new HashMap<>();
        boletasVentas = new ArrayList<>();
        usuarios = new ArrayList<>();
    }
    
    // Método para agregar un usuario a la lista de usuarios
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    // Método para buscar un usuario por su nombre de usuario
    public Usuario buscarUsuario(String nombreUsuario) {
        return usuarios.stream()
                .filter(usuario -> usuario.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst()
                .orElse(null);
    }

    // Método para agregar una moneda a la lista de monedas disponibles
    public void agregarMoneda(Moneda moneda) {
        monedas.add(moneda);
    }

    // Método para actualizar el tipo de cambio de una moneda
    public void actualizarTipoDeCambio(String moneda, double tipoCambio) {
        tiposDeCambio.put(moneda, tipoCambio);
    }

    // Método para obtener el precio de una moneda en moneda local
    public double obtenerPrecioMonedaLocal(double monto, String monedaExtranjera, String monedaLocal) {
        double tipoCambioExtranjera = tiposDeCambio.getOrDefault(monedaExtranjera, 1.0);
        double tipoCambioLocal = tiposDeCambio.getOrDefault(monedaLocal, 1.0);

        double montoEnDolares = monto / tipoCambioExtranjera;
        double montoEnMonedaLocal = montoEnDolares * tipoCambioLocal;

        return montoEnMonedaLocal;
    }

    // Método para crear una boleta de venta
    public BoletaVenta crearBoletaVenta(double monto, String monedaOrigen, String monedaDestino, String nombreCliente, String dniCliente, BoletaVenta.MetodoPago metodoPago, CasaDeCambio casaDeCambio) {
        double montoEnMonedaDestino = obtenerPrecioMonedaLocal(monto, monedaOrigen, monedaDestino);
        BoletaVenta boletaVenta = new BoletaVenta(monto, monedaOrigen, monedaDestino, new Date(), montoEnMonedaDestino, nombreCliente, dniCliente, metodoPago, casaDeCambio);
        boletasVentas.add(boletaVenta);
        return boletaVenta;
    }

    // Método para obtener la lista con nombres de monedas y sus tipos de cambio
    public List<String> obtenerMonedasYTiposDeCambio() {
        List<String> listaMonedasYTiposDeCambio = new ArrayList<>();
        for (Moneda moneda : monedas) {
            String nombreMoneda = moneda.getNombre();
            double tipoCambio = tiposDeCambio.getOrDefault(nombreMoneda, 1.0);
            listaMonedasYTiposDeCambio.add(nombreMoneda + " - Tipo de Cambio: " + tipoCambio);
        }
        return listaMonedasYTiposDeCambio;
    }

    // Método para validar si una moneda está registrada en la casa de cambio
    public boolean validarMonedaRegistrada(String nombreMoneda) {
        return monedas.stream().anyMatch(moneda -> moneda.getNombre().equalsIgnoreCase(nombreMoneda));
    }
}
