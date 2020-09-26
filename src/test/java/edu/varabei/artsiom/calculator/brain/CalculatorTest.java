package edu.varabei.artsiom.calculator.brain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calc = new Calculator();

    @Test
    public void addSimpleCases() {
        assertEquals("30", calc.add("10", "20"));
        assertEquals("1.1", calc.add("0.3", "0.8"));
        assertEquals("1.1", calc.add("000.3", "000000.8"));
        assertEquals("1.1", calc.add("2.6", "-1.5"));
    }

    @Test
    public void addLongCases() {
        assertTrue(decimalsEqual(
                "1_000_000_000_000",
                calc.add("999_999_999_998", "2.0000000000000")));
    }

    @Test
    public void subLongCases() {
        assertTrue(decimalsEqual("110", calc.sub("87654321098765432.1", "87654321098765322.1")));
    }

    private boolean decimalsEqual(String expected, String actual) {
        expected = expected.replaceAll("_", "");
        return 0 == new BigDecimal(expected).compareTo(new BigDecimal(actual));
    }

}