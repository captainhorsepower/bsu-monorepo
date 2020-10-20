package edu.varabei.artsiom.calculator.brain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final Map<String, Method> mathMethods = new LinkedHashMap<String, Method>() {
        {
            put("+", getMethod("add"));
            put("-", getMethod("subtract"));
            put("*", getMethod("multiply"));
            put("/", getMethod("divide"));
        }

        @SneakyThrows
        private Method getMethod(String name) {
            return BigDecimal.class.getMethod(name, BigDecimal.class, MathContext.class);
        }
    };

    public BigDecimal calc(String postfixNotation, RoundingMode mode) {
        val calcScale = 10;
        val resultScale = 6;
        val math = "+-*/";

        val calcContext = new MathContext(calcScale, mode);

        Deque<BigDecimal> stack = new ArrayDeque<>();
        for (String item : postfixNotation.split(" ")) {
            if (!math.contains(item))
                stack.add(new BigDecimal(item));
            else {
                val left = stack.pop();
                val right = stack.pop();
                stack.add(doMath(left, right, item, calcContext));
            }

        }

        val resContext = new MathContext(resultScale, mode);
        return stack.pop().round(resContext);
    }


    @SneakyThrows
    BigDecimal doMath(BigDecimal left, BigDecimal right, String op, MathContext context) {
        return (BigDecimal) mathMethods.get(op).invoke(left, right, context);
    }

}
