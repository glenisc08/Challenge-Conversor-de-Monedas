package src.Modelo;

import java.util.Map;

public class RespuestaTasaDeCambio {

    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates;

    public String getResult() {
        return result;
    }

    public String getBase_code() {
        return base_code;
    }

    public Map<String, Double> getConversion_rates() {
        return conversion_rates;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setBase_code(String base_code) {
        this.base_code = base_code;
    }

    public void setConversion_rates(Map<String, Double> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }

    @Override
    public String toString() {
        return "RespuestaTasaDeCambio{" +
                "result='" + result + '\'' +
                ", base_code='" + base_code + '\'' +
                ", conversion_rates=" + conversion_rates +
                '}';
    }
}