package edu.varabei.artsiom.calculator.brain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Calculator {

//    private final StringIsValidNumberValidator validator;

    public String add(String left, String right) {
        BigDecimal add = new BigDecimal(left).add(new BigDecimal(right));
        return add.toString();
    }

    public String sub(String left, String right) {
        BigDecimal sub = new BigDecimal(left).subtract(new BigDecimal(right));
        return sub.toString();
    }

}
