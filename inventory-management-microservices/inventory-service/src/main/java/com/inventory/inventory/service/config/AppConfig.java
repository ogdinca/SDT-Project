package com.inventory.inventory.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * RestTemplate bean for making HTTP calls to other services
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}