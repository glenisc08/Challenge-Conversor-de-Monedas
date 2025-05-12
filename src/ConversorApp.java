package src;

import src.Servicio.ConversorService;
import src.UI.ConsoleUI;

public class ConversorApp {
    public static void main(String[] args) {
        System.out.println("Iniciando Conversor de Monedas...");
        
        try {
            // Inicializar servicios
            ConversorService conversorService = new ConversorService();
            
            // Inicializar interfaz de usuario
            ConsoleUI consoleUI = new ConsoleUI(conversorService);
            
            // Iniciar la aplicación
            consoleUI.iniciar();
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
