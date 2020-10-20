package edu.varabei.artsiom.calculator.brain;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest implements CompareDecimalsMixin {

    DecimalParser parser = new DecimalParser();
    Calculator calc = new Calculator(parser);

    @Test
    public void addSimpleCases() {
        assertEquals("30", calc.add("10", "20"));
        assertEquals("1.1", calc.add("0.3", "0.8"));
        assertEquals("1.1", calc.add("000.3", "000000.8"));
        assertEquals("1.1", calc.add("2.6", "-1.5"));
        assertEquals("1.1", calc.add("+2,6", "-1,5"));
    }

    @Test
    public void broken() {
        assertEquals("3", calc.add("- 1 231", "+ 1 234"));
        assertEquals("-2 465", calc.sub("- 1 231", "+ 1 234"));
    }

    @Test
    public void addLongCases() {
        assertTrue(decimalsEqual(
                "1_000_000_000_000",
                calc.add("999_999_999_998", "2.0000000000000")));
    }

    @Test
    public void subLongCases() {
        assertTrue(decimalsEqual(
                "110",
                calc.sub("87654321098765432.1", "87654321098765322.1")));
        assertTrue(decimalsEqual(
                "999_999_999_998",
                calc.sub("1_000_000_000_000", "2.0000000000000")));
    }

    @Test
    public void whatsMathContext() {
        val roundUp = new MathContext(2, RoundingMode.UP);
        val roundDown = new MathContext(2, RoundingMode.DOWN);
        val roundHalfEven = new MathContext(2, RoundingMode.HALF_EVEN);
        assertTrue(decimalsEqual(
                "1.13",
                BigDecimal.ONE
                        .add(new BigDecimal(".123", roundUp))
        ));
        assertTrue(decimalsEqual(
                "1.12",
                BigDecimal.ONE
                        .add(new BigDecimal(".123", roundDown))
        ));
        assertTrue(decimalsEqual(
                "1.12",
                BigDecimal.ONE
                        .add(new BigDecimal(".125", roundHalfEven))
        ));
        assertTrue(decimalsEqual(
                "1.12",
                BigDecimal.ONE
                        .add(new BigDecimal(".115", roundHalfEven))
        ));
    }

}