package com.avaneev.opencv.recognition.classifiers;

import java.util.ArrayList;
import java.util.List;

import static com.avaneev.opencv.recognition.Shape.*;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class ClassifierFactory {

    public static Classifier getClassifierFor(Shape shape) {
        switch (shape) {
            case QUADRANGLE:
                return new QuadrangleClassifier();
            case ELLIPSE:
                return new EllipseClassifier();
            case TRIANGLE:
                return p -> p.getVertices() == 3 ? TRIANGLE : null;
            case PENTAGON:
                return p -> (p.getVertices() == 5 && p.getMinCos() >= -0.36 && p.getMinCos() <= -0.27) ? PENTAGON : null;
            case HEXAGON:
                return p -> (p.getVertices() == 6 && p.getMinCos() >= -0.6 && p.getMinCos() <= -0.41) ? HEXAGON : null;
            case OCTAGON:
                return p -> (p.getVertices() == 8 && p.getMinCos() >= -0.8 && p.getMinCos() <= -0.61) ? OCTAGON : null;
            default:
                return null;
        }
    }

    public static List<Classifier> getClassifiersFor(List<Shape> shapes) {
        List<Classifier> classifiers = new ArrayList<>();
        for (Shape shape : shapes) {
            classifiers.add(getClassifierFor(shape));
        }
        return classifiers;
    }

    public static List<Classifier> getClassifiers() {
        List<Classifier> classifiers = new ArrayList<>();
        for (Shape shape : Shape.values()) {
            classifiers.add(getClassifierFor(shape));
        }
        return classifiers;
    }

    public enum Shape {
        TRIANGLE,
        PENTAGON,
        HEXAGON,
        QUADRANGLE,
        ELLIPSE,
        OCTAGON
    }
}
