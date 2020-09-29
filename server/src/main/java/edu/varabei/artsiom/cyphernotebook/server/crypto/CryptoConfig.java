package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class CryptoConfig {

    @Bean
    Charset charset() {
        return StandardCharsets.US_ASCII;
    }

    @Bean
    String aesTransformation() {
        return "AES/CBC/PKCS5Padding";
    }

    @Bean
    @SneakyThrows
    KeyGenerator aesKeyGenerator() {
        return KeyGenerator.getInstance("AES");
    }

}
