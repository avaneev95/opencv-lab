package com.avaneev.opencv.recognition.classifiers;

import com.avaneev.opencv.recognition.Polygon;
import com.avaneev.opencv.recognition.Shape;
import com.avaneev.opencv.recognition.Vector;
import org.opencv.core.Point;

import static com.avaneev.opencv.recognition.Shape.*;
import static com.avaneev.opencv.recognition.Shape.TRAPEZOID;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class QuadrangleClassifier implements Classifier {
    @Override
    public Shape classify(Polygon polygon) {
        if (polygon.getVertices() == 4) {
            Point[] points = polygon.getPoints();
            Vector diagonal1 = Vector.from(points[2], points[0]);
            Vector diagonal2 = Vector.from(points[3], points[1]);
            double diagonalsAngle = diagonal1.angleBetween(diagonal2);

            double minCosThreshold = -0.1;
            double maxCosThreshold = 0.3;

            if (polygon.getMinCos() >= minCosThreshold && polygon.getMaxCos() <= maxCosThreshold) {
                return diagonalsAngle >= minCosThreshold && diagonalsAngle <= maxCosThreshold ? SQUARE : RECTANGLE;
            } else {
                int parallelSides = getParallelSides(points);

                if (parallelSides == 4) {
                    return diagonalsAngle >= minCosThreshold && diagonalsAngle <= maxCosThreshold ? RHOMBUS : PARALLELOGRAM;
                } else if (parallelSides == 2) {
                    return TRAPEZOID;
                } else {
                    return null;
                }
            }
        }
        return null;
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
