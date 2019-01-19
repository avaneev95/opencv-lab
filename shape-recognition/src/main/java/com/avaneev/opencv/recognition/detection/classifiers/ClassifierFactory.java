package com.avaneev.opencv.recognition.detection.classifiers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class ClassifierFactory {

    private static final Map<Shape, Classifier> CLASSIFIER_CACHE = new EnumMap<>(Shape.class);

    static {
        CLASSIFIER_CACHE.put(Shape.QUADRANGLE, new QuadrangleClassifier());
        CLASSIFIER_CACHE.put(Shape.ELLIPSE, new EllipseClassifier());
        CLASSIFIER_CACHE.put(Shape.TRIANGLE, new TriangleClassifier());
        CLASSIFIER_CACHE.put(Shape.PENTAGON, new PentagonClassifier());
        CLASSIFIER_CACHE.put(Shape.HEXAGON, new HexagonClassifier());
        CLASSIFIER_CACHE.put(Shape.OCTAGON, new OctagonClassifier());
    }

    public static Classifier getClassifierFor(Shape shape) {
        return CLASSIFIER_CACHE.get(shape);
    }

    public static List<Classifier> getClassifiersFor(List<Shape> shapes) {
        return shapes.stream()
                .map(ClassifierFactory::getClassifierFor)
                .collect(Collectors.toList());
    }

    public static List<Classifier> getClassifiers() {
        return getClassifiersFor(Arrays.asList(Shape.values()));
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
