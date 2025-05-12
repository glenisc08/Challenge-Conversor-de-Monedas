package src.UI;

import src.Servicio.ConversorService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Interfaz de usuario por consola para interactuar con el conversor
 */
public class ConsoleUI {
    private final ConversorService conversorService;
    private final Scanner scanner;
    private final DecimalFormat df;
    
    public ConsoleUI(ConversorService conversorService) {
        this.conversorService = conversorService;
        this.scanner = new Scanner(System.in);
        this.df = new DecimalFormat("#,##0.00");
    }
    
    /**
     * Inicia la interfaz de usuario
     */
    public void iniciar() {
        boolean salir = false;
        
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    realizarConversion();
                    break;
                case 2:
                    mostrarHistorial();
                    break;
                case 3:
                    mostrarMonedasSoportadas();
                    break;
                case 0:
                    salir = true;
                    System.out.println("\n¡Gracias por usar el Conversor de Monedas!");
                    break;
                default:
                    System.out.println("\nOpción no válida. Intente nuevamente.");
                    break;
            }
        }
        
        scanner.close();
    }
    
    /**
     * Muestra el menú principal
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n===================================");
        System.out.println("    CONVERSOR DE MONEDAS - ALURA    ");
        System.out.println("===================================");
        System.out.println("1. Realizar conversión");
        System.out.println("2. Ver historial de conversiones");
        System.out.println("3. Ver monedas soportadas");
        System.out.println("0. Salir");
        System.out.print("\nElija una opción: ");
    }
    
    /**
     * Lee una opción del usuario
     */
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Realiza el proceso de conversión
     */
    private void realizarConversion() {
        try {
            List<String> monedasSoportadas = conversorService.getMonedasSoportadas();
            
            // Mostrar monedas disponibles
            System.out.println("\n=== REALIZAR CONVERSIÓN ===");
            System.out.println("Monedas disponibles:");
            
            for (int i = 0; i < monedasSoportadas.size(); i++) {
                System.out.println((i + 1) + ". " + monedasSoportadas.get(i));
            }
            
            // Seleccionar moneda origen
            System.out.print("\nSeleccione la moneda de origen (1-" + monedasSoportadas.size() + "): ");
            int indiceOrigen = leerOpcion() - 1;
            
            if (indiceOrigen < 0 || indiceOrigen >= monedasSoportadas.size()) {
                System.out.println("Moneda de origen no válida.");
                return;
            }
            
            String monedaOrigen = monedasSoportadas.get(indiceOrigen);
            
            // Seleccionar moneda destino
            System.out.print("Seleccione la moneda de destino (1-" + monedasSoportadas.size() + "): ");
            int indiceDestino = leerOpcion() - 1;
            
            if (indiceDestino < 0 || indiceDestino >= monedasSoportadas.size()) {
                System.out.println("Moneda de destino no válida.");
                return;
            }
            
            String monedaDestino = monedasSoportadas.get(indiceDestino);
            
            // Ingresar cantidad a convertir
            System.out.print("Ingrese la cantidad a convertir de " + monedaOrigen + " a " + monedaDestino + ": ");
            double cantidad;
            
            try {
                cantidad = Double.parseDouble(scanner.nextLine().replace(",", "."));
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor que cero.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad no válida.");
                return;
            }
            
            // Realizar conversión
            double resultado = conversorService.convertir(monedaOrigen, monedaDestino, cantidad);
            
            // Mostrar resultado
            System.out.println("\n=== RESULTADO ===");
            System.out.println(df.format(cantidad) + " " + monedaOrigen + " = " + df.format(resultado) + " " + monedaDestino);
            
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }
    }
    
    /**
     * Muestra el historial de conversiones
     */
    private void mostrarHistorial() {
        List<Map<String, Object>> historial = conversorService.getHistorialConversiones();
        
        System.out.println("\n=== HISTORIAL DE CONVERSIONES ===");
        
        if (historial.isEmpty()) {
            System.out.println("Aún no hay conversiones registradas.");
            return;
        }
        
        for (int i = historial.size() - 1; i >= 0; i--) {
            Map<String, Object> conversion = historial.get(i);
            System.out.println("Fecha: " + conversion.get("fecha"));
            System.out.println("Conversión: " + conversion.get("cantidadOrigen") + " " + 
                    conversion.get("monedaOrigen") + " → " + 
                    conversion.get("cantidadDestino") + " " + 
                    conversion.get("monedaDestino"));
            System.out.println("-----------------------------------");
        }
    }
    
    /**
     * Muestra las monedas soportadas
     */
    private void mostrarMonedasSoportadas() {
        List<String> monedas = conversorService.getMonedasSoportadas();
        
        System.out.println("\n=== MONEDAS SOPORTADAS ===");
        for (String moneda : monedas) {
            String nombreMoneda = obtenerNombreMoneda(moneda);
            System.out.println("- " + moneda + ": " + nombreMoneda);
        }
    }
    
    /**
     * Obtiene el nombre completo de una moneda a partir de su código
     */
    private String obtenerNombreMoneda(String codigo) {
        switch (codigo) {
            case "USD": return "Dólar estadounidense";
            case "ARS": return "Peso argentino";
            case "BOB": return "Boliviano";
            case "BRL": return "Real brasileño";
            case "CLP": return "Peso chileno";
            case "COP": return "Peso colombiano";
            default: return codigo;
        }
    }
}
