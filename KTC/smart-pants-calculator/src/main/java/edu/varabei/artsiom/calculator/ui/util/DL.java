package edu.varabei.artsiom.calculator.ui.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface DL extends DocumentListener {

    void heh(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        heh(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        heh(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        heh(e);
    }
}
