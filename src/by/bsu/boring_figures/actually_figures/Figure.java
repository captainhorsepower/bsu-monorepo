package by.bsu.boring_figures.actually_figures;


import by.bsu.boring_figures.boring_panels.ToolsPanel;

import java.awt.*;
import java.util.List;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public abstract class Figure implements Drawable, Selectable {

    private Point center;
    private Color fillColor = Color.BLACK;
    private boolean selected;

    /**
     * @param points center is mean of given points
     */
    public Figure(List<Point> points) {
        initCenter(points);
    }

    protected void initCenter(List<Point> points) {
        this.center = points.stream()
                .reduce((l, r) -> new Point(
                        l.getX() + r.getX(),
                        l.getY() + r.getY()))
                .map(p -> new Point(
                        p.getX() / points.size(),
                        p.getY() / points.size()))
                .get();
        this.borderColor = ToolsPanel.borderColorChooser.getSelectedColor();
    }

    public Point getCenter() {
        return this.center;
    }

    public Color getBorderColor() {
        return new Color(
                1f * fillColor.getRed() / 255,
                1f * fillColor.getGreen() / 255,
                1f * fillColor.getBlue() / 255,
                selected ? 1.0f : 0.7f
        );
    }

    public void setBorderColor(Color borderColor) {
        this.fillColor = borderColor;
    }

    /**
     * calls getCenter
     */
    public Point location() {
        return this.center;
    }

    @Override
    public void select() {
        this.selected = true;
    }

    @Override
    public void deselect() {
        this.selected = false;
    }

    protected boolean isSelected() {
        return selected;
    }

    protected void setSelected(boolean selected) {
        this.selected = selected;
    }


}