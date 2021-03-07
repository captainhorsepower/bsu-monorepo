package edu.varabei.artsiom.calculator.brain;

import lombok.val;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class RoundingMachine {

    public static BigDecimal round(BigDecimal raw, int floatingPointPrecision, RoundingMode mode) {
        val integerPart = new BigDecimal(raw.longValue());
        val floatingPart = raw.subtract(integerPart);
        int prefixZeros = 0;
        if (floatingPart.toString().matches("0.[0]+[1-9]+")) {
            for (char c : floatingPart.toString().substring(2).toCharArray()) {
                if (c == '0')
                    prefixZeros++;
                else break;
            }
        }
        final int prec = floatingPointPrecision - prefixZeros;
        return prec == 0 ? integerPart : integerPart.add(
                floatingPart.round(new MathContext(prec, mode))
        );
    }

}
