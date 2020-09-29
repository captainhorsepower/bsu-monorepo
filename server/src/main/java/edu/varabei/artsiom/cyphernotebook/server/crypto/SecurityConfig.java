package edu.varabei.artsiom.cyphernotebook.server.crypto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Bean
    Charset charset() {
        return StandardCharsets.US_ASCII;
    }

    @Bean
    String aesTransformation() {
        return "AES/CBC/PKCS5Padding";
    }

}
