package src.Servicio;

import src.Cliente.ClienteTasaDeCambio;
import src.Modelo.RespuestaTasaDeCambio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ServicioConversor {

    private final ClienteTasaDeCambio cliente;
    private final List<String> monedasSoportadas;
    private final Map<String, String> nombresMonedas;
    private final List<Map<String, Object>> historialConversiones;
    private static final int MAX_HISTORIAL = 10;

    public ServicioConversor() {
        this.cliente = new ClienteTasaDeCambio();
        this.monedasSoportadas = new ArrayList<>();
        this.nombresMonedas = new HashMap<>();
        this.historialConversiones = new ArrayList<>();
        cargarMonedasSoportadas();
    }

    private void cargarMonedasSoportadas() {
        monedasSoportadas.add("USD");
        nombresMonedas.put("USD", "Dólar estadounidense");
        monedasSoportadas.add("EUR");
        nombresMonedas.put("EUR", "Euro");
        monedasSoportadas.add("GBP");
        nombresMonedas.put("GBP", "Libra Esterlina");
        monedasSoportadas.add("JPY");
        nombresMonedas.put("JPY", "Yen Japonés");
        monedasSoportadas.add("CAD");
        nombresMonedas.put("CAD", "Dólar Canadiense");
        monedasSoportadas.add("ARS");
        nombresMonedas.put("ARS", "Peso Argentino");
        monedasSoportadas.add("BRL");
        nombresMonedas.put("BRL", "Real Brasileño");
        monedasSoportadas.add("MXN");
        nombresMonedas.put("MXN", "Peso Mexicano");
        monedasSoportadas.add("KRW");
        nombresMonedas.put("KRW", "Won surcoreano");
        monedasSoportadas.add("CNY");
        nombresMonedas.put("CNY", "Yuan chino");

    }

    /**
     *
     * @param monedaOrigen
     * @param monedaDestino
     * @param cantidad
     * @return 
     * @throws IOException
     * @throws InterruptedException
     */
    public double convertirMoneda(String monedaOrigen, String monedaDestino, double cantidad) throws IOException, InterruptedException {
        RespuestaTasaDeCambio tasas = cliente.obtenerTasasDeCambio(monedaOrigen);

        if (tasas == null || tasas.getConversion_rates() == null || !tasas.getConversion_rates().containsKey(monedaDestino)) {
            throw new IOException("No se pudieron obtener las tasas de cambio.");
        }

        double tasaConversion = tasas.getConversion_rates().get(monedaDestino);
        double resultado = cantidad * tasaConversion;

        registrarConversion(monedaOrigen, monedaDestino, cantidad, resultado);

        return resultado;
    }

    public List<String> getMonedasSoportadas() {
        return monedasSoportadas;
    }

    public String obtenerNombreMoneda(String codigoMoneda) {
        return nombresMonedas.getOrDefault(codigoMoneda, codigoMoneda);
    }

    private void registrarConversion(String monedaOrigen, String monedaDestino, double cantidadOrigen, double cantidadDestino) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        Map<String, Object> registro = new HashMap<>();
        registro.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        registro.put("monedaOrigen", monedaOrigen);
        registro.put("monedaDestino", monedaDestino);
        registro.put("cantidadOrigen", df.format(cantidadOrigen));
        registro.put("cantidadDestino", df.format(cantidadDestino));
        historialConversiones.add(registro);

        if (historialConversiones.size() > MAX_HISTORIAL) {
            historialConversiones.remove(0); // 
        }
    }

    public List<Map<String, Object>> getHistorialConversiones() {
        return new ArrayList<>(historialConversiones);
    }
}