package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import edu.varabei.artsiom.calculator.brain.CalculatorException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Log4j2
@Builder
public class ArithmeticButton implements UIElement {

    private final JButton button = new JButton();
    private final LayoutConfigProperties.ButtonProperties props;
    private final BiFunction<String, String, String> operation;
    private final Supplier<String> leftNum;
    private final Supplier<String> rightNum;
    private final Consumer<String> resultConsumer;

    @PostConstruct
    void configure() {
        button.setText(props.getTitle());
        button.setBounds(props.bounds());
        button.addActionListener(e -> {
            try {
                String result = operation.apply(leftNum.get(), rightNum.get());
                resultConsumer.accept(result);
            } catch (Exception excep) {
                JOptionPane.showMessageDialog(null,
                        "Bad input values", "Bad input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public JComponent getDrawableComponent() {
        return button;
    }


}
