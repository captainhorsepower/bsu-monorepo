package by.bsu.boring_figures;

import by.bsu.boring_figures.boring_panels.DrawPanel;

import javax.swing.*;
import java.awt.*;

public class BoringFigures {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Boring Figures");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new DrawPanel();
        panel.setPreferredSize(new Dimension(500, 500));

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

    }
}