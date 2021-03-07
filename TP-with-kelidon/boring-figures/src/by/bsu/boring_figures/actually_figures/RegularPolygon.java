package by.bsu.boring_figures.actually_figures;


import java.util.ArrayList;
import java.util.List;

/**
 * @author vshch
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class RegularPolygon extends Polygon {

    /**
     * @param topLeft
     * @param bottomRight
     * @param pointsNumber
     */
    public RegularPolygon(Point topLeft, Point bottomRight, int pointsNumber) {
        super(vertices(topLeft, bottomRight, pointsNumber));
    }

    private static List<Point> vertices(Point topLeft, Point bottomRight, int n) {
        Circle circle = new Circle(topLeft, bottomRight);
        Point center = circle.getCenter();
        int radius = circle.getRadius();

        List<Point> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double deg = Math.PI * 2 * i / n;
            vertices.add(new Point(
                    (int) (center.getX() + radius * Math.sin(deg)),
                    (int) (center.getY() + radius * Math.cos(Math.PI + deg))
            ));
        }

        return vertices;
    }

}