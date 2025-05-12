package src.Cliente;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente para realizar solicitudes a la API ExchangeRate
 */
public class ExchangeRateClient {
    private static final String API_KEY = "e8c6b3da43a41c53a59d0edd";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient httpClient;

    public ExchangeRateClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Obtiene las tasas de cambio actuales para una moneda base
     * @param baseCurrency Código de la moneda base (ej: USD)
     * @return Respuesta JSON con las tasas de cambio
     * @throws IOException Si hay un error de E/S
     * @throws InterruptedException Si la solicitud es interrumpida
     */
    public String getExchangeRates(String baseCurrency) throws IOException, InterruptedException {
        String url = BASE_URL + API_KEY + "/latest/" + baseCurrency;
        
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Error al obtener las tasas de cambio. Código de estado: " + response.statusCode());
        }
        
        return response.body();
    }
}