package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public class ResultComponent extends JLabel {

    public void setResultText(String resultText) {
        setText(resultText);
    }

}
