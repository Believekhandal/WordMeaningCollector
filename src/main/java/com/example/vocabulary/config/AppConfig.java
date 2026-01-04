package com.example.vocabulary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${api.read-timeout:5000}")
    private int readTimeout;

    @Value("${api.connect-timeout:10000}")
    private int connectTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // Set configurable connection timeout (default 10 seconds)
        factory.setConnectTimeout(connectTimeout);

        // Set configurable read timeout (default 5 seconds as requested)
        factory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
