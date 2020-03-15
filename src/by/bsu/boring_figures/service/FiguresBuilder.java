package by.bsu.boring_figures.service;

import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.Point;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FiguresBuilder {

    private Class<?> clazz;
    private List<Point> points;
    private int verticesNumber;

    public FiguresBuilder() {
    }

    public FiguresBuilder(Class<?> clazz, List<Point> points, int verticesNumber) {
        this.clazz = clazz;
        this.points = points;
        this.verticesNumber = verticesNumber;
    }

    public Figure build() throws IllegalAccessException, InvocationTargetException, InstantiationException, PointsShortageException {
        Constructor<?> constr = clazz.getConstructors()[0];
        List<String> paramsNames = new ArrayList<>();
        Arrays.stream(constr.getParameterTypes())
                .forEach(e -> paramsNames.add(e.getSimpleName()));
        try {
            if (paramsNames.size() == 1) {
                if (points.size() < 3) throw new PointsShortageException();
                return (Figure) constr.newInstance(new ArrayList<>(points));
            } else if (paramsNames.size() == 2) {
                assert points.size() < 3;
                Figure f = (Figure) constr.newInstance(points.get(0), points.get(1));
                return f;
            } else if (Collections.frequency(paramsNames, "Point") == 3) {
                assert points.size() < 4;
                Figure f = (Figure) constr.newInstance(points.get(0), points.get(1), points.get(2));
                return f;
            } else {
                assert points.size() < 3;
                Figure f = (Figure) constr.newInstance(points.get(0), points.get(1), verticesNumber);
                return f;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PointsShortageException();
        }
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setVerticesNumber(int verticesNumber) {
        this.verticesNumber = verticesNumber;
    }
}
