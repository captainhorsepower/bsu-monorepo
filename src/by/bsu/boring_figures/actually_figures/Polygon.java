package by.bsu.boring_figures.actually_figures;


import java.awt.*;
import java.util.List;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Polygon extends Figure2D {

    private List<Point> points;

    public Polygon(List<Point> points) {
        super(points);
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int[] xPoints = points.stream().mapToInt(Point::getX).toArray();
        int[] yPoints = points.stream().mapToInt(Point::getY).toArray();
        java.awt.Polygon p = new java.awt.Polygon(xPoints, yPoints, points.size());
        graphics2D.setColor(getBorderColor());
        graphics2D.drawPolygon(p);
        graphics2D.setColor(getFillColor());
        graphics2D.fillPolygon(p);
    }

    /**
     * @param newLocation newCenter
     */
    @Override
    public void move(Point newLocation) {
        // FIXME: 3/9/20
    }

}