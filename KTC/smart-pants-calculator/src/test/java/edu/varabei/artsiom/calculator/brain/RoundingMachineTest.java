package edu.varabei.artsiom.calculator.brain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class RoundingMachineTest {


    @Test
    public void round() {
        test("0.011", "0.0111", 3, RoundingMode.DOWN);
        test("0.01", "0.0111", 2, RoundingMode.DOWN);
        test("0", "0.0111", 1, RoundingMode.DOWN);
    }

    private static void test(String expected, String raw, int prec, RoundingMode mode) {
        BigDecimal rounded = RoundingMachine.round(new BigDecimal(raw), prec, mode);
        Assertions.assertEquals(expected, new DecimalParser().parse(rounded));
    }
}