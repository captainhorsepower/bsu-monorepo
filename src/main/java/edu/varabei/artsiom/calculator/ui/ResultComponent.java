package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public class ResultComponent extends JLabel implements UIElement{

    @Override
    public JComponent getDrawableComponent() {
        return this;
    }

    public void setResultText(String resultText) {
        setText(resultText);
    }

}
