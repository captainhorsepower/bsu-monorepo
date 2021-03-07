package by.bsu.boring_figures.actually_figures;


import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * @param newLocation newCenter
     */
    @Override
    public void move(Point newLocation) {
        int shiftX = newLocation.getX() - location().getX();
        int shiftY = newLocation.getY() - location().getY();
        this.points = this.getPoints().stream()
                .map(p -> new Point(p.getX() + shiftX, p.getY() + shiftY))
                .collect(Collectors.toList());

        initCenter(points);
    }

    @Override
    protected Shape getShape() {
        int[] xPoints = points.stream().mapToInt(Point::getX).toArray();
        int[] yPoints = points.stream().mapToInt(Point::getY).toArray();
        return new java.awt.Polygon(xPoints, yPoints, points.size());
    }
}