package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
@FunctionalInterface
public interface Classifier {
    Shape classify(Polygon polygon);
}
