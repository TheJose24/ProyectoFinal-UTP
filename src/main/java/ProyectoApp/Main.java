package ProyectoApp;

import Proyecto.BoletaVenta;
import Proyecto.BoletaVenta.MetodoPago;
import Proyecto.CasaDeCambio;
import Proyecto.Moneda;
import Proyecto.Usuario;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JOptionPane;

public class Main {

    private static CasaDeCambio casaDeCambio;
    private static Usuario usuarioActual;

    public static void main(String[] args) {
        casaDeCambio = new CasaDeCambio();
        // Agrega usuarios al sistema
        agregarUsuariosPredeterminados();
        // Realiza el proceso de inicio de sesión
        realizarLogin();
    }

    // Método para agregar usuarios predeterminados manualmente a la casa de cambio
    private static void agregarUsuariosPredeterminados() {
        // Usuarios predeterminados (nombreUsuario, contrasena)
        Usuario usuario1 = new Usuario("usuario1", "contrasena1");
        Usuario usuario2 = new Usuario("usuario2", "contrasena2");
        // Agrega los usuarios a una lista en la clase Main
        casaDeCambio.agregarUsuario(usuario1);
        casaDeCambio.agregarUsuario(usuario2);
    }

    // Método para realizar el inicio de sesión
    private static void realizarLogin() {
        boolean loginExitoso = false;
        int intentos = 3; //Intentos permitidos

        while (!loginExitoso && intentos > 0) {
            String nombreUsuario = JOptionPane.showInputDialog(null, "Ingrese su nombre de usuario:", "Login", JOptionPane.PLAIN_MESSAGE);
            String contrasena = JOptionPane.showInputDialog(null, "Ingrese su contraseña:", "Login", JOptionPane.PLAIN_MESSAGE);

            // Verifica si las credenciales son válidas utilizando el método validarCredenciales de la clase Usuario
            Usuario usuario = casaDeCambio.buscarUsuario(nombreUsuario);
            if (usuario != null && usuario.validarCredenciales(nombreUsuario, contrasena)) {
                loginExitoso = true;
                usuarioActual = usuario; // Guarda el usuario actual para futuras operaciones
                JOptionPane.showMessageDialog(null, "¡Login exitoso!", "Login", JOptionPane.INFORMATION_MESSAGE);
                mostrarMenuPrincipal();
            } else {
                intentos--;
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Intentos restantes: " + intentos, "Login", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Ejecucion de medidas sino se logra un login exitoso
        if (!loginExitoso) {
            JOptionPane.showMessageDialog(null, "Demasiados intentos fallidos. Cerrando la aplicación.", "Login", JOptionPane.WARNING_MESSAGE);
            System.exit(0); // Cerrar la aplicación
        }
    }

    // Método para mostrar el menú principal y manejar las opciones del usuario
    public static void mostrarMenuPrincipal() {

        // Monedas predeterminadas con sus valores de tipo de cambio
        casaDeCambio.agregarMoneda(new Moneda("Dolar"));
        casaDeCambio.actualizarTipoDeCambio("Dolar", 0.2788);

        casaDeCambio.agregarMoneda(new Moneda("Euro"));
        casaDeCambio.actualizarTipoDeCambio("Euro", 0.2509);

        casaDeCambio.agregarMoneda(new Moneda("Libra Esterlina"));
        casaDeCambio.actualizarTipoDeCambio("Libra Esterlina", 0.2170);

        casaDeCambio.agregarMoneda(new Moneda("Sol"));
        casaDeCambio.actualizarTipoDeCambio("Sol", 1);

        casaDeCambio.agregarMoneda(new Moneda("Yen"));
        casaDeCambio.actualizarTipoDeCambio("Yen", 39.487);

        // Número de tarjeta y código de seguridad predeterminados
        final String TARJETA_PRED = "123456789";
        final String CODIGO_SEGURIDAD_PRED = "123";

        // Menú principal
        int opcion;
        do {
            String menu = " ------------ Casa de Cambio ------------\n"
                    + "1. Agregar moneda\n"
                    + "2. Actualizar tipo de cambio\n"
                    + "3. Mostrar lista de monedas registradas\n"
                    + "4. Realizar cambio de moneda\n"
                    + "5. Imprimir boleta de venta\n"
                    + "6. Ver historial de ventas\n"
                    + "7. Salir\n"
                    + "Ingrese una opción: ";

            opcion = Integer.parseInt(JOptionPane.showInputDialog(null, menu));

            BoletaVenta boletaVenta;
            MetodoPago metodoPago;

            switch (opcion) {
                // Agregar Moneda
                case 1:
                    String nuevaMoneda = JOptionPane.showInputDialog("Ingrese el nombre de la moneda: ");
                    double nuevoTipoCambio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el tipo de cambio respecto al dólar: "));
                    Moneda monedaNueva = new Moneda(nuevaMoneda); // Crear el objeto Moneda con el nombre ingresado
                    casaDeCambio.agregarMoneda(monedaNueva); // Pasar el objeto Moneda al método agregarMoneda
                    casaDeCambio.actualizarTipoDeCambio(nuevaMoneda, nuevoTipoCambio);
                    JOptionPane.showMessageDialog(null, "Moneda y tipo de cambio agregados con éxito.");
                break;

                // Actualizar tipo de cambio
                case 2:
                    String nombreMonedaActualizar;
                    double tipoCambio;

                    // Pedir el nombre de la moneda y validar si está registrada
                    boolean monedaRegistrada;
                    do {
                        nombreMonedaActualizar = JOptionPane.showInputDialog("Ingrese el nombre de la moneda: ");
                        monedaRegistrada = casaDeCambio.validarMonedaRegistrada(nombreMonedaActualizar);

                        // Mostrar el mensaje de error si la moneda no está registrada
                        if (!monedaRegistrada) {
                            JOptionPane.showMessageDialog(null, "Moneda no registrada. Ingrese una moneda válida.", "Moneda no registrada", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (!monedaRegistrada);

                    // Actualiza el tipo de cambio de la moneda validada
                    tipoCambio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo tipo de cambio: "));
                    casaDeCambio.actualizarTipoDeCambio(nombreMonedaActualizar, tipoCambio);
                    JOptionPane.showMessageDialog(null, "Tipo de cambio actualizado con éxito.");
                break;

                //Mostrar lista de monedas registradas
                case 3:
                    List<String> listaMonedasYTiposDeCambio = casaDeCambio.obtenerMonedasYTiposDeCambio();
                    StringBuilder mensajeMonedas = new StringBuilder("--- Monedas y Tipos de Cambio Registrados ---\n");
                    for (String monedaYTipoCambio : listaMonedasYTiposDeCambio) {
                        mensajeMonedas.append(monedaYTipoCambio).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, mensajeMonedas.toString());
                break;

                // Realizar cambio de moneda
                case 4:
                    double montoCambio = 0;
                    String monedaOrigen = "";
                    String monedaDestino = "";

                    // Validar el monto a cambiar (debe ser un número)
                    while (true) {
                        String montoInput = JOptionPane.showInputDialog("Ingrese el monto a cambiar: ");
                        try {
                            montoCambio = Double.parseDouble(montoInput);
                            break; // Salir del bucle si el monto es un número válido
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Por favor, ingrese un valor numérico", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // Validar la moneda de origen (debe estar registrada)
                    while (true) {
                        monedaOrigen = JOptionPane.showInputDialog("Ingrese la moneda de origen: ");
                        if (casaDeCambio.validarMonedaRegistrada(monedaOrigen)) {
                            break; // Salir del bucle si la moneda está registrada
                        } else {
                            JOptionPane.showMessageDialog(null, "Por favor ingrese una moneda registrada", "Moneda no registrada", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // Validar la moneda de destino (debe estar registrada)
                    while (true) {
                        monedaDestino = JOptionPane.showInputDialog("Ingrese la moneda de destino: ");
                        if (casaDeCambio.validarMonedaRegistrada(monedaDestino)) {
                            break; // Salir del bucle si la moneda está registrada
                        } else {
                            JOptionPane.showMessageDialog(null, "Por favor ingrese una moneda registrada", "Moneda no registrada", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // Obtener el monto en la moneda local de referencia (Dólar en este caso)
                    double montoEnDolares = casaDeCambio.obtenerPrecioMonedaLocal(montoCambio, monedaOrigen, "Dólar");
                    double montoEnMonedaDestino = casaDeCambio.obtenerPrecioMonedaLocal(montoEnDolares, "Dólar", monedaDestino);

                    // Mostrar el monto cambiado en la moneda destino
                    JOptionPane.showMessageDialog(null, "Monto en " + monedaDestino + ": " + montoEnMonedaDestino);

                    // Preguntar por confirmación de compra
                    String confirmacion = JOptionPane.showInputDialog("¿Confirmar compra? (S/N)");
                    if (confirmacion != null && confirmacion.equalsIgnoreCase("S")) {
                        // Preguntar por el método de pago
                        String metodoPagoInput = JOptionPane.showInputDialog(
                                "Seleccione el método de pago:\n"
                                + "(1) Pago en efectivo\n"
                                + "(2) Pago con tarjeta");
                        if (metodoPagoInput != null && !metodoPagoInput.isEmpty()) {
                            int metodoPagoSeleccionado = Integer.parseInt(metodoPagoInput);
                            if (metodoPagoSeleccionado == 1) {
                                metodoPago = MetodoPago.EFECTIVO;
                            } else if (metodoPagoSeleccionado == 2) {
                                metodoPago = MetodoPago.TARJETA;

                                // Pago con tarjeta: Pedir los datos de la tarjeta
                                String numeroTarjeta;
                                String codigoSeguridad;

                                boolean datosCorrectos = false;
                                while (!datosCorrectos) {
                                    numeroTarjeta = JOptionPane.showInputDialog("Ingrese el número de la tarjeta: ");
                                    if (!numeroTarjeta.matches("\\d+")) {
                                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un número de tarjeta válido.", "Error", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        codigoSeguridad = JOptionPane.showInputDialog("Ingrese el código de seguridad de 3 dígitos: ");
                                        if (!codigoSeguridad.matches("\\d+")) {
                                            JOptionPane.showMessageDialog(null, "Por favor, ingrese un código de seguridad válido.", "Error", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            // Validar que los datos de tarjeta ingresados coincidan con los predeterminados
                                            if (numeroTarjeta.equals(TARJETA_PRED) && codigoSeguridad.equals(CODIGO_SEGURIDAD_PRED)) {
                                                datosCorrectos = true;
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Número de tarjeta o código de seguridad incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    }
                                }

                                // Continuar con la ejecución del código normal
                            } else {
                                JOptionPane.showMessageDialog(null, "Opción de método de pago inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Opción de método de pago inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Compra cancelada.", "Aviso", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    // Pedir datos para la boleta
                    String nombreCliente = JOptionPane.showInputDialog("Ingrese el nombre del cliente: ");
                    String dniCliente = JOptionPane.showInputDialog("Ingrese el DNI del cliente: ");

                    // Crear la boleta de venta
                    boletaVenta = casaDeCambio.crearBoletaVenta(montoCambio, monedaOrigen, monedaDestino, nombreCliente, dniCliente, metodoPago, casaDeCambio);
                    boletaVenta.crearBoleta("C:\\Users\\josee\\Documents\\boleta.txt");
                    // Registrar la venta en el historial usando la instancia de BoletaVenta
                    boletaVenta.registrarVenta(LocalDateTime.now(), metodoPago, monedaOrigen, monedaDestino);
                    JOptionPane.showMessageDialog(null, "Compra registrada correctamente, para ver la boleta digite '5' en el menú principal.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                break;

                // Imprimir boleta de venta
                case 5:
                    try {
                    BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\josee\\Documents\\boleta.txt"));
                    StringBuilder boletaTexto = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        boletaTexto.append(line).append("\n");
                    }
                    reader.close();
                    JOptionPane.showMessageDialog(null, boletaTexto.toString(), "Boleta de Venta", JOptionPane.PLAIN_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al mostrar la boleta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

                // Ver historial de ventas
                case 6:
                    List<BoletaVenta> historialBoletas = BoletaVenta.obtenerHistorialBoletas();
                    if (historialBoletas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay ventas registradas en el historial.");
                    } else {
                        StringBuilder historialTexto = new StringBuilder("----------------- Historial de Ventas -----------------\n");
                        for (BoletaVenta boleta : historialBoletas) {
                            historialTexto.append("Archivo: ").append(boleta.getNombreArchivo()).append("\n");
                            historialTexto.append("Monto en moneda local (Soles): ").append(boleta.getMontoEnMonedaLocal()).append("\n");
                            historialTexto.append("-------------------------------------------\n");
                        }
                        JOptionPane.showMessageDialog(null, historialTexto.toString());
                    }
                break;

                // Salir
                case 7:
                    JOptionPane.showMessageDialog(null, "¡Hasta luego!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
            }

        } while (opcion != 7);
    }
}
