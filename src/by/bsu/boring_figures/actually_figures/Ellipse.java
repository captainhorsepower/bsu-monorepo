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

    @Override
    public void draw(Graphics2D graphics2D) {
        int w = bottomRight.getX() - topLeft.getX();
        int h = bottomRight.getY() - topLeft.getY();
        Ellipse2D ellipse = new Ellipse2D.Float(topLeft.getX(), topLeft.getY(), w, h);
        graphics2D.setColor(getBorderColor());
        graphics2D.draw(ellipse);
        graphics2D.setColor(getFillColor());
        graphics2D.fill(ellipse);
    }

    /**
     * @param newCenter points
     */
    @Override
    public void move(Point newCenter) {
        // FIXME: 3/9/20
    }

}