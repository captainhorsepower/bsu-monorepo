package edu.varabei.artsiom.calculator.brain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final StringIsValidNumberValidator validator;

    public String add(String num1, String num2) {
        return "result will be here";
    }

    public String sub(String left, String right) {
        return "result will be here";
    }

}
