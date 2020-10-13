package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import edu.varabei.artsiom.calculator.brain.DecimalParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class NastyLayoutConfiguration {

    private final Calculator calculator;
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
    UIElement addButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getAddButton())
                .operation(calculator::add)
//                .leftNum(leftNumField()::getText)
//                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    UIElement substrButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getSubButton())
                .operation(calculator::sub)
//                .leftNum(leftNumField()::getText)
//                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    UIElement multButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getMultButton())
                .operation(calculator::mult)
//                .leftNum(leftNumField()::getText)
//                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    UIElement divButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getDivButton())
                .operation(calculator::div)
//                .leftNum(leftNumField()::getText)
//                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    ResultComponent resultLabel() {
        return new ResultComponent(layoutConfig.getResult());
    }


}
