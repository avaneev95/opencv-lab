package com.avaneev.opencv.recognition.detection.classifiers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory to get the instances of the geometric shape classifiers.
 *
 * @author Andrey Vaneev
 * Creation date: 29.09.2018
 */
public class ClassifierFactory {

    private static final Map<ShapeType, Classifier> CLASSIFIER_CACHE = new EnumMap<>(ShapeType.class);

    static {
        CLASSIFIER_CACHE.put(ShapeType.QUADRANGLE, new QuadrangleClassifier());
        CLASSIFIER_CACHE.put(ShapeType.ELLIPSE, new EllipseClassifier());
        CLASSIFIER_CACHE.put(ShapeType.TRIANGLE, new TriangleClassifier());
        CLASSIFIER_CACHE.put(ShapeType.PENTAGON, new PentagonClassifier());
        CLASSIFIER_CACHE.put(ShapeType.HEXAGON, new HexagonClassifier());
        CLASSIFIER_CACHE.put(ShapeType.OCTAGON, new OctagonClassifier());
    }

    /**
     * Returns classifier capable to recognise shape of the given shape type.
     *
     * @param shape geometric shape type.
     * @see ShapeType
     */
    public static Classifier getClassifierFor(ShapeType shape) {
        return CLASSIFIER_CACHE.get(shape);
    }

    /**
     * Returns a list of classifiers for the given shape types.
     *
     * @param shapes geometric shape types.
     * @see ShapeType
     */
    public static List<Classifier> getClassifiersFor(List<ShapeType> shapes) {
        return shapes.stream()
                .map(ClassifierFactory::getClassifierFor)
                .collect(Collectors.toList());
    }

    /**
     * Returns all available classifier.
     */
    public static List<Classifier> getClassifiers() {
        return getClassifiersFor(Arrays.asList(ShapeType.values()));
    }

    public enum ShapeType {
        TRIANGLE,
        PENTAGON,
        HEXAGON,
        QUADRANGLE,
        ELLIPSE,
        OCTAGON
    }
}
