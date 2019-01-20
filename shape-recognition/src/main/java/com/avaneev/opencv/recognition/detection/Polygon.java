package com.avaneev.opencv.recognition.detection;

import lombok.Getter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a polygon.
 * Decorates {@link MatOfPoint} contour obtained from {@link Imgproc#findContours(Mat, List, Mat, int, int)}.
 * This class can approximate the contour,
 * calculate the width, height and number of angles of the polygon,
 * calculate minimum and maximum cosine of the angle between the sides.
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
     * Creates a polygon using the specified contour and an approximation accuracy factor.
     *
     * @param contour source contour.
     * @param epsilon approximation accuracy factor.
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
            Vector v1 = Vector.of(points[j % vertices], points[j - 1]);
            Vector v2 = Vector.of(points[j - 2], points[j - 1]);
            cos.add(v1.angleBetween(v2));
        }
        cos.sort(Double::compareTo);
        this.minCos = cos.getFirst();
        this.maxCos = cos.getLast();
    }
}
