package by.bsu.boring_figures.actually_figures;


import java.awt.*;

/**
 * @author shchors_vs
 * @version 1.0
 * @created 02-Mar-2020 3:52:17 PM
 */
public interface Drawable {

    void draw(Graphics2D graphics2D);

    Point location();

    /**
     * @param newLocation points
     */
    void move(Point newLocation);

}