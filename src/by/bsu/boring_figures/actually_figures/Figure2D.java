package by.bsu.boring_figures.actually_figures;


import by.bsu.boring_figures.boring_components.ColorChangedListener;
import by.bsu.boring_figures.boring_panels.ToolsPanel;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public abstract class Figure2D extends Figure{

    private final Color fillColor;

    protected Figure2D(Color fillColor) {
        // FIXME: 3/9/20 
        super(null);
        this.fillColor = fillColor;
    }

    protected Figure2D(List<Point> points) {
        // FIXME: 3/9/20 
        super(points);
        this.fillColor = ToolsPanel.fillColorChooser.getSelectedColor();
    }

    public Color getFillColor() {
        return new Color(
                1f * fillColor.getRed() / 255,
                1f * fillColor.getGreen() / 255,
                1f * fillColor.getBlue() / 255,
                isSelected() ? 1.0f : 0.8f
        );
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(getBorderColor());
        graphics2D.draw(getShape());
        graphics2D.setColor(getFillColor());
        graphics2D.fill(getShape());
    }

    @Override
    public boolean contains(Point point) {
        return getShape().contains(point.getX(), point.getY());
    }

    protected abstract Shape getShape();
}