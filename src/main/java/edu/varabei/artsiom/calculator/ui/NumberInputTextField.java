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
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;

@Log4j2
@RequiredArgsConstructor
public class NumberInputTextField implements UIElement {

    private final JTextField textField = new JTextField();
    private final LayoutConfigProperties.ComponentProperties props;
    private final DecimalParser parser;

    @PostConstruct
    public void configure() {
        textField.setBounds(props.bounds());
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
            && !("" + keyEvent.getKeyChar()).matches("[0-9\\-+.,\\s_]")) {
            log.trace("[key listener] suppressed key press");
            keyEvent.consume();
        }
    }

    @SneakyThrows
    private void onDocumentChange(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        String textAfter = document.getText(0, document.getLength());
        boolean isNumber = isNumber(textAfter);
        log.trace("[document listener] {} {} isNumber: {}", documentEvent.getType(), textAfter, isNumber);
        textField.setBackground(isNumber ? Color.GREEN : Color.RED);
    }

    private boolean isNumber(String text) {
        try {
            parser.parse(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
