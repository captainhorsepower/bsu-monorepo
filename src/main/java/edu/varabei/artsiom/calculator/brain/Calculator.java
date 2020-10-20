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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final DecimalParser parser;
    //TODO 10/20/20: change to config
    private final int resultScale = 10;
    private final int calcScale = 6;
    private final Map<MathOP, Method> mathMethods = new LinkedHashMap<MathOP, Method>() {
        {
            put(MathOP.ADD, getMethod("add"));
            put(MathOP.SUB, getMethod("subtract"));
            put(MathOP.MULT, getMethod("multiply"));
            put(MathOP.DIV, getMethod("divide"));
        }

        @SneakyThrows
        private Method getMethod(String name) {
            return BigDecimal.class.getMethod(name, BigDecimal.class, MathContext.class);
        }
    };

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

    public String calc(Expr expr, RoundingMode mode) {
        val calcContext = new MathContext(calcScale, mode);
        val resContext = new MathContext(resultScale, mode);

        val mid = doMath(
                parser.parse(expr.getMidFirst().get()),
                parser.parse(expr.getMidLast().get()),
                expr.sop.get(),
                calcContext
        );

        BigDecimal left = parser.parse(expr.getFirst().get());
        BigDecimal right = parser.parse(expr.getLast().get());

        MathOP op;
        if (expr.top.get().p > expr.fop.get().p) {
            right = doMath(mid, right, expr.top.get(), calcContext);
            op = expr.fop.get();
        } else {
            left = doMath(left, mid, expr.fop.get(), calcContext);
            op = expr.top.get();
        }

        return parser.parse(doMath(left, right, op, resContext));
    }

    @SneakyThrows
    BigDecimal doMath(BigDecimal left, BigDecimal right, MathOP op, MathContext context) {
        return (BigDecimal) mathMethods.get(op).invoke(left, right, context);
    }


    @Getter
    public static class Expr {
        public static final int ADD = 1, SUB = 1, MULT = 2, DIV = 2;
        Supplier<String> first, midFirst, midLast, last;
        Supplier<MathOP> fop, sop, top; // first, second, third operation
    }

    @RequiredArgsConstructor
    public enum MathOP {
        ADD(1), SUB(1),
        MULT(2), DIV(2);
        private final int p;
    }

}
