package by.bsu.boring_figures.actually_figures;


import java.util.Arrays;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Rectangle extends Polygon {
    /**
     * @param topLeft
     * @param bottomRight points
     */
    public Rectangle(Point topLeft, Point bottomRight) {
        super(Arrays.asList(
                topLeft,
                new Point(bottomRight.getX(), topLeft.getY()),
                bottomRight,
                new Point(topLeft.getX(), bottomRight.getY())
        ));
    }

}