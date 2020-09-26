package edu.varabei.artsiom.calculator.brain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final DecimalParser parser;

    public String add(String left, String right) {
        return parser.parse(
                parser.parse(left)
                        .add(parser.parse(right))
                        .setScale(6, RoundingMode.HALF_EVEN));
    }

    public String sub(String left, String right) {
        return parser.parse(
                parser.parse(left)
                        .subtract(parser.parse(right))
                        .setScale(6, RoundingMode.HALF_EVEN));
    }

    public String mult(String left, String right) {
        return parser.parse(
                parser.parse(left)
                        .multiply(parser.parse(right))
                        .setScale(6, RoundingMode.HALF_EVEN));
    }

    public String div(String left, String right) {
        return parser.parse(
                parser.parse(left)
                        .divide(parser.parse(right), 6, RoundingMode.HALF_EVEN));
    }

}
