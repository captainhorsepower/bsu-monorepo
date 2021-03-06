package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.Calculator;
import edu.varabei.artsiom.calculator.ui.util.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.function.Supplier;

@Log4j2
@RequiredArgsConstructor
public class ResultComponent implements UIElement {

    private final JPanel drawableComponent = new JPanel();
    private final JLabel resultValue = new JLabel();
    private final JLabel roundedValue = new JLabel();
    private final JButton button = new JButton();
    private final LayoutConfigProperties.ResultProperties props;
    private final Supplier<Tuple<String, String>> resultSupplier;

    @PostConstruct
    public void configure() {
        drawableComponent.setLayout(null);
        drawableComponent.setBounds(props.bounds());

        int width = 80;
        button.setText(props.getLabel());
        button.setBounds(0, 0, width, props.getHeight() / 2);
        button.addActionListener(e -> {
            try {
                setResultText(resultSupplier.get());
            } catch (Calculator.DecimalOverflowException excep) {
                log.info("bad input. Detail={}", excep.getMessage());
                JOptionPane.showMessageDialog(null,
                        excep.getMessage()
                        , "Overflow", JOptionPane.ERROR_MESSAGE);
            } catch (Exception excep) {
                log.info("bad input. Detail={}", excep.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Bad input values"
                        , "Bad input", JOptionPane.ERROR_MESSAGE);
            }
        });

        resultValue.setText(props.getInitValue());
        resultValue.setBounds(width, 0, props.getWidth() - width, props.getHeight() / 2);
        roundedValue.setText("rounded will be here...");
        roundedValue.setBounds(width, props.getHeight() / 2, props.getWidth() - width, props.getHeight() / 2);

        drawableComponent.add(button);
        drawableComponent.add(resultValue);
        drawableComponent.add(roundedValue);
    }

    @Override
    public JComponent getDrawableComponent() {
        return drawableComponent;
    }

    public void setResultText(Tuple<String, String> resultText) {
        resultValue.setText(resultText.getT1());
        roundedValue.setText(resultText.getT2());
    }

}
