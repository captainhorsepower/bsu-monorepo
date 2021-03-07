package edu.varabei.artsiom.calculator.ui;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@Configuration
@ConfigurationProperties("calculator.components")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutConfigProperties {

    ComponentProperties identity;

    List<TextFieldProperties> numberInputs;
    List<ComponentProperties> mathOps;

    ButtonProperties addButton;
    ButtonProperties subButton;
    ButtonProperties multButton;
    ButtonProperties divButton;

    ResultProperties result;

    @PostConstruct
    void heh() {
        // upd number inputs
        val firstNum = numberInputs.get(0);
        int y = firstNum.getY();
        int yStep = 70;
        int yOp = y + yStep / 2;
        int xOp = firstNum.getWidth() / 2 + firstNum.getX();
        for (TextFieldProperties l : numberInputs) {
            l.setY(y);
            y += yStep;
        }

        // init math ops
        List<ComponentProperties> mathOpProps = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            val prop = new ComponentProperties(
                    "none",
                    xOp, yOp,
                    20, 25
            );
            yOp += yStep;
            mathOpProps.add(prop);
        }
        setMathOps(mathOpProps);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
        String initValue;
    }
}
