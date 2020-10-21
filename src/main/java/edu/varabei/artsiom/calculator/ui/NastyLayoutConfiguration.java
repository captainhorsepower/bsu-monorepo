package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import edu.varabei.artsiom.calculator.brain.DecimalParser;
import edu.varabei.artsiom.calculator.brain.ExprToNotation;
import edu.varabei.artsiom.calculator.ui.util.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.MathContext;
import java.util.Arrays;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class NastyLayoutConfiguration {

    private final Calculator calculator;
    private final ExprToNotation exprToNotation;
    private final LayoutConfigProperties layoutConfig;
    private final DecimalParser decimalParser;

    @Bean
    public UIElement identity() {
        return new IdentityComponent(layoutConfig.getIdentity());
    }

    @Bean
    public NumberInputTextField firstNumField() {
        return new NumberInputTextField(layoutConfig.getNumberInputs().get(0), decimalParser);
    }

    @Bean
    public NumberInputTextField secondNumField() {
        return new NumberInputTextField(layoutConfig.getNumberInputs().get(1), decimalParser);
    }

    @Bean
    NumberInputTextField thirdNumField() {
        return new NumberInputTextField(layoutConfig.getNumberInputs().get(2), decimalParser);
    }

    @Bean
    NumberInputTextField fourthNumField() {
        return new NumberInputTextField(layoutConfig.getNumberInputs().get(3), decimalParser);
    }

    @Bean
    MathOpTextField firstOp() {
        return new MathOpTextField(layoutConfig.getMathOps().get(0));
    }

    @Bean
    MathOpTextField secondOp() {
        return new MathOpTextField(layoutConfig.getMathOps().get(1));
    }

    @Bean
    MathOpTextField thirdOp() {
        return new MathOpTextField(layoutConfig.getMathOps().get(2));
    }

    @Bean
    RoundingModeSelector roundingMode(LayoutConfigProperties layoutConfig) {
        return new RoundingModeSelector();
    }

    @Bean
    ResultComponent resultLabel(RoundingModeSelector rm) {
        return new ResultComponent(layoutConfig.getResult(), () -> {
            val expr = String.join(" ",
                    Arrays.asList(
                            decimalParser.parse(firstNumField().getText()).toString(),
                            firstOp().getText(), "(",
                            decimalParser.parse(secondNumField().getText()).toString(),
                            secondOp().getText(),
                            decimalParser.parse(thirdNumField().getText()).toString(),
                            ")", thirdOp().getText(),
                            decimalParser.parse(fourthNumField().getText()).toString()
                    ));
            val postfixNotation = exprToNotation.toPostfixNotation(expr);
            val res = calculator.calc(postfixNotation);

            val roundingMode = rm.getSelected();
            return Tuple.of(
                    decimalParser.parse(res.round(new MathContext(6, roundingMode))),
                    decimalParser.parse(res.setScale(0, roundingMode))
            );
        });
    }

}
