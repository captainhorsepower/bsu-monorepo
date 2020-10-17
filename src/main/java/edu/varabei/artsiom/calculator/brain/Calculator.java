package edu.varabei.artsiom.calculator.brain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final DecimalParser parser;
    private final int resultScale;
    private final int calcScale;

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

//    public String calc(Expr expr, int roundingMode) {
//        // have four numbers, three operations.
//        // two middle numbers are immediately combined.
//
//        // then need to combine left with middle or middle with third.
//        // and then the rest.
//
//        BigDecimal left, mid, right;
//        mid = parser.parse(expr.midFirst.get())
//                sop
//            parser.parse(expr.midLast.get());
//
//        int op
//        if (expr.top > expr.fop) {
//
//        }
//        "subtract, add, multiply, divide";
//        new MathContext()
//        left.divi
//        BigDecimal.class.getMethod()
//
//        BigDecimal result = left op right;
//
//        return parser.parse(result.setScale(resultScale, roundingMode));
//    }


    @Getter
    public static class Expr {
        public static final int ADD = 1, SUB = 1, MULT = 2, DIV = 2;
        Supplier<String> first, midFirst, midLast, last;
        Supplier<Integer> fop, sop, top; // first, second, third operation
    }

}
