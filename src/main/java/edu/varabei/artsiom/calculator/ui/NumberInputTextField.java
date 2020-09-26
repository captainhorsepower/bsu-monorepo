package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.ui.util.KL;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@Log4j2
@RequiredArgsConstructor
public class NumberInputTextField extends JTextField implements UIElement {

    @PostConstruct
    public void configure() {
        this.addKeyListener((KL) this::keyTyped);
    }

    @Override
    public JComponent getDrawableComponent() {
        return this;
    }

    //TODO 9/26/20: extract it somewhere
    private void keyTyped(KeyEvent keyEvent) {
        NumberInputTextField textField = NumberInputTextField.this;
        final String textBefore = textField.getText();
        final int pos = textField.getCaretPosition();
        String textAfter = textBefore.substring(0, pos)
                           + keyEvent.getKeyChar()
                           + textBefore.substring(pos);
        if (keyEvent.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            textAfter = textBefore.substring(0, pos) + textBefore.substring(pos);
            log.info("[text field] backspace: {} -> {}", textBefore, textAfter);
        } else if (keyEvent.getExtendedKeyCode() == KeyEvent.VK_DELETE) {
            textAfter = textBefore.substring(0, pos + 1) + textBefore.substring(pos + 1);
            log.info("[text field] delete: {} -> {}", textBefore, textAfter);
        } else if (!("" + keyEvent.getKeyChar()).matches("[0-9\\-+.,]")) {
            keyEvent.consume();
            return;
        }

        boolean isNumber = textAfter.matches("[-+]?\\d*([.,]\\d+)?");
        log.info("[text field] {} [{}] isNumber: {}", textAfter, keyEvent.getKeyChar(), isNumber);
        textField.setBackground(isNumber ? Color.GREEN : Color.RED);
    }
}
