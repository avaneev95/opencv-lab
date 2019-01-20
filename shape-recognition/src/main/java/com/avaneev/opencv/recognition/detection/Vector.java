package com.avaneev.opencv.recognition.detection;

import lombok.Getter;
import org.opencv.core.Point;

/**
 * This class represents two point vector.
 *
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
@Getter
public class Vector {

    private final double x;
    private final double y;

    private Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates vector instance from two points coordinates.
     *
     * @param pStart start point.
     * @param pEnd end point.
     */
    public static Vector of(Point pStart, Point pEnd) {
        return new Vector(pEnd.x - pStart.x, pEnd.y - pStart.y);
    }

    /**
     * Returns the cosine of the angle between two vectors.
     */
    public double angleBetween(Vector other) {
        return (this.x * other.x + this.y * other.y) /
                Math.sqrt((Math.pow(this.x, 2) +
                Math.pow(this.y, 2)) * (Math.pow(other.x, 2) + Math.pow(other.y, 2)) + 1e-10);
    }
}
