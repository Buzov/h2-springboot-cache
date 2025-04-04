package com.javaquasar.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CacheApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void fullRestCycle_shouldWork() throws Exception {
        String baseUrl = "http://localhost:" + port + "/cache/";

        // Save a new cache entry
        SaveCacheEntry saveEntry = new SaveCacheEntry();
        saveEntry.setKey("integrationKey");
        saveEntry.setValue("integrationValue");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(saveEntry),
            headers
        );

        ResponseEntity<Void> postResponse = restTemplate.postForEntity(baseUrl, request, Void.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Retrieve the saved entry
        ResponseEntity<CacheEntry> getResponse = restTemplate.getForEntity(baseUrl + "integrationKey", CacheEntry.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getValue()).isEqualTo("integrationValue");
    }
}
