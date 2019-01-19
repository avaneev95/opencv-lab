package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

import static com.avaneev.opencv.recognition.detection.Shape.TRIANGLE;

/**
 * @author Andrei Vaneev
 * Creation date: 2019-01-19
 */
public class TriangleClassifier implements Classifier {
    @Override
    public Shape classify(Polygon polygon) {
        return polygon.getVertices() == 3 ? TRIANGLE : null;
    }
}
