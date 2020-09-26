package edu.varabei.artsiom.calculator.ui;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Data
@Configuration
@ConfigurationProperties("calculator.components")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutConfigProperties {

    ComponentProperties identity;
    TextFieldProperties leftNumber;
    TextFieldProperties rightNumber;
    ButtonProperties addButton;
    ButtonProperties subButton;
    ButtonProperties multButton;
    ButtonProperties divButton;
    ResultProperties result;

    @Data
    static class ComponentProperties {
        String label;
        int x, y, width, height;

        public Rectangle bounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    @Data
    static class ButtonProperties extends ComponentProperties {
        String title;
    }

    @Data
    static class ResultProperties extends ComponentProperties {
        String initValue;
    }

    @Data
    static class TextFieldProperties extends ComponentProperties {
        int maxLen;
    }
}
