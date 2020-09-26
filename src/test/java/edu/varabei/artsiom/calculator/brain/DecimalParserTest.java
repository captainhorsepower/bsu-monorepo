package edu.varabei.artsiom.calculator.brain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DecimalParserTest {

    DecimalParser parser = new DecimalParser();

    @Test
    public void simpleCases() {
        assertTrue(decimalsEqual("10", parser.parse("10.0")));
        assertTrue(decimalsEqual(".9", parser.parse("0.9")));
    }

    @Test
    public void withNumDelimiters() {
        assertTrue(decimalsEqual("1_000_001", parser.parse("1_000_001")));
        assertTrue(decimalsEqual("1_000_001", parser.parse("1 000 001")));
    }

    @Test
    public void bothDotAndComma() {
        assertTrue(decimalsEqual("1.1", parser.parse("1.1")));
        assertTrue(decimalsEqual("1.1", parser.parse("1,1")));
    }

    @Test
    public void removeTrailingZeros() {
        assertEquals("1", parser.parse(new BigDecimal("1.00000")));
        assertEquals("1.01", parser.parse(new BigDecimal("1.01000")));
    }

    @Test
    public void formattedIntegerPart() {
        assertEquals("1 000", parser.formattedIntegerPart(
                parser.parse("1_000")));
        assertEquals("1 234 567 000", parser.formattedIntegerPart(
                parser.parse("1_234_567_000")));
        assertEquals("0", parser.formattedIntegerPart(
                parser.parse("0000000")));
    }

    @Test
    public void test() {
        Arrays.stream(new BigDecimal("12345.101").toBigInteger().divideAndRemainder(new BigInteger("10")))
                .forEach(System.out::println);
    }

    private boolean decimalsEqual(String expected, String actual) {
        expected = expected.replaceAll("_", "");
        return 0 == new BigDecimal(expected).compareTo(new BigDecimal(actual));
    }

    private boolean decimalsEqual(String expected, BigDecimal actual) {
        return decimalsEqual(expected, actual.toString());
    }

}