package by.bsu.boring_figures.actually_figures;


import java.awt.*;
import java.util.Arrays;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class LineSegment extends Figure1D {

    private Point start;
    private Point fini;

    /**
     * @param firstPoint
     * @param secondPoint secondPoint
     */
    public LineSegment(Point firstPoint, Point secondPoint) {
        super(Arrays.asList(firstPoint, secondPoint));
        this.start = firstPoint;
        this.fini = secondPoint;
    }

    public Point getStart() {
        return start;
    }

    public Point getSecondPoint() {
        return fini;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(getBorderColor());
        g2d.drawLine(
                start.getX(), start.getY(),
                fini.getX(), fini.getY()
        );
    }

    /**
     * @param newCenter points
     */
    @Override
    public void move(Point newCenter) {
        int shiftX = newCenter.getX() - location().getX();
        int shiftY = newCenter.getY() - location().getY();
        for (Point p : Arrays.asList(start, fini)) {
            p.setX(p.getX() + shiftX);
            p.setY(p.getY() + shiftY);
        }
        initCenter(Arrays.asList(start, fini));
    }

    @Override
    public boolean contains(Point point) {
        final int magicNumber = 5;
        Polygon pseudoLine = new Polygon(Arrays.asList(
                new Point(start.getX(), start.getY() - magicNumber),
                new Point(fini.getX(), fini.getY() - magicNumber),
                new Point(fini.getX(), fini.getY() + magicNumber),
                new Point(start.getX(), start.getY() + magicNumber)
        ));

        return pseudoLine.contains(point);
    }
}