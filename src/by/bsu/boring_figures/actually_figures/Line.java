package by.bsu.boring_figures.actually_figures;


import java.awt.*;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Line extends Ray {

    public Line(Point firstPoint, Point secondPoint) {
        super(firstPoint, secondPoint);
    }

    public void draw(Graphics2D graphics2D) {
        int scaleX = (getSecondPoint().getX() - getFirstPoint().getX()) * MAGIC_SCALE_FACTOR;
        int scaleY = (getSecondPoint().getY() - getFirstPoint().getY()) * MAGIC_SCALE_FACTOR;
        graphics2D.drawLine(
                getFirstPoint().getX() - scaleX, getFirstPoint().getY() - scaleY,
                getFirstPoint().getX() + scaleX, getFirstPoint().getY() + scaleY
        );
    }

}