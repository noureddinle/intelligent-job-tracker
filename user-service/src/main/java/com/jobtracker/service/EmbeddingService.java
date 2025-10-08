package com.jobtracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);
    private static final String python_api_url = "http://localhost:8001/embed";
    private final RestTemplate restTemplate = new RestTemplate();

    public float[] getEmbedding(String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of("text", text);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(python_api_url, request, Map.class);
            if(response.getBody() == null || !response.getBody().containsKey("embedding")) {
                log.error("Invalid response from embedding service = {} ", response.getBody());
                throw new RestClientException("Invalid response from embedding service");
            }

            List<?> embeddingList = (List<?>) response.getBody().get("embedding");
            if (embeddingList == null || embeddingList.isEmpty()) {
                log.error("Invalid embedding data from service = {} ", embeddingList);
                throw new RestClientException("Invalid embedding data from service");
            }

            float[] embedding = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embedding[i] = ((Number) embeddingList.get(i)).floatValue();
            }
            log.info("Successfully retrieved embedding from service = {} ", embedding.length);
            return embedding;
            } catch (RestClientException e) {
                log.error("Error calling embedding service: {}", e.getMessage());
                throw new RestClientException("Failed to get embedding from service", e);
        }
    }
}