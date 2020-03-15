package by.bsu.boring_figures.service;

import by.bsu.boring_figures.actually_figures.Figure;
import by.bsu.boring_figures.actually_figures.Point;
import by.bsu.boring_figures.actually_figures.Rectangle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        Constructor<?> constr = Arrays
                .stream(clazz.getConstructors())
                .filter(c -> c.getParameterTypes().length > 0)
                .collect(Collectors.toList())
                .get(0);
        List<String> paramsNames = new ArrayList<>();
        for (Class<?> a : constr.getParameterTypes()) {
            paramsNames.add(a.getSimpleName());
        }
        try {
            if (paramsNames.size() == 1) {
                return (Figure) constr.newInstance(points);
            } else if (paramsNames.size() == 2) {
                return (Figure) constr.newInstance(points.get(points.size() - 2), points.get(points.size() - 1));
            } else if (Collections.frequency(paramsNames, "Point") == 3) {
                return (Figure) constr.newInstance(points.get(points.size() - 3), points.get(points.size() - 2), points.get(points.size() - 1));
            } else {
                return (Figure) constr.newInstance(points.get(points.size() - 2), points.get(points.size() - 1), verticesNumber);
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
