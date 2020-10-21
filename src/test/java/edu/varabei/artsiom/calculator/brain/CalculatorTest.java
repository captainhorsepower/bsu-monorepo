package edu.varabei.artsiom.calculator.brain;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest implements CompareDecimalsMixin {

    Calculator calc = new Calculator();

    @Test
    public void calc() {
        assertTrue(decimalsEqual(
                "2",
                calc.calc("1 1 +", RoundingMode.DOWN)
        ));
        assertTrue(decimalsEqual(
                "3",
                calc.calc("2 2 2 * + 3 -", RoundingMode.DOWN)
        ));
        assertEquals(
                new BigDecimal("6"),
                calc.calc("0 2 3 * + 0 +")
        );
        assertEquals(
                new BigDecimal("3"),
                calc.calc("0 9 3 / + 0 +")
        );
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