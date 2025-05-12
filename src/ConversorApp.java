package src;

import src.Servicio.ServicioConversor;
import src.UI.InterfazConsola;

public class ConversorApp {

    public static void main(String[] args) {
        ServicioConversor servicioConversor = new ServicioConversor();
        InterfazConsola interfazConsola = new InterfazConsola(servicioConversor);
        interfazConsola.ejecutar();
    }
}