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
    UIElement roundingMode(LayoutConfigProperties layoutConfig) {
        final ButtonGroup buttonGroup = new ButtonGroup();
        final JRadioButton math = new JRadioButton("math");
        final JRadioButton bank = new JRadioButton("bank");
        final JRadioButton down = new JRadioButton("down");

        buttonGroup.add(math);
        buttonGroup.add(bank);
        buttonGroup.add(down);

        bank.setSelected(true);

        val panel = new JPanel();
        val h = 110;
        val w = 60;
        panel.setBounds(300, 95, w, h);

        panel.add(math);
        panel.add(bank);
        panel.add(down);
        return () -> panel;
    }

    @Bean
    ResultComponent resultLabel() {
        return new ResultComponent(layoutConfig.getResult());
    }


}
