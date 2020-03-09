package by.bsu.boring_figures.actually_figures;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.Arrays;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class LineSegment extends Figure1D {

    private Point firstPoint;
    private Point secondPoint;

    /**
     * @param firstPoint
     * @param secondPoint secondPoint
     */
    public LineSegment(Point firstPoint, Point secondPoint) {
        super(Arrays.asList(firstPoint, secondPoint));
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(getBorderColor());
        g2d.drawLine(
                firstPoint.getX(), firstPoint.getY(),
                secondPoint.getX(), secondPoint.getY()
        );
    }

    /**
     * @param newCenter points
     */
    @Override
    public void move(Point newCenter) {
        // FIXME: 3/9/20
        throw new NotImplementedException();
    }

    @Override
    public boolean contains(Point point) {
        final int magicNumber = 5;
        Polygon pseudoLine = new Polygon(Arrays.asList(
                new Point(firstPoint.getX(), firstPoint.getY() - magicNumber),
                new Point(secondPoint.getX(), secondPoint.getY() - magicNumber),
                new Point(secondPoint.getX(), secondPoint.getY() + magicNumber),
                new Point(firstPoint.getX(), firstPoint.getY() + magicNumber)
        ));

        return pseudoLine.contains(point);
    }
}