package by.bsu.boring_figures;

import by.bsu.boring_panels.ToolsPanel;

import javax.swing.*;
import java.awt.*;


public class BoringFigures {
    public static void main(String[] args) {
        var frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.pack();
        var toolsPanel = new ToolsPanel();
        frame.add(toolsPanel, BorderLayout.LINE_START);
        frame.setVisible(true);
    }
}