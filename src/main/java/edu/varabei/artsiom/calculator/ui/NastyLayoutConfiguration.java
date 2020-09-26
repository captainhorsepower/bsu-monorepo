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

    @Bean
    public NumberInputTextField inputTextField() {
        NumberInputTextField textField = new NumberInputTextField();
        textField.setBounds(50, 50, 100, 25);
        return textField;
    }

    @Bean
    public NumberInputTextField input2TextField() {
        NumberInputTextField textField = new NumberInputTextField();
        textField.setBounds(50, 100, 100, 25);
        return textField;
    }

    @Bean
    JComponent addButton() {
        JButton add = new JButton("add");
        add.setBounds(200, 100, 60, 25);
        add.addActionListener(e -> {
            String left = inputTextField().getText();
            String right = input2TextField().getText();

            String result = calculator.add(left, right);
            log.info("[add] {} + {} = {}", left, right, result);

            resultLabel().setResultText(result);
        });
        return add;
    }

    @Bean
    JComponent substrButton() {
        JButton sub = new JButton("subtract");
        sub.setBounds(200, 100, 60, 25);
        sub.addActionListener(e -> {
            String left = inputTextField().getText();
            String right = input2TextField().getText();

            String result = calculator.sub(left, right);
            log.info("[sub] {} - {} = {}", left, right, result);

            resultLabel().setResultText(result);
        });
        return sub;
    }

    @Bean
    ResultComponent resultLabel() {
        ResultComponent result = new ResultComponent();
        result.setBounds(200, 250, 250, 25);
        return result;
    }


}
