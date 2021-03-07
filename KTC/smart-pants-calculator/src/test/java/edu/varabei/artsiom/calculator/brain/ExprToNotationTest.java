package edu.varabei.artsiom.calculator.brain;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class ExprToNotationTest {

    private ExprToNotation parser = new ExprToNotation();

    @Test
    public void stringTests() {
        val expr = "a + b * c - d";
        assertEquals("a b c * + d -", toPostfixNotation(expr));
        assertEquals("a b +", toPostfixNotation("a + b"));
        assertEquals("a b + c -", toPostfixNotation("a + b - c"));
        assertEquals("a b c - +", toPostfixNotation("a + ( b - c )"));
        assertEquals("a b c - +", toPostfixNotation("a + ( b - c )"));
        assertEquals("a b c - *", toPostfixNotation("a * ( b - c )"));
        assertEquals("a b c - * d /", toPostfixNotation("a * ( b - c ) / d"));
        assertEquals("1.123 4.123 +", toPostfixNotation("1.123 + 4.123"));
    }

    @Test
    public void redTest() {
        assertEquals("a b + c + d +", toPostfixNotation("a + b + c + d"));
        assertEquals("a b c + + d +", toPostfixNotation("a + ( b + c ) + d"));
    }

    String toPostfixNotation(String expr) {
        return parser.toPostfixNotation(expr);
    }


    // 986 282 584 876,635029 - (123456.666666*987654.777777)/(-8.888888)=999 999 999 999.999998
    @Test
    public void largeNumbersTest() {
        assertEquals("a b c * d / -", toPostfixNotation("a - ( b * c ) / d"));
        // FIXME: 11/30/20 
//        assertEquals("a b c * d / -", toPostfixNotation("a - b * c / d"));

        String a = "986282584876,635029",
                b = "123456.666666",
                c = "987654.777777",
                d = "-8.888888";
        String infix = String.format("%s - ( %s * %s ) / %s", a, b, c, d);
        String postfix = String.format("%s %s %s * %s / -", a, b, c, d);
        assertEquals(postfix, toPostfixNotation(infix));
    }
}