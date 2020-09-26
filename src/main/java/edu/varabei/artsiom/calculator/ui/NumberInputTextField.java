package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.brain.DecimalParser;
import edu.varabei.artsiom.calculator.ui.util.DL;
import edu.varabei.artsiom.calculator.ui.util.KL;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;

@Log4j2
@RequiredArgsConstructor
public class NumberInputTextField implements UIElement {

    private final JPanel drawableComponent = new JPanel();
    private final JTextField textField = new JTextField();
    private final LayoutConfigProperties.TextFieldProperties props;
    private final DecimalParser parser;

    @PostConstruct
    public void configure() {
        drawableComponent.setLayout(null);
        drawableComponent.setBounds(props.bounds());

        JLabel label = new JLabel(props.getLabel());
        label.setBounds(0, 0, 50, props.getHeight());

        textField.setBounds(50, 0, props.getWidth() - 50, props.getHeight());
        textField.addKeyListener((KL) this::keyTyped);
        textField.getDocument().addDocumentListener((DL) this::onDocumentChange);

        drawableComponent.add(label);
        drawableComponent.add(textField);
    }

    @Override
    public JComponent getDrawableComponent() {
        return drawableComponent;
    }

    public String getText() {
        return textField.getText();
    }

    private void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getExtendedKeyCode() != KeyEvent.VK_BACK_SPACE
            && keyEvent.getExtendedKeyCode() != KeyEvent.VK_DELETE
            && (textField.getText()
                        .replaceAll("(?![0-9.,])+", "")
                        .length() >= props.getMaxLen()
                || !("" + keyEvent.getKeyChar()).matches("[0-9\\-+.,\\s_]"))) {
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
