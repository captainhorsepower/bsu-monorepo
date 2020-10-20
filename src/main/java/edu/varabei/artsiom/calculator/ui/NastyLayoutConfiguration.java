package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import edu.varabei.artsiom.calculator.brain.DecimalParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

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
    UIElement radioButtons(LayoutConfigProperties layoutConfig) {
        final ButtonGroup buttonGroup = new ButtonGroup();
        final JRadioButton add = new JRadioButton("+");
        final JRadioButton sub = new JRadioButton("-");
        final JRadioButton mult = new JRadioButton("*");
        final JRadioButton div = new JRadioButton("/");

        buttonGroup.add(add);
        buttonGroup.add(sub);
        buttonGroup.add(mult);
        buttonGroup.add(div);

        val panel = new JPanel();
        panel.add(add);
        panel.add(sub);
        panel.add(mult);
        panel.add(div);
        val inputs = layoutConfig.getNumberInputs();
        val h = 110;
        val w = 60;
        panel.setBounds(
                (inputs.get(1).getX() + inputs.get(1).getWidth() + inputs.get(2).getX()) / 2,
                inputs.get(1).getY() - h / 2,
                w, h
        );
        return () -> panel;
    }

    @Bean
    ResultComponent resultLabel() {
        return new ResultComponent(layoutConfig.getResult());
    }


}
