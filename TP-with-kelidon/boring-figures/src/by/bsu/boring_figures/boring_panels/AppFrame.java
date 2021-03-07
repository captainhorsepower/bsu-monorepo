package by.bsu.boring_figures.boring_panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AppFrame extends JFrame{

    public AppFrame(String title) throws HeadlessException {
        super(title);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(850, 500));
        panel.add(drawPanel, BorderLayout.LINE_START);

        JPanel tools = new ToolsPanel();
        tools.setPreferredSize(new Dimension(100, 500));
        panel.add(tools, BorderLayout.LINE_END);

        this.setPreferredSize(new Dimension(1000, 500));
        this.setContentPane(panel);

        this.pack();
        this.setVisible(true);
    }

}
