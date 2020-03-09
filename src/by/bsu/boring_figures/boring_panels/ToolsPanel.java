package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.boring_components.ColorChooserButton;

import javax.swing.*;
import java.awt.*;


public class ToolsPanel extends JPanel {

    static final String[] figures = new String[]{
            "Ray",
            "Line",
            "Line segment",
            "Polyline",
            "Circle",
            "Ellipse",
            "Parallelogram",
            "Rectangle",
            "Polygon",
            "Regular polygon",
            "Rhombus"};

    static public JComboBox<String> figureList;
    static public ColorChooserButton borderColorChooser;
    static public ColorChooserButton fillColorChooser;

    public ToolsPanel() {
        this.setLayout(new GridLayout(12, 1));
        this.setSize(50, 200);

        Label label = new Label("Figures");
        add(label);

        figureList = new JComboBox<>(figures);
        add(figureList);

        label = new Label("Border color");
        add(label);

        borderColorChooser = new ColorChooserButton(Color.WHITE);
        borderColorChooser.addColorChangedListener(newColor -> {
        });
        add(borderColorChooser);

        label = new Label("Fill color");
        add(label);

        fillColorChooser = new ColorChooserButton(Color.WHITE);
        fillColorChooser.addColorChangedListener(newColor -> {
        });
        add(fillColorChooser);

        repaint();
    }

}
