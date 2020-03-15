package by.bsu.boring_figures.actually_figures;


/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Line extends Ray {

    public Line(Point firstPoint, Point secondPoint) {
        super(extrapolatedOutOfScreen(secondPoint, firstPoint), extrapolatedOutOfScreen(firstPoint, secondPoint));
    }

}