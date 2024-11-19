package com.entregable.sistema.util;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpClientUtil {

    private static final RestTemplate restTemplate;

    static {
        org.apache.hc.client5.http.classic.HttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(factory);
    }

    public static String sendPatchRequest(String url, HttpEntity<String> requestEntity) {
        String response = "";
        try {
            // Realiza la solicitud PATCH
            response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class).getBody();
        } catch (Exception e) {
            response = "FAILED: " + e.getMessage();
            // Lanza una excepción personalizada o devuelve el error para que sea manejado en TaskService
            throw new RuntimeException("Error en PATCH request: " + e.getMessage());
        }
        return response;
    }
}
