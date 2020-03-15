package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Polygon;
import by.bsu.boring_figures.actually_figures.Rectangle;
import by.bsu.boring_figures.actually_figures.*;
import by.bsu.boring_figures.boring_components.ColorChooserButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class ToolsPanel extends JPanel {

    static final Map<String, Class<?>> figuresBinding = new HashMap<>();
    static public JComboBox<String> figuresComboBox;
    static public ColorChooserButton borderColorChooser;
    static public ColorChooserButton fillColorChooser;
    static public JSpinner verticesSpinner;

    static {
        figuresBinding.put("Ray", Ray.class);
        figuresBinding.put("Line", Line.class);
        figuresBinding.put("Line segment", LineSegment.class);
        figuresBinding.put("Polyline", Polyline.class);
        figuresBinding.put("Circle", Circle.class);
        figuresBinding.put("Ellipse", Ellipse.class);
        figuresBinding.put("Parallelogram", Parallelogram.class);
        figuresBinding.put("Rectangle", Rectangle.class);
        figuresBinding.put("Polygon", Polygon.class);
        figuresBinding.put("Regular polygon", RegularPolygon.class);
        figuresBinding.put("Rhombus", Rhombus.class);
    }

    public ToolsPanel() {
        this.setLayout(new GridLayout(15, 1));
        this.setSize(50, 200);

        Label label = new Label("Interaction mode");
        add(label);

        JRadioButton draw = new JRadioButton("draw", true);
        JRadioButton move = new JRadioButton("move");

        ButtonGroup modesButtons = new ButtonGroup();
        modesButtons.add(draw);
        modesButtons.add(move);
        add(draw);
        add(move);


        label = new Label("Figures");
        add(label);

        figuresComboBox = new JComboBox<>();
        figuresBinding.forEach((e1, e2) -> figuresComboBox.addItem(e1));
        add(figuresComboBox);

        label = new Label("Border color");
        add(label);

        borderColorChooser = new ColorChooserButton(Color.WHITE);
        add(borderColorChooser);

        label = new Label("Fill color");
        add(label);

        fillColorChooser = new ColorChooserButton(Color.WHITE);
        fillColorChooser.addColorChangedListener(newColor -> {
        });
        add(fillColorChooser);

        label = new Label("Vertices number");
        add(label);

        verticesSpinner = new JSpinner();
        verticesSpinner.setModel(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));
        add(verticesSpinner);

        repaint();
    }

}
