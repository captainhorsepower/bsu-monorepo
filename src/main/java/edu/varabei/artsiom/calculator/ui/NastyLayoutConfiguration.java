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
    public NumberInputTextField leftNumField() {
        return new NumberInputTextField(layoutConfig.getLeftNumber(), decimalParser);
    }

    @Bean
    public NumberInputTextField rightNumField() {
        return new NumberInputTextField(layoutConfig.getRightNumber(), decimalParser);
    }

    @Bean
    UIElement addButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getAddButton())
                .operation(calculator::add)
                .leftNum(leftNumField()::getText)
                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    UIElement substrButton() {
        return ArithmeticButton.builder()
                .props(layoutConfig.getSubButton())
                .operation(calculator::sub)
                .leftNum(leftNumField()::getText)
                .rightNum(rightNumField()::getText)
                .resultConsumer(resultLabel()::setResultText)
                .build();
    }

    @Bean
    ResultComponent resultLabel() {
        return new ResultComponent(layoutConfig.getResult());
    }


}
