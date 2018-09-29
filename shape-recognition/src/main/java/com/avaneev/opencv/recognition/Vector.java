package com.avaneev.opencv.recognition;

import org.opencv.core.Point;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector from(Point pStart, Point pEnd) {
        return new Vector(pEnd.x - pStart.x, pEnd.y - pStart.y);
    }

    public double angleBetween(Vector other) {
        return (this.x * other.x + this.y * other.y) / Math.sqrt((Math.pow(this.x, 2) + Math.pow(this.y, 2)) * (Math.pow(other.x, 2) + Math.pow(other.y, 2)) + 1e-10);
    }

    /**
     * Возвращает косинус угла между двумя векторами, заданными координатами двух точек.
     *
     * @param pt1 конечная точка первого вектора
     * @param pt2 конечная точка второго вектора
     * @param pt0 общая начальная точка обоих векторов
     * @return косинус угла между векторами
     */
    public static double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x, dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x, dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }
}
