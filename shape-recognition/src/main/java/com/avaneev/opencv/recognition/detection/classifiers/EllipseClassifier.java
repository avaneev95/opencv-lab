package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;
import org.opencv.imgproc.Imgproc;

import static com.avaneev.opencv.recognition.detection.Shape.CIRCLE;
import static com.avaneev.opencv.recognition.detection.Shape.ELLIPSE;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class EllipseClassifier implements Classifier {

    @Override
    public Shape classify(Polygon polygon) {
        if (polygon.getVertices() > 8) {
            double area = Imgproc.contourArea(polygon.getContour());
            double aspectRatio = ((double) polygon.getWidth()) / polygon.getHeight();
            int radiusX = polygon.getWidth() / 2;
            int radiusY = polygon.getHeight() / 2;
            if (Math.abs(radiusX - radiusY) < 3) {
                if (Math.abs(1 - aspectRatio) <= 0.2 && Math.abs(1 - (area / (Math.PI * Math.pow(radiusX, 2)))) <= 0.2) {
                    return CIRCLE;
                }
            } else {
                if (Math.abs(1 - (area / (Math.PI * radiusX * radiusY))) <= 0.5) {
                    return ELLIPSE;
                }
            }
        }
        return null;
    }
}
