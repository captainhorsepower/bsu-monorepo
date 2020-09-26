package edu.varabei.artsiom.calculator.brain;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class DecimalParser {

    public BigDecimal parse(String input) {
        input = handleWhitespace(input.replaceAll("[\\-+]\\s?", ""));
        input = input.replaceAll(",", ".");
        return new BigDecimal(input);
    }

    private String handleWhitespace(String input) {
        final int spaceCount = StringUtils.countOccurrencesOf(input, " ");
        final List<String> splits = Arrays.stream(input.split(" "))
                .filter(arr -> arr.length() > 0)
                .collect(Collectors.toList());
        if (spaceCount != splits.size() - 1) {
            throw new RuntimeException("too much spaces");
        }
        // one split => 123.123123123 // short just last
        // two 12 123.123123123 // first and last
        // three or more 12 123 123.12312312312 // first, middle train, last
        switch (splits.size()) {
            case 1:
                onlyOneSplit(splits.get(0));
                break;
            case 2:
                checkFirst(splits.get(0));
                checkLast(splits.get(1));
                break;
            default:
                checkFirst(splits.get(0));
                checkMiddleTrain(splits);
                checkLast(splits.get(splits.size() - 1));
        }


        return input.replaceAll("[\\s_]", "");
    }

    void onlyOneSplit(String shortLast) {
        if (shortLast.split("\\.")[0].length() > 3) {
            throw new RuntimeException("last integer part invalid");
        }
    }

    void checkFirst(String first) {
        if (first.length() > 3 || first.length() < 1) {
            throw new RuntimeException("bad first split");
        }
    }

    void checkLast(String last) {
        if (last.split("\\.")[0].length() != 3) {
            throw new RuntimeException("last integer part invalid");
        }
    }

    void checkMiddleTrain(List<String> splits) {
        final long count = splits.stream()
                .skip(1).limit(splits.size() - 2)
                .filter(split -> split.length() == 3)
                .count();
        if (count != splits.size() - 2) {
            throw new RuntimeException("invalid middle train");
        }
    }

    public String parse(BigDecimal input) {
        return formattedIntegerPart(input) + fromattedDecimalPart(input);
    }

    String fromattedDecimalPart(BigDecimal decimal) {
        String str = decimal.toString();
        final int ind = str.indexOf('.');
        if (ind == -1) return "";
        return str.substring(ind)
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
