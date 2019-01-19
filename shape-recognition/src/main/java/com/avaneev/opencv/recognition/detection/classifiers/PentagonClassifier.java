package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

import static com.avaneev.opencv.recognition.detection.Shape.PENTAGON;

/**
 * @author Andrei Vaneev
 * Creation date: 2019-01-19
 */
public class PentagonClassifier implements Classifier {

    @Override
    public Shape classify(Polygon polygon) {
        boolean angleCheck = polygon.getMinCos() >= -0.36 && polygon.getMinCos() <= -0.27;
        return polygon.getVertices() == 5 && angleCheck ? PENTAGON : null;
    }
}
