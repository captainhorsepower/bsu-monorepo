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
        int a = Math.min(
                Math.abs(bottomRight.getX() - topLeft.getX()),
                Math.abs(bottomRight.getY() - topLeft.getY())
        );
        int w = bottomRight.getX() - topLeft.getX() > 0 ? a : -a;
        int h = bottomRight.getY() - topLeft.getY() > 0 ? a : -a;
        return new Point(topLeft.getX() + w, topLeft.getY() + h);
    }

    public int getRadius() {
        return (getBottomRight().getY() - getTopLeft().getY()) / 2;
    }

}