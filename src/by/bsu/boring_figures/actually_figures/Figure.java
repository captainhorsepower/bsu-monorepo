package by.bsu.boring_figures.actually_figures;


import java.awt.*;
import java.util.List;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public abstract class Figure implements Drawable {

    private Point center;
    private Color borderColor = Color.BLACK;

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
    }

    public Point getCenter() {
        return this.center;
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * calls getCenter
     */
    public Point location() {
        return this.center;
    }

}