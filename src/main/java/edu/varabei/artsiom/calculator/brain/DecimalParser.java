package edu.varabei.artsiom.calculator.brain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DecimalParser {

    public BigDecimal parse(String input) {
        input = input.replaceAll("[\\s_]", "");
        input = input.replaceAll(",", ".");
        return new BigDecimal(input);
    }

}
