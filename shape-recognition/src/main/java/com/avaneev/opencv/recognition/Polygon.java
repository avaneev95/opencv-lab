package com.avaneev.opencv.recognition;

import lombok.Getter;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;

/**
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
            cos.add(Vector.angle(points[j % vertices], points[j - 2], points[j - 1]));
        }
        cos.sort(Double::compareTo);
        this.minCos = cos.getFirst();
        this.maxCos = cos.getLast();
    }
}
