package by.bsu.boring_figures.service;

public class PointsShortageException extends Exception {
    public PointsShortageException() {
        super("there're not enough points to build this figure");
    }
}
