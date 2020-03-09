package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.actually_figures.Polyline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.event.InputEvent.ALT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;


public class DrawPanel extends JPanel {

    private List<Figure> figures;
    private Figure selected;

    public DrawPanel() {
        super(true);
        this.figures = new ArrayList<>();

        //TODO 3/9/20: remove cuz it's just for debug and fun purposes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiers() & CTRL_MASK) != 0 || SwingUtilities.isRightMouseButton(e)) {
                    Figure d = new Polyline(Arrays.asList(
                            new Point(e.getX() - 50, e.getY() - 50),
                            new Point(e.getX() + 50, e.getY() + 50),
                            new Point(e.getX() + 150, e.getY() + 250),
                            new Point(e.getX() + 250, e.getY() + 150)
                    ));
                    addFigure(d);
                    return;
                }


                if (selected != null && (e.getModifiers() & ALT_MASK) != 0) {
                    System.out.println("move " + selected.getClass().getSimpleName());
                    selected.move(new Point(e.getX(), e.getY()));
                    repaint();
                    return;
                }

                Point p = new Point(e.getX(), e.getY());
                setSelected(figures.stream()
                        .filter(f -> f.contains(p))
                        .findFirst()
                        .orElse(null));
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if ((e.getModifiers() & ALT_MASK) != 0) {
                    DrawPanel.this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    DrawPanel.this.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    public void addFigure(Figure figure) {
        this.figures.add(figure);
        setSelected(figure);
        repaint();
    }

    public void setSelected(Figure figure) {
        if (figure != this.selected) {
            if (this.selected != null) this.selected.deselect();
            if (figure == null || !figures.contains(figure)) {
                this.selected = null;
            } else {
                this.selected = figure;
                this.selected.select();
            }
            repaint();
        }
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
