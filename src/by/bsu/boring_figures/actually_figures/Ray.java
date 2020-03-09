package by.bsu.boring_figures.actually_figures;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Ray extends LineSegment {

    protected static final int MAGIC_SCALE_FACTOR = 8;

    public Ray(Point firstPoint, Point secondPoint) {
        super(firstPoint, secondPoint(firstPoint, secondPoint));
    }

    public static Point secondPoint(Point firstPoint, Point secondPoint) {
        int shiftX = (secondPoint.getX() - firstPoint.getX()) * MAGIC_SCALE_FACTOR;
        int shiftY = (secondPoint.getY() - firstPoint.getY()) * MAGIC_SCALE_FACTOR;
        return new Point(firstPoint.getX() + shiftX, firstPoint.getY() + shiftY);
    }

}