package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class NastyLayoutConfiguration {

    private final Calculator calculator;
    private final LayoutConfigProperties layoutConfig;

    @Bean
    public NumberInputTextField inputTextField() {
        LayoutConfigProperties.ComponentProperties layoutProps = layoutConfig.getLeftNumber();
        NumberInputTextField textField = new NumberInputTextField();
        textField.setBounds(layoutProps.bounds());
        return textField;
    }

    @Bean
    public NumberInputTextField input2TextField() {
        LayoutConfigProperties.ComponentProperties layoutProps = layoutConfig.getRightNumber();
        NumberInputTextField textField = new NumberInputTextField();
        textField.setBounds(layoutProps.bounds());
        return textField;
    }

    @Bean
    UIElement addButton() {
        LayoutConfigProperties.ComponentProperties layoutProps = layoutConfig.getAddButton();
        JButton add = new JButton("add");
        add.setBounds(layoutProps.bounds());
        add.addActionListener(e -> {
            String left = inputTextField().getText();
            String right = input2TextField().getText();

            String result = calculator.add(left, right);
            log.info("[add] {} + {} = {}", left, right, result);

            resultLabel().setResultText(result);
        });
        return () -> add;
    }

    @Bean
    UIElement substrButton() {
        LayoutConfigProperties.ComponentProperties layoutProps = layoutConfig.getSubButton();
        JButton sub = new JButton("subtract");
        sub.setBounds(layoutProps.bounds());
        sub.addActionListener(e -> {
            String left = inputTextField().getText();
            String right = input2TextField().getText();

            String result = calculator.sub(left, right);
            log.info("[sub] {} - {} = {}", left, right, result);

            resultLabel().setResultText(result);
        });
        return () -> sub;
    }

    @Bean
    ResultComponent resultLabel() {
        LayoutConfigProperties.ComponentProperties layoutProps = layoutConfig.getResult();
        ResultComponent result = new ResultComponent();
        result.setBounds(layoutProps.bounds());
        return result;
    }


}
