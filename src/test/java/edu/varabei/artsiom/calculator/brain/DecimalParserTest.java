package edu.varabei.artsiom.calculator.brain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DecimalParserTest {

    DecimalParser parser = new DecimalParser();

    @Test
    public void redTest() {
        assertTrue(decimalsEqual("1234.566", parser.parse("1 234.566")));
        assertTrue(decimalsEqual("1234.566", parser.parse("1 234,566")));
    }

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
    public void formattedDecimalPart() {
        assertEquals("", parser.fromattedDecimalPart(new BigDecimal("1.00000")));
        assertEquals("", parser.fromattedDecimalPart(new BigDecimal("100000")));
        assertEquals(".1", parser.fromattedDecimalPart(new BigDecimal("1.1")));
        assertEquals(".01", parser.fromattedDecimalPart(new BigDecimal("1.01000")));
    }

    @Test
    public void formattedIntegerPart() {
        assertEquals("1 000", parser.formattedIntegerPart(
                parser.parse("1_000")));
        assertEquals("1", parser.formattedIntegerPart(
                parser.parse("00001")));
        assertEquals("1 234 567 000", parser.formattedIntegerPart(
                parser.parse("1_234_567_000")));
        assertEquals("10 234 567 000", parser.formattedIntegerPart(
                parser.parse("10_234_567_000")));
        assertEquals("0", parser.formattedIntegerPart(
                parser.parse("0000000")));
    }

    @Test
    public void parseDecimalToString() {
        assertEquals("1 000.123", parser.parse(
                parser.parse("1_000.123")));
        assertEquals("123 456 987.123", parser.parse(
                parser.parse("123_456_987.1230000")));
        assertEquals("1", parser.parse(
                parser.parse("00001.000000")));

        assertEquals("-1", parser.parse(
                parser.parse("-00001.000000")));
        assertEquals("-123 456 987.123", parser.parse(
                parser.parse("-123_456_987.1230000")));
        assertEquals("-2 465", parser.parse(new BigDecimal("-2465")));
    }


    @Test
    public void onlyOneSplit() {
        assertThrows(Throwable.class, () -> parser.onlyOneSplit("1234.123123"));
        assertThrows(Throwable.class, () -> parser.onlyOneSplit("12345.123123"));
        assertDoesNotThrow(() -> parser.onlyOneSplit("10"));
        assertDoesNotThrow(() -> parser.onlyOneSplit("101"));
        assertDoesNotThrow(() -> parser.onlyOneSplit("101.12312313"));
        assertDoesNotThrow(() -> parser.onlyOneSplit(".12312313"));
    }

    @Test
    public void checkFirst() {
        assertDoesNotThrow(() -> parser.checkFirst("1"));
        assertDoesNotThrow(() -> parser.checkFirst("12"));
        assertDoesNotThrow(() -> parser.checkFirst("123"));
        assertThrows(Throwable.class, () -> parser.checkFirst(""));
        assertThrows(Throwable.class, () -> parser.checkFirst("1234"));
    }

    @Test
    public void checkLast() {
        assertDoesNotThrow(() -> parser.checkLast("123"));
        assertDoesNotThrow(() -> parser.checkLast("123.1231231"));
        assertThrows(Throwable.class, () -> parser.checkLast("1234.1231231"));
        assertThrows(Throwable.class, () -> parser.checkLast("12.1231231"));
        assertThrows(Throwable.class, () -> parser.checkLast(".1231231"));
    }

    @Test
    public void checkMiddleTrain() {
        assertDoesNotThrow(() -> parser.checkMiddleTrain(Arrays.asList(
                "12 123 123.".split(" "))));
        assertDoesNotThrow(() -> parser.checkMiddleTrain(Arrays.asList(
                "123 123 123 123.123123123".split(" "))));
        assertDoesNotThrow(() -> parser.checkMiddleTrain(Arrays.asList(
                "12 123 123 456 123".split(" "))));
        assertDoesNotThrow(() -> parser.checkMiddleTrain(Arrays.asList(
                "12 123 123 456 123 123 123.".split(" "))));
        assertThrows(Throwable.class, () -> parser.checkMiddleTrain(Arrays.asList(
                "12 1234 123 456 123 123 123.".split(" "))));
        assertThrows(Throwable.class, () -> parser.checkMiddleTrain(Arrays.asList(
                "12 123 123 4564 123".split(" "))));

        assertThrows(Throwable.class, () -> parser.checkMiddleTrain(Arrays.asList(
                "12 1234 123".split(" "))));

        assertThrows(Throwable.class, () -> parser.checkMiddleTrain(Arrays.asList(
                "12 1234 123.".split(" "))));

        assertThrows(Throwable.class, () -> parser.checkMiddleTrain(Arrays.asList(
                "12 1234 123.123".split(" "))));
    }

    @Test
    public void allowOnlyValidSpaces() {
        assertThrows(Throwable.class, () -> parser.parse("-1 123 2"));
        assertThrows(Throwable.class, () -> parser.parse("1 123 2"));
        assertThrows(Throwable.class, () -> parser.parse("1 123 2.123"));

        assertDoesNotThrow(() -> parser.parse("1"));
        assertDoesNotThrow(() -> parser.parse("1.123123123"));
        assertDoesNotThrow(() -> parser.parse("12"));
        assertDoesNotThrow(() -> parser.parse("12.123123"));
        assertDoesNotThrow(() -> parser.parse("12 123"));
        assertDoesNotThrow(() -> parser.parse("12 123.1231"));
        assertDoesNotThrow(() -> parser.parse("1 123 123"));
        assertDoesNotThrow(() -> parser.parse("1 123 123.13123123"));
        assertDoesNotThrow(() -> parser.parse("112 123 123.13123123"));
        assertDoesNotThrow(() -> parser.parse("+112 123 123.13123123"));
        assertDoesNotThrow(() -> parser.parse("-112 123 123.13123123"));
        assertDoesNotThrow(() -> parser.parse("+ 112 123 123.13123123"));
        assertDoesNotThrow(() -> parser.parse("- 112 123 123.13123123"));
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