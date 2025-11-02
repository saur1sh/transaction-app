package com.demo.bankapp.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FetchExchangeRatesHelper {

    @Qualifier("exchangeRestTemplate")
    @NonNull
    private final RestTemplate restTemplate;

    @Value("${ext-api.exchange.access-token}")
    String accessToken;

    @Value("${ext-api.exchange.uri}")
    private String exchangeUri;

    @Value("${ext-api.mock.use-mock-data}")
    private boolean useMockData;

    @Value("${ext-api.mock.mock-data-url}")
    private String mockUri;


    @Cacheable("currencyRates")
    public Map<String, Double> getCurrencyRates() {
        String uri = useMockData ? mockUri : exchangeUri + accessToken;

        if (useMockData) {
            ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    }
            );

            List<Map<String, Object>> responseList = responseEntity.getBody();

            if (responseList == null || responseList.isEmpty()) {
                throw new RuntimeException("Empty response from mock currency API.");
            }

            Object ratesObj = responseList.get(0).get("rates");
            if (!(ratesObj instanceof Map)) {
                throw new RuntimeException("Invalid 'rates' field in mock API response.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Double> rates = (Map<String, Double>) ratesObj;
            return rates;
        } else {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> responseBody = responseEntity.getBody();

            if (responseBody == null || responseBody.get("rates") == null) {
                throw new RuntimeException("Missing 'rates' field in real API response.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Double> rates = (Map<String, Double>) responseBody.get("rates");
            return rates;
        }
    }
}
