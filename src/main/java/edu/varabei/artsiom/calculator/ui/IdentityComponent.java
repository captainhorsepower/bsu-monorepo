package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.swing.*;

@RequiredArgsConstructor
public class IdentityComponent extends JPanel implements UIElement {

    private final LayoutConfigProperties.ComponentProperties props;

    @PostConstruct
    public void configure() {
        setLayout(null);
        setBounds(props.bounds());

        JLabel имяФамилия = new JLabel("Артём Воробей");
        имяФамилия.setBounds(0, 0, 200, 25);

        JLabel курсГруппаГод = new JLabel("4к 4гр, сентябрь 2020");
        курсГруппаГод.setBounds(0, 25, 200, 25);

        add(имяФамилия);
        add(курсГруппаГод);
    }

    @Override
    public JComponent getDrawableComponent() {
        return this;
    }
}
