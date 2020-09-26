package edu.varabei.artsiom.calculator.ui;

import edu.varabei.artsiom.calculator.MainWindowConfig;
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

    private final MainWindowConfig config;
    private final List<JComponent> innerComponents;

    @PostConstruct
    public void applyConfig() {
        setTitle(config.getTitle());
        setSize(config.getWidth(), config.getHeight());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(null);
        innerComponents.forEach(this::add);

        setVisible(true);
    }

}
