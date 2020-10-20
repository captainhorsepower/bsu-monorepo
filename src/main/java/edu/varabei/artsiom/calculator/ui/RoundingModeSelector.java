package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class RoundingModeSelector implements UIElement {
    final ButtonGroup buttonGroup = new ButtonGroup();
    final JRadioButton math = new JRadioButton("math");
    final JRadioButton bank = new JRadioButton("bank");
    final JRadioButton down = new JRadioButton("down");
    final JComponent drawableComponent = new JPanel();

    @PostConstruct
    void init() {
        buttonGroup.add(math);
        buttonGroup.add(bank);
        buttonGroup.add(down);

        bank.setSelected(true);

        val h = 110;
        val w = 60;
        drawableComponent.setBounds(300, 95, w, h);

        drawableComponent.add(math);
        drawableComponent.add(bank);
        drawableComponent.add(down);
    }

    @Override
    public JComponent getDrawableComponent() {
        return drawableComponent;
    }

    public RoundingMode getSelected() {
        if (math.isSelected()) return RoundingMode.HALF_UP;
        if (bank.isSelected()) return RoundingMode.HALF_EVEN;
        return RoundingMode.DOWN;
    }

}
