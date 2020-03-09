package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.*;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.actually_figures.Polyline;
import by.bsu.boring_figures.actually_figures.Polygon;
import by.bsu.boring_figures.actually_figures.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.event.InputEvent.ALT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;


import static java.awt.event.ActionEvent.SHIFT_MASK;

public class DrawPanel extends JPanel {

    private List<Figure> figures;
    private Figure selected;
    private List<Point> points;

    public DrawPanel() {
        super(true);
        this.figures = new ArrayList<>();

        //TODO 3/9/20: remove cuz it's just for debug and fun purposes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (selected != null && (e.getModifiers() & ALT_MASK) != 0) {
                    System.out.println("move " + selected.getClass().getSimpleName());
                    selected.move(new Point(e.getX(), e.getY()));
                    repaint();
                    return;
                }

                if ((e.getModifiers() & SHIFT_MASK) != 0) {
                    points.clear();
                } else {
                    points.add(new Point(e.getX(), e.getY()));

                    switch (ToolsPanel.figureList.getSelectedIndex()) {
                        case 6: {
                            if (points.size() >= 3) {
                                Drawable f = new Parallelogram(
                                        points.get(points.size() - 1),
                                        points.get(points.size() - 2),
                                        points.get(points.size() - 3));
                                addFigure(f);
                            }
                            break;
                        }
                        case 7:{
                            if (points.size() >= 2) {
                                Drawable f = new Rectangle(
                                        points.get(points.size() - 1),
                                        points.get(points.size() - 2));
                                addFigure(f);
                            }
                            break;
                        }
                        case 8: {
                            Drawable f = new Polygon(points);
                            addFigure(f);
                            break;
                        }
                        case 9:{
                            if (points.size() >= 2) {
                                Drawable f = new RegularPolygon(
                                        points.get(points.size() - 1),
                                        points.get(points.size() - 2),
                                        7
                                        );
                                addFigure(f);
                            }
                            break;
                        }
                        case 10:{
                            if (points.size() >= 2) {
                                Drawable f = new Rhombus(
                                        points.get(points.size() - 1),
                                        points.get(points.size() - 2));
                                addFigure(f);
                            }
                            break;
                        }
                    }


                    Drawable d = new Circle(
                            new Point(e.getX() - 1, e.getY() - 1),
                            new Point(e.getX() + 1, e.getY() + 1)
                    );
                    addFigure(d);

                    Point p = new Point(e.getX(), e.getY());
                    setSelected(figures.stream()
                            .filter(f -> f.contains(p))
                            .findFirst()
                            .orElse(null));
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
