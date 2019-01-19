package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

import static com.avaneev.opencv.recognition.detection.Shape.OCTAGON;

/**
 * @author Andrei Vaneev
 * Creation date: 2019-01-19
 */
public class OctagonClassifier implements Classifier {

    @Override
    public Shape classify(Polygon polygon) {
        boolean angleCheck = polygon.getMinCos() >= -0.72 && polygon.getMinCos() <= -0.66;
        return polygon.getVertices() == 8 && angleCheck ? OCTAGON : null;
    }
}
