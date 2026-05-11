package com.example.bw_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${webclient.base-url}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
