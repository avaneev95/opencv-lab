package com.avaneev.opencv.recognition.detection;

import lombok.Getter;
import org.opencv.core.Point;

/**
 * Вспомогательный класс для работы с векторами.
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
     * Создает вектор по координатам двух точек.
     * @param pStart начальная точка.
     * @param pEnd конечная точка.
     */
    public static Vector from(Point pStart, Point pEnd) {
        return new Vector(pEnd.x - pStart.x, pEnd.y - pStart.y);
    }

    /**
     * Возвращает косинус угла между двумя векторами.
     */
    public double angleBetween(Vector other) {
        return (this.x * other.x + this.y * other.y) /
                Math.sqrt((Math.pow(this.x, 2) +
                Math.pow(this.y, 2)) * (Math.pow(other.x, 2) + Math.pow(other.y, 2)) + 1e-10);
    }
}
