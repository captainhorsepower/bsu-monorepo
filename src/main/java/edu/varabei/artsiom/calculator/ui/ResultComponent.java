package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.swing.*;

@RequiredArgsConstructor
public class ResultComponent implements UIElement {

    private final JLabel label = new JLabel();
    private final LayoutConfigProperties.ComponentProperties props;

    @PostConstruct
    public void configure() {
        label.setBounds(props.bounds());
    }

    @Override
    public JComponent getDrawableComponent() {
        return label;
    }

    public void setResultText(String resultText) {
        label.setText(resultText);
    }

}
