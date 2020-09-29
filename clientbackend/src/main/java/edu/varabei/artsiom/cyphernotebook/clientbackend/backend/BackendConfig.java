package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import lombok.val;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BackendConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder templateBuilder) {
        return templateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()))
                //TODO 9/30/20: extract cookie
//        https://stackoverflow.com/questions/22853321/resttemplate-client-with-cookies
//                .interceptors(new StatefulRestTemplateInterceptor())
                .build();
    }

    @Bean
    StateStore concurrentMapStateStore() {
        return new StateStore();
    }

}
