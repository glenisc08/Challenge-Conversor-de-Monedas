package src.Servicio;

import src.Modelo.ExchangeRateResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.Cliente.ExchangeRateClient;

/**
 * Servicio que proporciona funcionalidades de conversión de monedas
 */
public class ConversorService {
    private final ExchangeRateClient client;
    private final Gson gson;
    private Map<String, Map<String, Double>> tasasCache;
    private LocalDateTime ultimaActualizacion;
    private final List<String> monedasSoportadas;
    private final List<Map<String, Object>> historialConversiones;
    private static final long CACHE_DURATION_SECONDS = 3600; // 1 hora

    public ConversorService() {
        this.client = new ExchangeRateClient();
        this.gson = new Gson();
        this.tasasCache = new HashMap<>();
        this.monedasSoportadas = new ArrayList<>();
        this.historialConversiones = new ArrayList<>();
        
        // Inicializar monedas soportadas
        monedasSoportadas.add("USD"); // Dólar estadounidense
        monedasSoportadas.add("ARS"); // Peso argentino
        monedasSoportadas.add("BOB"); // Boliviano boliviano
        monedasSoportadas.add("BRL"); // Real brasileño
        monedasSoportadas.add("CLP"); // Peso chileno
        monedasSoportadas.add("COP"); // Peso colombiano
    }

    /**
     * Obtiene las tasas de cambio actualizadas para una moneda base
     * @param baseCurrency Código de la moneda base
     * @return Mapa con las tasas de cambio
     * @throws IOException Si hay un error de E/S
     * @throws InterruptedException Si la solicitud es interrumpida
     */
    public Map<String, Double> obtenerTasasDeCambio(String baseCurrency) throws IOException, InterruptedException {
        // Verificar si ya tenemos tasas cacheadas y si son válidas (menos de 1 hora)
        if (esCacheValido(baseCurrency)) {
            return tasasCache.get(baseCurrency);
        }

        // Obtener nuevas tasas
        String jsonResponse = client.getExchangeRates(baseCurrency);
        ExchangeRateResponse response = gson.fromJson(jsonResponse, ExchangeRateResponse.class);
        
        // Filtrar solo las monedas soportadas
        Map<String, Double> tasasFiltradas = new HashMap<>();
        for (String moneda : monedasSoportadas) {
            if (response.getConversion_rates().containsKey(moneda)) {
                tasasFiltradas.put(moneda, response.getConversion_rates().get(moneda));
            }
        }
        
        // Actualizar cache
        tasasCache.put(baseCurrency, tasasFiltradas);
        ultimaActualizacion = LocalDateTime.now();
        
        return tasasFiltradas;
    }

    /**
     * Realiza la conversión de una cantidad de una moneda a otra
     * @param monedaOrigen Código de la moneda origen
     * @param monedaDestino Código de la moneda destino
     * @param cantidad Cantidad a convertir
     * @return Cantidad convertida
     * @throws IOException Si hay un error de E/S
     * @throws InterruptedException Si la solicitud es interrumpida
     */
    public double convertir(String monedaOrigen, String monedaDestino, double cantidad) 
            throws IOException, InterruptedException {
        
        // Si la moneda origen y destino son iguales, devolver la misma cantidad
        if (monedaOrigen.equals(monedaDestino)) {
            return cantidad;
        }
        
        // Obtener tasas para la moneda origen
        Map<String, Double> tasas = obtenerTasasDeCambio(monedaOrigen);
        
        // Calcular conversión
        double tasaDestino = tasas.get(monedaDestino);
        double resultado = cantidad * tasaDestino;
        
        // Registrar la conversión en el historial
        registrarConversion(monedaOrigen, monedaDestino, cantidad, resultado);
        
        return resultado;
    }
    
    /**
     * Verifica si el cache para una moneda base es válido
     */
    private boolean esCacheValido(String baseCurrency) {
        if (ultimaActualizacion == null || !tasasCache.containsKey(baseCurrency)) {
            return false;
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        long segundosTranscurridos = java.time.Duration.between(ultimaActualizacion, ahora).getSeconds();
        
        return segundosTranscurridos < CACHE_DURATION_SECONDS;
    }
    
    /**
     * Registra una conversión en el historial
     */
    private void registrarConversion(String monedaOrigen, String monedaDestino, double cantidadOrigen, double cantidadDestino) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        Map<String, Object> registro = new HashMap<>();
        
        registro.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        registro.put("monedaOrigen", monedaOrigen);
        registro.put("monedaDestino", monedaDestino);
        registro.put("cantidadOrigen", df.format(cantidadOrigen));
        registro.put("cantidadDestino", df.format(cantidadDestino));
        
        historialConversiones.add(registro);
        
        // Mantener solo las últimas 10 conversiones
        if (historialConversiones.size() > 10) {
            historialConversiones.remove(0);
        }
    }
    
    /**
     * Obtiene el historial de conversiones
     */
    public List<Map<String, Object>> getHistorialConversiones() {
        return new ArrayList<>(historialConversiones);
    }
    
    /**
     * Obtiene la lista de monedas soportadas
     */
    public List<String> getMonedasSoportadas() {
        return new ArrayList<>(monedasSoportadas);
    }
}