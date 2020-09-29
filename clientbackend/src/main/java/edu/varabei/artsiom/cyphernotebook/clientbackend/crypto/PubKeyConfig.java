package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;

@Configuration
public class PubKeyConfig {

    @Bean
    String pubKeyTransformation() {
        return "RSA/ECB/PKCS1Padding";
    }

    @Bean
    String pubKeyAlgorithm() {
        return "RSA";
    }

    @Bean
    @SneakyThrows
    KeyFactory pubKeyFactory() {
        return KeyFactory.getInstance(pubKeyAlgorithm());
    }
}
