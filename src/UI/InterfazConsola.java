package src.UI;

import src.Servicio.ServicioConversor;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class InterfazConsola {

    private final ServicioConversor servicioConversor;
    private final Scanner scanner;

    public InterfazConsola(ServicioConversor servicioConversor) {
        this.servicioConversor = servicioConversor;
        this.scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = obtenerOpcion();
            salir = procesarOpcion(opcion);
        }
        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("=== Conversor de Monedas ===");
        System.out.println("1. Realizar Conversión");
        System.out.println("2. Ver Historial de Conversiones");
        System.out.println("3. Ver Monedas Soportadas");
        System.out.println("0. Salir");
        System.out.println("Seleccione una opción:");
    }

    private int obtenerOpcion() {
        int opcion = -1;
        while (opcion < 0 || opcion > 3) {
            try {
                opcion = scanner.nextInt();
                if (opcion < 0 || opcion > 3) {
                    System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Ingrese un número.");
                scanner.next(); 
            }
            scanner.nextLine();
        }
        return opcion;
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                realizarConversion();
                return false;
            case 2:
                mostrarHistorialConversiones();
                return false;
            case 3:
                mostrarMonedasSoportadas();
                return false;
            case 0:
                System.out.println("Saliendo...");
                return true;
            default:
                System.out.println("Opción inválida."); 
                return false;
        }
    }

    private void realizarConversion() {
        try {
            mostrarMonedasDisponibles();
            String monedaOrigen = obtenerMonedaSeleccionada("origen");
            String monedaDestino = obtenerMonedaSeleccionada("destino");
            double cantidad = obtenerCantidad();

            double resultado = servicioConversor.convertirMoneda(monedaOrigen, monedaDestino, cantidad);
            mostrarResultado(monedaOrigen, monedaDestino, cantidad, resultado);

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarMonedasDisponibles() {
        System.out.println("Monedas disponibles:");
        List<String> monedas = servicioConversor.getMonedasSoportadas();
        for (int i = 0; i < monedas.size(); i++) {
            System.out.println((i + 1) + ". " + monedas.get(i) + " (" + servicioConversor.obtenerNombreMoneda(monedas.get(i)) + ")");
        }
    }

    private String obtenerMonedaSeleccionada(String tipo) {
        int seleccion;
        List<String> monedas = servicioConversor.getMonedasSoportadas();
        while (true) {
            System.out.println("Seleccione la moneda de " + tipo + " (1-" + monedas.size() + "):");
            try {
                seleccion = scanner.nextInt();
                if (seleccion >= 1 && seleccion <= monedas.size()) {
                    return monedas.get(seleccion - 1);
                }
                System.out.println("Selección no válida.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Ingrese un número.");
                scanner.next(); 
            }
            scanner.nextLine(); 
        }
    }

    private double obtenerCantidad() {
        double cantidad;
        while (true) {
            try {
                System.out.println("Ingrese la cantidad a convertir:");
                cantidad = scanner.nextDouble();
                if (cantidad > 0) {
                    break;
                }
                System.out.println("Cantidad debe ser mayor que cero.");
            } catch (InputMismatchException e) {
                System.out.println("Cantidad no válida. Ingrese un número.");
                scanner.next();
            }
            scanner.nextLine(); 
        }
        return cantidad;
    }

    private void mostrarResultado(String monedaOrigen, String monedaDestino, double cantidad, double resultado) {
        System.out.println("Resultado:");
        System.out.println(cantidad + " " + monedaOrigen + " = " + resultado + " " + monedaDestino);
    }

    private void mostrarHistorialConversiones() {
        List<Map<String, Object>> historial = servicioConversor.getHistorialConversiones();
        System.out.println("\n=== Historial de Conversiones ===");
        if (historial.isEmpty()) {
            System.out.println("No hay conversiones en el historial.");
        } else {
            for (Map<String, Object> conversion : historial) {
                System.out.println("Fecha: " + conversion.get("fecha"));
                System.out.println(conversion.get("cantidadOrigen") + " " + conversion.get("monedaOrigen") +
                        " -> " + conversion.get("cantidadDestino") + " " + conversion.get("monedaDestino"));
            }
        }
    }

    private void mostrarMonedasSoportadas() {
        System.out.println("\n=== Monedas Soportadas ===");
        servicioConversor.getMonedasSoportadas().forEach(moneda ->
                System.out.println("- " + moneda + " (" + servicioConversor.obtenerNombreMoneda(moneda) + ")"));
    }
}