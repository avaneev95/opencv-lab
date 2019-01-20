package com.avaneev.opencv.recognition.detection.classifiers;

import com.avaneev.opencv.recognition.detection.Polygon;
import com.avaneev.opencv.recognition.detection.Shape;

/**
 * Interface for the geometric shape classifier.
 *
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
@FunctionalInterface
public interface Classifier {

    /**
     * Get the shape of the given polygon.
     *
     * @param polygon polygon to recognise.
     * @return recognised geometric shape.
     */
    Shape classify(Polygon polygon);
}
