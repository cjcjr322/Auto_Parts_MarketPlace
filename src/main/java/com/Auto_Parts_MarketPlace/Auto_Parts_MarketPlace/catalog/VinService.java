package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class VinService {

    private static final Logger log = LoggerFactory.getLogger(VinService.class);
    private static final Pattern VIN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");
    private static final String YEAR_CODES = "ABCDEFGHJKLMNPRSTVWXY123456789";

    private final RestClient client;

    public VinService() {
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(http);
        factory.setReadTimeout(Duration.ofSeconds(10));
        this.client = RestClient.builder().requestFactory(factory).build();
    }

    public VinResult decode(String raw) {
        String vin = raw == null ? "" : raw.trim().toUpperCase().replaceAll("\\s", "");
        if (!VIN.matcher(vin).matches()) {
            return new VinResult(vin, "", "", "",
                    "VIN должен состоять из 17 символов: латинские буквы (кроме I, O, Q) и цифры.");
        }

        String make = "";
        String model = "";
        String year = "";
        try {
            Map<String, Object> body = client.get()
                    .uri("https://vpic.nhtsa.dot.gov/api/vehicles/DecodeVinValues/{vin}?format=json", vin)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
            if (body != null && body.get("Results") instanceof List<?> list
                    && !list.isEmpty() && list.get(0) instanceof Map<?, ?> r) {
                make = pretty(str(r.get("Make")));
                model = str(r.get("Model"));
                year = str(r.get("ModelYear"));
            }
        } catch (Exception e) {
            log.warn("Не удалось расшифровать VIN через NHTSA: {}", e.getMessage());
        }

        if (make.isEmpty()) {
            return new VinResult(vin, "", "", "",
                    "Не удалось определить автомобиль по этому VIN. Выберите марку и модель вручную ниже.");
        }
        if (year.isEmpty() || year.equals("0")) {
            year = yearFromVin(vin);
        }
        return new VinResult(vin, make, model, year, null);
    }

    /** Год по 10-му символу VIN. Для старых машин результат приблизительный. */
    private static String yearFromVin(String vin) {
        int idx = YEAR_CODES.indexOf(vin.charAt(9));
        if (idx < 0) return "";
        int year = 2010 + idx;
        if (year > Year.now().getValue() + 1) year -= 30;
        return String.valueOf(year);
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString().trim();
    }

    private static String pretty(String s) {
        StringBuilder sb = new StringBuilder();
        for (String w : s.toLowerCase().split(" ")) {
            if (w.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
        }
        return sb.toString();
    }
}