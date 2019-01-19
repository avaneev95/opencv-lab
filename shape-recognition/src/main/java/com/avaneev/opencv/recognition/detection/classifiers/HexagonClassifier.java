package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

import static com.avaneev.opencv.recognition.detection.Shape.HEXAGON;

/**
 * @author Andrei Vaneev
 * Creation date: 2019-01-19
 */
public class HexagonClassifier implements Classifier {

    @Override
    public Shape classify(Polygon polygon) {
        boolean angleCheck = polygon.getMinCos() >= -0.6 && polygon.getMinCos() <= -0.41;
        return polygon.getVertices() == 6 && angleCheck ? HEXAGON : null;
    }
}
