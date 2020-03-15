package by.bsu.boring_figures.boring_panels;

import by.bsu.boring_figures.actually_figures.Circle;
import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.service.FiguresBuilder;
import by.bsu.boring_figures.service.PointsShortageException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static by.bsu.boring_figures.boring_panels.ToolsPanel.figuresBinding;
import static by.bsu.boring_figures.boring_panels.ToolsPanel.verticesSpinner;
import static java.awt.event.ActionEvent.SHIFT_MASK;
import static java.awt.event.InputEvent.ALT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;

public class DrawPanel extends JPanel {

    public static List<Point> points;
    private final int MOVE_MASK = ALT_MASK;
    private final int ADD_POINT_MASK = CTRL_MASK;
    private final int CLEAR_POINTS_MASK = SHIFT_MASK;
    private final int DRAW_FIGURE_MASK = CTRL_MASK | CLEAR_POINTS_MASK;
    private final int COMBINED_MASK = MOVE_MASK | ADD_POINT_MASK | CLEAR_POINTS_MASK | DRAW_FIGURE_MASK;

    private final MouseListener moveFigureListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (matchesOnly(e.getModifiers(), MOVE_MASK)) {
                if (selected != null) {
                    selected.move(new Point(e.getX(), e.getY()));
                    repaint();
                    System.out.println("moved " + selected.getClass().getSimpleName());
                }
                return;
            }

            Point p = new Point(e.getX(), e.getY());
            setSelected(figures.stream()
                    .filter(f -> f.contains(p))
                    .findFirst()
                    .orElse(null));
        }

    };

    private final MouseListener drawFigureMouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {

            if (matchesOnly(e.getModifiers(), ADD_POINT_MASK) || matchesOnly(e.getModifiers(), DRAW_FIGURE_MASK)) {
                Point selectedPoint = new Point(e.getX(), e.getY());
                points.add(selectedPoint);
                drawSelectedPoint(selectedPoint);

                if (matchesOnly(e.getModifiers(), DRAW_FIGURE_MASK)) {
                    Class<?> clazzNew = figuresBinding.get(ToolsPanel.figuresComboBox.getSelectedItem());

                    builder.setClazz(clazzNew);
                    builder.setPoints(points);
                    builder.setVerticesNumber((int) verticesSpinner.getValue());

                    try {
                        Figure f = builder.build();
                        addFigure(f);
                    } catch (PointsShortageException ex) {
                        JOptionPane.showMessageDialog(DrawPanel.this, "Need more points");
                        System.out.println("need more points");
                    } catch (Error | Exception ex) {
                        JOptionPane.showMessageDialog(DrawPanel.this, "Too much points. Selection cleared.");
                        System.out.println("can't bulid");
                    } finally {
                        figurePoints.clear();
                        points.clear();
                        repaint();
                    }
                }
                return;
            }

            if (matchesOnly(e.getModifiers(), CLEAR_POINTS_MASK)) {
                points.clear();
                return;
            }
        }

        private void drawSelectedPoint(Point point) {
            figurePoints.add(selectedPointMarker(point));
            repaint();
        }

        private Figure selectedPointMarker(Point point) {
            int pointRadius = 3;
            return new Circle(
                    new Point(point.getX() - pointRadius, point.getY() - pointRadius),
                    new Point(point.getX() + pointRadius, point.getY() + pointRadius)
            );
        }
    };

    private LinkedList<Figure> figures;
    private Figure selected;
    private FiguresBuilder builder;
    private List<Figure> figurePoints;

    public DrawPanel() {
        super(true);
        points = new ArrayList<>();
        this.figures = new LinkedList<>();
        this.builder = new FiguresBuilder();
        this.figurePoints = new ArrayList<>();

        setBorder(new BevelBorder(BevelBorder.RAISED));

        addMouseListener(moveFigureListener);
        addMouseListener(drawFigureMouseListener);
    }

    private boolean matches(int modifiers, int mask) {
        return (modifiers & mask) != 0;
    }

    private boolean matchesOnly(int modifiers, int mask) {
        return (modifiers & COMBINED_MASK) == mask;
    }

    public void addFigure(Figure figure) {
        this.figures.addFirst(figure);
        this.figurePoints.clear();
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
                figures.remove(figure);
                figures.addFirst(figure);
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
        List<Figure> reversed = new ArrayList<>(figures);
        Collections.reverse(reversed);
        reversed.forEach(drawable -> drawable.draw(g2d));
        figurePoints.forEach(drawable -> drawable.draw(g2d));
    }
}
