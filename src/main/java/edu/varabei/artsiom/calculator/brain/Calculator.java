package edu.varabei.artsiom.calculator.brain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

}
