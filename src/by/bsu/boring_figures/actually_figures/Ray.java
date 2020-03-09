package by.bsu.boring_figures.actually_figures;


import java.awt.*;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Ray extends LineSegment {

    protected static final int MAGIC_SCALE_FACTOR = 10_000;

    public Ray(Point firstPoint, Point secondPoint) {
        super(firstPoint, secondPoint);
    }

    public void draw(Graphics2D graphics2D) {
        int scaledX = getFirstPoint().getX() + (getSecondPoint().getX() - getFirstPoint().getX()) * MAGIC_SCALE_FACTOR;
        int scaledY = getFirstPoint().getY() + (getSecondPoint().getY() - getFirstPoint().getY()) * MAGIC_SCALE_FACTOR;
        graphics2D.drawLine(
                getFirstPoint().getX(), getFirstPoint().getY(),
                scaledX, scaledY
        );
    }

}