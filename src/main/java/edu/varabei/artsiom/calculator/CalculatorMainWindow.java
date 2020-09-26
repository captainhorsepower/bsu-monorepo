package edu.varabei.artsiom.calculator;

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

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Label Example");
//        JLabel l1, l2;
//        l1 = new JLabel("First Label.");
//        l1.setBounds(50, 50, 100, 30);
//        l2 = new JLabel("Second Label.");
//        l2.setBounds(50, 100, 100, 30);
//        frame.add(l1);
//        frame.add(l2);
//    }

}
