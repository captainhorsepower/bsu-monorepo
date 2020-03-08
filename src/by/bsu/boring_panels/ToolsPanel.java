package by.bsu.boring_panels;

import by.bsu.boring_components.ColorChooserButton;

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
            "Rectangle",
            "Polyline",
            "Regular polygon",
            "Rhombus"};

    public ToolsPanel() {
        this.setLayout(new GridLayout(12, 1));
        this.setSize(50, 200);

        Label label = new Label("Figures");
        add(label);

        JComboBox<String> figureList = new JComboBox<>(figures);
        add(figureList);

        label = new Label("Border color");
        add(label);

        ColorChooserButton colorChooser = new ColorChooserButton(Color.WHITE);
        colorChooser.addColorChangedListener(newColor -> {
        });
        add(colorChooser);

        label = new Label("Fill color");
        add(label);

        colorChooser = new ColorChooserButton(Color.WHITE);
        colorChooser.addColorChangedListener(newColor -> {
        });
        add(colorChooser);
    }

}
