package by.bsu.boring_figures.actually_figures;


import java.util.List;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public abstract class Figure1D extends Figure {
    public Figure1D(List<Point> points) {
        super(points);
    }

    @Override
    public boolean contains(Point point) {
        return false;
    }
}