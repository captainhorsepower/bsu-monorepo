package by.bsu.boring_figures;

import by.bsu.boring_panels.ToolsPanel;

import javax.swing.*;
import java.awt.*;


public class BoringFigures {
    public static void main(String[] args) {
        System.out.println("Hello World");

        var frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var toolsPanel = new ToolsPanel();
        frame.setPreferredSize(new Dimension(600,600));
        frame.pack();
        frame.add(toolsPanel, BorderLayout.LINE_START);
        frame.setVisible(true);
    }
}