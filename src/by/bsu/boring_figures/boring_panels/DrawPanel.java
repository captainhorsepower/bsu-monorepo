package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.actually_figures.RegularPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.awt.event.InputEvent.CTRL_MASK;


public class DrawPanel extends JPanel {

    private List<Figure> figures;

    public DrawPanel() {
        super(true);
        this.figures = new ArrayList<>();

        //TODO 3/9/20: remove cuz it's just for debug and fun purposes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiers() & CTRL_MASK) != 0 || SwingUtilities.isRightMouseButton(e)) {
                    Figure d = new RegularPolygon(
                            new Point(e.getX() - 50, e.getY() - 50),
                            new Point(e.getX() + 50, e.getY() + 50),
                            3 + new Random().nextInt(7)
                    );
                    addFigure(d);
                    return;
                }

                Point p = new Point(e.getX(), e.getY());
                List<Figure> fs = figures.stream()
                        .filter(f -> f.contains(p))
                        .collect(Collectors.toList());

                figures.removeAll(fs);
                repaint();

            }
        });
    }

    public void addFigure(Figure figure) {
        this.figures.add(figure);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        figures.forEach(drawable -> drawable.draw(g2d));
    }
}
