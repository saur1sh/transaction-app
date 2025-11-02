package com.demo.bankapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    /**
     * Creates and registers a RestTemplate bean in the Spring application context.
     * This allows it to be autowired into other services.
     */
    @Bean(name = "exchangeRestTemplate")
    public RestTemplate exchangeRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.setContentType(MediaType.APPLICATION_JSON);
        defaultHeaders.add("Accept", "application/json");

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().addAll(defaultHeaders);
            return execution.execute(request, body);
        });

        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
