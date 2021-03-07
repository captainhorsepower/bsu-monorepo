package edu.varabei.artsiom.calculator.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class CalculatorMainWindow extends JFrame {

    private final MainWindowProperties config;
    private final List<UIElement> innerComponents;

    @PostConstruct
    public void applyConfig() {
        setTitle(config.getTitle());
        setSize(config.getWidth(), config.getHeight());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(null);
        innerComponents.stream()
                .map(UIElement::getDrawableComponent)
                .forEach(this::add);

        setVisible(true);
    }

}
