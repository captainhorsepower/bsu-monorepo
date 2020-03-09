package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Drawable;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.actually_figures.RegularPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawPanel extends JPanel {

    private List<Drawable> figures;

    public DrawPanel() {
        super(true);
        this.figures = new ArrayList<>();

        //TODO 3/9/20: remove cuz it's just for debug and fun purposes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e);
                Drawable d = new RegularPolygon(
                        new Point(e.getX() - 50, e.getY() - 50),
                        new Point(e.getX() + 50, e.getY() + 50),
                        3 + new Random().nextInt(7)
                );
                addFigure(d);
            }
        });
    }

    public void addFigure(Drawable figure) {
        this.figures.add(figure);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        figures.forEach(drawable -> drawable.draw((Graphics2D) g));
    }
}
