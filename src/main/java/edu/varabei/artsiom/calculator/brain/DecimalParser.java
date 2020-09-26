package edu.varabei.artsiom.calculator.brain;

import lombok.Value;
import lombok.val;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

@Component
public class DecimalParser {

    public BigDecimal parse(String input) {
        input = input.replaceAll("[\\s_]", "");
        input = input.replaceAll(",", ".");
        return new BigDecimal(input);
    }

    public String parse(BigDecimal input) {
        return formattedIntegerPart(input) + fromattedDecimalPart(input);
    }

    String fromattedDecimalPart(BigDecimal decimal) {
        String str = decimal.toString();
        final int ind = str.indexOf('.');
        if (ind == -1) return "";
        return str. substring(ind)
                .replaceAll("0*$", "")
                .replaceAll("^.$", "");
    }

    String formattedIntegerPart(BigDecimal decimal) {
        StringBuilder result = new StringBuilder();
        BigInteger intPart = decimal.toBigInteger();
        while (intPart.compareTo(BigInteger.ZERO) > 0) {
            final BigInteger[] divideAndRemainder = intPart.divideAndRemainder(BigInteger.valueOf(1000));
            intPart = divideAndRemainder[0];
            BigInteger remainder = divideAndRemainder[1];
            result.insert(0, String.format("%03d ", divideAndRemainder[1].intValue()));
        }
        return result.toString().trim()
                .replaceAll("^0+", "")
                .replaceAll("^$", "0");
    }

    @Value
    static class Pair<S, T> {
        S left;
        S right;
    }

}
