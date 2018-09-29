package com.avaneev.opencv.recognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static com.avaneev.opencv.recognition.Shape.*;

/**
 * @author Andrey Vaneev
 * Creation date: 22.09.2018
 */
//@Builder
public class ShapeDetector {

    private Mat source;
    private boolean addLabels;
    private boolean drawContours;


    public Shape detect(MatOfPoint contour) {

        Polygon features = new Polygon(contour, 0.015);

        int vertices = features.getVertices();
        double minCos = features.getMinCos();
        double maxCos = features.getMaxCos();
        Point[] points = features.getPoints();

        if (vertices == 3) {
            return TRIANGLE;
        }

        if (vertices >= 4 && vertices <= 6) {
            if (vertices == 4) {
                return classifyRectangle(points, minCos, maxCos);
            }
            if (vertices == 5 && minCos >= -0.36 && maxCos <= -0.27) {
                return PENTAGON;
            }
            if (vertices == 6 && minCos >= -0.60 && maxCos <= -0.41) {
                return HEXAGON;
            }
        }

        if (vertices >= 8) {
            double area = Imgproc.contourArea(contour);
            double aspectRatio = ((double) features.getWidth()) / features.getHeight();
            int radiusX = features.getWidth() / 2;
            int radiusY = features.getHeight() / 2;
            if (Math.abs(radiusX - radiusY) < 0.1) {
                if (Math.abs(1 - aspectRatio) <= 0.2 && Math.abs(1 - (area / (Math.PI * Math.pow(radiusX, 2)))) <= 0.2) {
                    return CIRCLE;
                }
            } else {
                if (Math.abs(1 - (area / (Math.PI * radiusX * radiusY))) <= 0.2) {
                    return ELLIPSE;
                }
            }
        }

        return null;
    }

    private Shape classifyRectangle(Point[] polygon, double minCos, double maxCos) {
        Vector diagonal1 = Vector.from(polygon[2], polygon[0]);
        Vector diagonal2 = Vector.from(polygon[3], polygon[1]);
        double diagonalsAngle = diagonal1.angleBetween(diagonal2);

        double minCosThreshold = -0.1;
        double maxCosThreshold = 0.3;

        if (minCos >= minCosThreshold && maxCos <= maxCosThreshold) {
            return diagonalsAngle >= minCosThreshold && diagonalsAngle <= maxCosThreshold ? SQUARE : RECTANGLE;
        } else {
            int parallelSides = getParallelSides(polygon);

            if (parallelSides == 4) {
                return diagonalsAngle >= minCosThreshold && diagonalsAngle <= maxCosThreshold ? RHOMBUS : PARALLELOGRAM;
            } else if (parallelSides == 2) {
                return TRAPEZOID;
            } else {
                return null;
            }
        }
    }

    private int getParallelSides(Point[] points) {
        double parallelThreshold = 0.98;
        Vector side1 = Vector.from(points[0], points[1]);
        Vector side3 = Vector.from(points[2], points[3]);
        Vector side2 = Vector.from(points[1], points[2]);
        Vector side4 = Vector.from(points[3], points[0]);
        return (Math.abs(side1.angleBetween(side3)) >= parallelThreshold ? 2 : 0) +
               (Math.abs(side2.angleBetween(side4)) >= parallelThreshold ? 2 : 0);
    }
}
