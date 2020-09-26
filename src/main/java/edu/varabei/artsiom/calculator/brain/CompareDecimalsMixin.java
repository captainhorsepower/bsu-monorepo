package edu.varabei.artsiom.calculator.brain;

import java.math.BigDecimal;

public interface CompareDecimalsMixin {

    default boolean decimalsEqual(String expected, String actual) {
        expected = expected.replaceAll("_", "");
        return 0 == new BigDecimal(expected).compareTo(new BigDecimal(actual));
    }

    default boolean decimalsEqual(String expected, BigDecimal actual) {
        return decimalsEqual(expected, actual.toString());
    }

}
