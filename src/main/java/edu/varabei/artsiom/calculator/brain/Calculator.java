package edu.varabei.artsiom.calculator.brain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final DecimalParser parser;

    public String add(String left, String right) {
        return parser.parse(left)
                .add(parser.parse(right))
                .toString();
    }

    public String sub(String left, String right) {
        return parser.parse(left)
                .subtract(parser.parse(right))
                .toString();
    }

    public String mult(String left, String right) {
        return parser.parse(left)
                .multiply(parser.parse(right))
                .toString();
    }

    public String div(String left, String right) {
        return parser.parse(left)
                .divide(parser.parse(right), 6, RoundingMode.HALF_EVEN)
                .toString();
    }

}
