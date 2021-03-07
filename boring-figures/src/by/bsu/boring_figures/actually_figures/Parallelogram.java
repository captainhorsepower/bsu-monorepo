package by.bsu.boring_figures.actually_figures;


import java.util.Arrays;
import java.util.List;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Parallelogram extends Polygon {

    /**
     * @param firstPoint
     * @param secondPoint
     * @param thirdPoint
     */
    public Parallelogram(Point firstPoint, Point secondPoint, Point thirdPoint) {
        super(generateParallelogram(firstPoint, secondPoint, thirdPoint));
    }

    private static List<Point> generateParallelogram(Point first, Point second, Point third) {
        int x = third.getX() + first.getX() - second.getX();
        int y = first.getY() + third.getY() - second.getY();
        Point fourth = new Point(x, y);
        return Arrays.asList(first, second, third, fourth);
    }
}