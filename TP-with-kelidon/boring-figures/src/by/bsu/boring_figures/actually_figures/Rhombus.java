package by.bsu.boring_figures.actually_figures;


/**
 * @author vshch
 * @version 1.0
 * @created 02-Mar-2020 3:52:18 PM
 */
public class Rhombus extends Parallelogram {

    public Rhombus(Point top, Point right) {
        super(top, right, bottom(top, right));
    }

    private static Point bottom(Point top, Point right) {
        int x = top.getX();
        int y = top.getY() + 2 * (right.getY() - top.getY());
        return new Point(x, y);
    }

}