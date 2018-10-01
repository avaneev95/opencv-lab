package com.avaneev.opencv.recognition.detection;

import lombok.Getter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Вспомогательный класс для хранения характеристик полигона.</p>
 * <p>Декорирует {@link MatOfPoint} - контур полученный с помощью {@link Imgproc#findContours(Mat, List, Mat, int, int)}.</p>
 * <p>Аппроксимирует контур, рассчитывает количество углов, ширину и высоту полигона,
 * минимальное и максимальное значение косинуса угла между сторонами.</p>
 *
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
@Getter
public class Polygon {
    private Point[] points;
    private int vertices;
    private int height;
    private int width;
    private double minCos;
    private double maxCos;
    private MatOfPoint contour;

    /**
     * Создает полигон, используя заданный контур и коэффициент точности аппроксимации
     *
     * @param contour исходный контур
     * @param epsilon коэффицент точноти аппроксимации контура
     */
    public Polygon(MatOfPoint contour, double epsilon) {
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        MatOfPoint2f curve = new MatOfPoint2f(contour.toArray());
        Imgproc.approxPolyDP(curve, approxCurve, Imgproc.arcLength(curve, true) * epsilon, true);
        Rect r = Imgproc.boundingRect(contour);

        this.width = r.width;
        this.height = r.height;
        this.points = approxCurve.toArray();
        this.vertices = points.length;
        this.contour = contour;

        LinkedList<Double> cos = new LinkedList<>();
        for (int j = 2; j < vertices + 1; j++) {
            Vector v1 = Vector.from(points[j % vertices], points[j - 1]);
            Vector v2 = Vector.from(points[j - 2], points[j - 1]);
            cos.add(v1.angleBetween(v2));
        }
        cos.sort(Double::compareTo);
        this.minCos = cos.getFirst();
        this.maxCos = cos.getLast();
    }
}
