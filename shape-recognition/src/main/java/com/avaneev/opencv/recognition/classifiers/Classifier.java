package com.avaneev.opencv.recognition.classifiers;

import com.avaneev.opencv.recognition.Polygon;
import com.avaneev.opencv.recognition.Shape;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
@FunctionalInterface
public interface Classifier {
    Shape classify(Polygon polygon);
}
