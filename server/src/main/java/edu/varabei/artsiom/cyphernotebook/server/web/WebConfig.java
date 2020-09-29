package edu.varabei.artsiom.cyphernotebook.server.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
