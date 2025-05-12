package src.Cliente;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import src.Modelo.RespuestaTasaDeCambio;

public class ClienteTasaDeCambio {

    private static final String API_KEY = "e8c6b3da43a41c53a59d0edd";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient httpClient;
    private final Gson gson;

    public ClienteTasaDeCambio() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     *
     * @param monedaBase 
     * @return 
     * @throws IOException
     * @throws InterruptedException
     */
    public RespuestaTasaDeCambio obtenerTasasDeCambio(String monedaBase) throws IOException, InterruptedException {
        URI direccion = URI.create(BASE_URL + API_KEY + "/latest/" + monedaBase);
        HttpRequest request = HttpRequest.newBuilder().uri(direccion).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error al obtener las tasas de cambio: " + response.statusCode());
        }

        return gson.fromJson(response.body(), RespuestaTasaDeCambio.class);
    }
}