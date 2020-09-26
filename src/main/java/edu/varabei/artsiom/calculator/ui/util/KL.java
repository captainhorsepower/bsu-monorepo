package edu.varabei.artsiom.calculator.ui.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface KL extends KeyListener {
    @Override
    default void keyPressed(KeyEvent e) {}

    @Override
    default void keyReleased(KeyEvent e) {}
}
