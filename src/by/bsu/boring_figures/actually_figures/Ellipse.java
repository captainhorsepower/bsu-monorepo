package by.bsu.boring_figures.actually_figures;


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public class Ellipse extends Figure2D {

    private Point topLeft;
    private Point bottomRight;

    /**
     * theRECT
     *
     * @param topLeft
     * @param bottomRight
     */
    public Ellipse(Point topLeft, Point bottomRight) {
        super(Arrays.asList(topLeft, bottomRight));
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    /**
     * @param newCenter points
     */
    @Override
    public void move(Point newCenter) {
        int shiftX = newCenter.getX() - location().getX();
        int shiftY = newCenter.getY() - location().getY();
        topLeft.setX(topLeft.getX() + shiftX);
        topLeft.setY(topLeft.getY() + shiftY);
        bottomRight.setX(bottomRight.getX() + shiftX);
        bottomRight.setY(bottomRight.getY() + shiftY);
        initCenter(Arrays.asList(topLeft, bottomRight));
    }

    @Override
    protected Shape getShape() {
        int w = bottomRight.getX() - topLeft.getX();
        int h = bottomRight.getY() - topLeft.getY();
        return new Ellipse2D.Float(topLeft.getX(), topLeft.getY(), w, h);
    }
}