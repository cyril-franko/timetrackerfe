package com.cyrof.configuration;

import com.cyrof.timetracker.integration.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TimetrackerConfig {


    @Bean
    @Primary
    public ApiClient apiDevClient(@Value("${integration.timetracker.base-path}") String baseUrl) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseUrl);
        return apiClient;
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
