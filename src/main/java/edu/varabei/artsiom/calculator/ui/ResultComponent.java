package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.swing.*;

@RequiredArgsConstructor
public class ResultComponent implements UIElement {

    private final JPanel drawableComponent = new JPanel();
    private final JLabel resultValue = new JLabel();
    private final LayoutConfigProperties.ResultProperties props;

    @PostConstruct
    public void configure() {
        drawableComponent.setLayout(null);
        drawableComponent.setBounds(props.bounds());

        JLabel resultLabel = new JLabel(props.getLabel());
        resultLabel.setBounds(0,0, 50, 25);

        resultValue.setText(props.getInitValue());
        resultValue.setBounds(50, 0, 300, 25);

        drawableComponent.add(resultLabel);
        drawableComponent.add(resultValue);
    }

    @Override
    public JComponent getDrawableComponent() {
        return drawableComponent;
    }

    public void setResultText(String resultText) {
        resultValue.setText(resultText);
    }

}
