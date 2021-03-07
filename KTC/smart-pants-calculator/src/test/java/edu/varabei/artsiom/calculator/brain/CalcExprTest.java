package edu.varabei.artsiom.calculator.brain;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.util.Arrays;

public class CalcExprTest {

    Calculator calculator = new Calculator();
    ExprToNotation exprToNotation = new ExprToNotation();
    DecimalParser decimalParser = new DecimalParser();

    @Test
    public void test() {
        String
                a = "986 282 584 876,635029",
                b = "123456.666666",
                c = "987654.777777",
                d = "-8.888888";
        val expr = createExpr("%s - ( %s * %s ) / %s", a, b, c, d);
        testCalc("999 999 999 999.999998", expr);
    }

    @Test
    public void test1() {
        testCalc("-1.4", "1 - ( 3 * 4 ) / 5");
    }

    @Test
    public void basicMath() {
        testCalc("-1", "1 - 2");
        testCalc("1", "3 - 2");
        testCalc("1.5", "3 / 2");
        testCalc("0.75", "3 / 4");
        testCalc("0.75", "-3 / -4");
        testCalc("12", "-3 * -4");
        testCalc("-88.88", "-10 * 8.888");
        testCalc("-88.88", "10 * -8.888");
        testCalc("8.88", "10 * .888");
    }

    @Test
    public void basicMathLargeNumbers() {
        testCalc("1", "1");
        testCalc("986 282 584 876.635029", createExpr("%s", "986 282 584 876,635029"));

        testCalc("0.518517", createExpr(
                "%s * %s",
                ".666666", ".777777"
        ));

        testCalc("121 932 566 681.097393", createExpr(
                "%s * %s",
                "123456.666666", "987654.777777"
        ));
    }


    void testCalc(String ans, String expr) {testCalc(ans, expr, RoundingMode.DOWN);}

    void testCalc(String ans, String expr, RoundingMode roundingMode) {
        val postfixNotation = exprToNotation.toPostfixNotation(expr);
        val res = calculator.calc(postfixNotation);
        val rounded = RoundingMachine.round(res, 6, roundingMode);
        Assertions.assertEquals(ans, decimalParser.parse(rounded));
    }

    String createExpr(String format, String... decimals) {
        final String expr = String.format(
                format,
                Arrays.stream(decimals).map(decimalParser::parse).toArray()
        );
        System.out.println(expr);
        return expr;
    }
}
