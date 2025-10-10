package com.jobtracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    @Value("${PYTHON_SERVICE_URL}")
    private String pythonServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public float[] getEmbedding(String text) {
        if (text == null || text.trim().isEmpty()) {
            log.warn("⚠️ Empty text received for embedding generation.");
            return new float[768];
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of("text", text);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    pythonServiceUrl + "/embed",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("embedding")) {
                log.error("❌ Invalid response from embedding service: {}", responseBody);
                throw new RestClientException("Invalid response from embedding service");
            }

            List<?> embeddingList = (List<?>) responseBody.get("embedding");
            if (embeddingList == null || embeddingList.isEmpty()) {
                log.error("❌ Empty embedding data from service");
                throw new RestClientException("Empty embedding data from service");
            }

            float[] embedding = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embedding[i] = ((Number) embeddingList.get(i)).floatValue();
            }

            log.info("✅ Successfully retrieved embedding of length {}", embedding.length);
            return embedding;

        } catch (RestClientException e) {
            log.error("🔥 Error calling embedding service: {}", e.getMessage());
            throw new RestClientException("Failed to get embedding from Python service", e);
        }
    }
}
