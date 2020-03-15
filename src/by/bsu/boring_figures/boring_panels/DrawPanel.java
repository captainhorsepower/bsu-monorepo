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
import java.lang.reflect.InvocationTargetException;
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
    private final int COMBINED_MASK = MOVE_MASK | ADD_POINT_MASK;
    private MouseListener moveFigureListener = new MouseAdapter() {
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

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if ((e.getModifiers() & SHIFT_MASK) != 0) {
                    points.clear();
                } else if ((e.getModifiers() & CTRL_MASK) != 0) {
                    points.add(new Point(e.getX(), e.getY()));
                    figurePoints.add(new Circle(
                            new Point(e.getX() - 1, e.getY() - 1),
                            new Point(e.getX() + 1, e.getY() + 1)));
                    repaint();

                    Class<?> clazzNew = figuresBinding.get(ToolsPanel.figuresComboBox.getSelectedItem());

                    builder.setClazz(clazzNew);
                    builder.setPoints(points);
                    builder.setVerticesNumber((int) verticesSpinner.getValue());

                    try {
                        Figure f = builder.build();
                        addFigure(f);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                        System.out.println("can't bulid");
                    } catch (PointsShortageException ex) {
                        System.out.println("need more points");
                    }
                    return;

                }

            }
        });
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
