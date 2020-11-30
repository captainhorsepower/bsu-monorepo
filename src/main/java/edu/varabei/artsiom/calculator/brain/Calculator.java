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

    private static BigDecimal INF = new BigDecimal("1000000000001.00000000000");

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

    public BigDecimal calc(String postfixNotation) {
        return calc(postfixNotation, 20, RoundingMode.HALF_UP);
    }

    ;

    public BigDecimal calc(String postfixNotation, RoundingMode mode) {
        return calc(postfixNotation, 10, mode);
    }

    public BigDecimal calc(String postfixNotation, int calcScale, RoundingMode mode) {
        val math = "+-*/";
        val calcContext = new MathContext(calcScale, mode);
        Deque<BigDecimal> stack = new ArrayDeque<>();
        for (String item : postfixNotation.split(" ")) {
            if (!math.contains(item))
                stack.addFirst(new BigDecimal(item));
            else {
                val right = stack.pop();
                val left = stack.pop();
                val res = doMath(left, right, item, calcContext);
                if (res.compareTo(INF) > 0) {
                    throw new RuntimeException(
                            String.format("Промежутчный результат слишком велик!\nres=%s", res.toString())
                    );
                }
                stack.addFirst(res);
            }

        }
        return stack.pop();
    }


    @SneakyThrows
    BigDecimal doMath(BigDecimal left, BigDecimal right, String op, MathContext context) {
        final BigDecimal res = (BigDecimal) mathMethods.get(op).invoke(left, right, context);
        RoundingMachine.round(res, 10, RoundingMode.HALF_UP);
        return res;
    }

}
