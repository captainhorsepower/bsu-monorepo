package edu.varabei.artsiom.calculator.ui;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.awt.*;

@Data
@Configuration
@ConfigurationProperties("calculator.components")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutConfigProperties {

    ComponentProperties leftNumber;
    ComponentProperties rightNumber;
    ComponentProperties addButton;
    ComponentProperties subButton;
    ComponentProperties result;

    @Data
    static class ComponentProperties {
        int x, y, width, height;

        public Rectangle bounds() {
            return new Rectangle(x, y, width, height);
        }
    }
}