package edu.varabei.artsiom.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CalculatorApplication.class)
                .headless(false)
                .run(args);
    }

}
