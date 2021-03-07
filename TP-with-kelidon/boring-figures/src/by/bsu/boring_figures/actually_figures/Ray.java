package by.bsu.boring_figures.actually_figures;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Ray extends LineSegment {

    protected static final int LARGEST_POSSIBLE_COORD = 8000;

    public Ray(Point firstPoint, Point secondPoint) {
        super(firstPoint, extrapolatedOutOfScreen(firstPoint, secondPoint));
    }

    protected static Point extrapolatedOutOfScreen(Point start, Point finish) {
        int shiftX = (finish.getX() - start.getX());
        int shiftY = (finish.getY() - start.getY());
        if (shiftX == 0 && shiftY == 0) {
            throw new RuntimeException("Two points with same coords don't form LineSegment");
        }
        int x, y;
        for (
                x = finish.getX(), y = finish.getY();
                x > 0 && y > 0 && x < LARGEST_POSSIBLE_COORD && y < LARGEST_POSSIBLE_COORD;
                x += shiftX, y += shiftY)
            ;
        return new Point(x, y);
    }

}