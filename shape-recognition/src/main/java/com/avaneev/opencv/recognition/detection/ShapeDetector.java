package com.avaneev.opencv.recognition.detection;

import com.avaneev.opencv.recognition.detection.classifiers.Classifier;
import com.avaneev.opencv.recognition.detection.classifiers.ClassifierFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Andrey Vaneev
 * Creation date: 22.09.2018
 */
@Builder
public class ShapeDetector {

    private Mat source;
    private Mat edges;
    private Consumer<Shape> onShapeDetected;

    @Builder.Default
    private boolean putLabels = true;
    @Builder.Default
    private boolean drawContours = true;
    @Builder.Default
    private final Stats stats = new Stats();

    public void detect() {
        this.stats.setStartTimestamp(System.nanoTime());
        Mat edges = this.findEdges();

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        this.stats.setFoundContours(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            if (Math.abs(Imgproc.contourArea(contours.get(i))) < 100) {
                continue;
            }

            Polygon polygon = new Polygon(contours.get(i), 0.01);
            List<Classifier> classifiers = ClassifierFactory.getClassifiers();
            for (Classifier classifier : classifiers) {
                Shape shape = classifier.classify(polygon);
                if (shape != null) {
                    String shapeName = shape.name().toLowerCase();
                    if (drawContours) {
                        Imgproc.drawContours(source, contours, i, new Scalar(0, 0, 255), 2);
                    }
                    if (putLabels) {
                        this.putLabel(contours, i, shapeName);
                    }
                    if (onShapeDetected != null) {
                        onShapeDetected.accept(shape);
                    }
                    this.stats.setRecognisedContours(this.stats.getRecognisedContours() + 1);
                    break;
                }
            }
        }
        this.stats.setEndTimestamp(System.nanoTime());
    }

    private void putLabel(List<MatOfPoint> contours, int i, String shapeName) {
        Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(i).toArray()));
        int[] baseLine = new int[1];
        Size textSize = Imgproc.getTextSize(shapeName, Core.FONT_HERSHEY_SIMPLEX, 0.8, 2, baseLine);
        int y = boundRect.y + boundRect.height + 20;
        if (boundRect.y + boundRect.height + textSize.height + 20 > source.height()) {
            y = boundRect.y - 10;
        }
        Point position = new Point(boundRect.x + (boundRect.width - textSize.width) / 2, y);
        Imgproc.putText(source, shapeName, position, Core.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(0, 0, 0), 2);
    }

    public Mat getSource() {
        return this.source;
    }

    public Mat getEdges() {
        return this.edges;
    }

    public Stats getStats() {
        return this.stats;
    }

    private Mat findEdges() {
        Mat edges = new Mat(source.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(source, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(edges, edges, new Size(3, 3));
        Imgproc.Canny(edges, edges, 10, 100, 3);
        this.edges = edges;
        return edges;
    }

    @Getter
    @Setter
    public static class Stats {
        private int foundContours = 0;
        private int recognisedContours;
        private long startTimestamp;
        private long endTimestamp;

        public String getTimeString() {
            long interval = (endTimestamp - startTimestamp) / 1000000;
            final long hr = TimeUnit.MILLISECONDS.toHours(interval);
            final long min = TimeUnit.MILLISECONDS.toMinutes(interval) %60;
            final long sec = TimeUnit.MILLISECONDS.toSeconds(interval) %60;
            final long ms = TimeUnit.MILLISECONDS.toMillis(interval) %1000;
            return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
        }
    }
}
