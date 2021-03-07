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
    private Point finish;

    /**
     * @param firstPoint
     * @param secondPoint secondPoint
     */
    public LineSegment(Point firstPoint, Point secondPoint) {
        super(Arrays.asList(firstPoint, secondPoint));
        this.start = firstPoint;
        this.finish = secondPoint;
    }

    public Point getStart() {
        return start;
    }

    public Point getSecondPoint() {
        return finish;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(getBorderColor());
        g2d.drawLine(
                start.getX(), start.getY(),
                finish.getX(), finish.getY()
        );
    }

    /**
     * @param newCenter points
     */
    @Override
    public void move(Point newCenter) {
        int shiftX = newCenter.getX() - location().getX();
        int shiftY = newCenter.getY() - location().getY();
        for (Point p : Arrays.asList(start, finish)) {
            p.setX(p.getX() + shiftX);
            p.setY(p.getY() + shiftY);
        }
        initCenter(Arrays.asList(start, finish));
    }

    @Override
    public boolean contains(Point point) {
        final int magicNumber = 5;
        Polygon pseudoLine = new Polygon(Arrays.asList(
                new Point(start.getX() - magicNumber, start.getY() - magicNumber),
                new Point(start.getX() + magicNumber, start.getY() + magicNumber),
                new Point(finish.getX() + magicNumber, finish.getY() + magicNumber),
                new Point(finish.getX() - magicNumber, finish.getY() - magicNumber)
        ));

        return pseudoLine.contains(point);
    }
}