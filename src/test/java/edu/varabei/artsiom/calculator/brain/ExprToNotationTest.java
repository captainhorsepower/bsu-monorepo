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

}