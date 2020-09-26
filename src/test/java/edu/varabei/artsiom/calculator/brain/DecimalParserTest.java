package edu.varabei.artsiom.calculator.brain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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

    private boolean decimalsEqual(String expected, String actual) {
        expected = expected.replaceAll("_", "");
        return 0 == new BigDecimal(expected).compareTo(new BigDecimal(actual));
    }

    private boolean decimalsEqual(String expected, BigDecimal actual) {
        return decimalsEqual(expected, actual.toString());
    }

}