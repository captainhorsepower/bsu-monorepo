package by.bsu.boring_figures.actually_figures;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Circle extends Ellipse {

    /**
     * smaller square (RECT)
     *
     * @param topLeft
     * @param bottomRight
     */
    public Circle(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight(topLeft, bottomRight));
    }

    private static Point bottomRight(Point topLeft, Point bottomRight) {
        int d = Math.min(
                bottomRight.getX() - topLeft.getX(),
                bottomRight.getY() - topLeft.getY()
        );
        return new Point(topLeft.getX() + d, topLeft.getY() + d);
    }

    public int getRadius() {
        return (getBottomRight().getY() - getTopLeft().getY()) / 2;
    }

}