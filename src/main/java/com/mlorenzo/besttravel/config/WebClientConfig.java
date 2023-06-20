package com.mlorenzo.besttravel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource(value = "classpath:api-currency.properties")
public class WebClientConfig {
    private final String baseUrl;
    private final String apiKeyHeader;
    private final String apiKey;

    public WebClientConfig(@Value("${api.base-url}") final String baseUrl,
                           @Value("${api.api-key.header}") final String apiKeyHeader,
                           @Value("${api.api-key}") final String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKeyHeader = apiKeyHeader;
        this.apiKey = apiKey;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }
}
