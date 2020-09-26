package edu.varabei.artsiom.calculator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InnerComponentsConfig {

    @Bean
    public NumberInputTextField inputTextField() {
        NumberInputTextField textField = new NumberInputTextField();
        textField.setBounds(50, 50, 100, 20);
        return textField;
    }

}
