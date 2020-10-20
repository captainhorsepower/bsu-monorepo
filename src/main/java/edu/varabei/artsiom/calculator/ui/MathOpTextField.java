package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.DecimalParser;
import edu.varabei.artsiom.calculator.ui.util.DL;
import edu.varabei.artsiom.calculator.ui.util.KL;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;

@Log4j2
@RequiredArgsConstructor
public class MathOpTextField implements UIElement {

    private final JTextField textField = new JTextField();
    private final LayoutConfigProperties.ComponentProperties props;

    @PostConstruct
    public void configure() {
        textField.setBounds(props.bounds());

        JLabel label = new JLabel(props.getLabel());
        label.setBounds(0, 0, 50, props.getHeight());

        textField.setText("+");

        textField.addKeyListener((KL) this::keyTyped);
        textField.getDocument().addDocumentListener((DL) this::onDocumentChange);
    }

    @Override
    public JComponent getDrawableComponent() {
        return textField;
    }

    public String getText() {
        return textField.getText();
    }

    private void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getExtendedKeyCode() != KeyEvent.VK_BACK_SPACE
            && keyEvent.getExtendedKeyCode() != KeyEvent.VK_DELETE
            && (textField.getText().length() >= 1)) {
            log.trace("[key listener] suppressed key press");
            keyEvent.consume();
        }
    }

    @SneakyThrows
    private void onDocumentChange(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        String textAfter = document.getText(0, document.getLength());
        String math = "+-*/";
        boolean isNumber = !textAfter.isEmpty() && math.contains(textAfter.trim());
        textField.setBackground(isNumber ? Color.GREEN : Color.RED);
    }

}
