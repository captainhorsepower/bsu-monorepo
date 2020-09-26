package edu.varabei.artsiom.calculator.ui;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("calculator")
@Data
public class MainWindowProperties {

    String title;
    Integer width;
    Integer height;

}
