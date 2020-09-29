package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;

@Configuration
public class AESConfig {

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
