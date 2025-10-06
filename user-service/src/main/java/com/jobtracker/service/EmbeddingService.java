package com.jobtracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map;

@Service
public class EmbeddingService {

    private static final String python_api_url = "http://localhost:8000/embed";
    private final RestTemplate restTemplate = new RestTemplate();

    public float[] getEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("text", text);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            python_api_url, HttpMethod.POST, request,
             new ParameterizedTypeReference<Map<String, Object>>() {}
            );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            @SuppressWarnings("unchecked")
            var list = (java.util.List<Double>) response.getBody().get("mbedding");
            float[] embedding = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                embedding[i] = list.get(i).floatValue();
            }
            return embedding;
        }
        throw new RestClientException("Failed to fetch embedding");
    }
}