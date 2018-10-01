package com.avaneev.opencv.recognition;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;

/**
 * @author Andrey Vaneev
 * Creation date: 15.09.2018
 */
public class Main2 {
    static {
//        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        File imageSrc = new File("test_data/shapes.png");
        Mat src = Imgcodecs.imread(imageSrc.getAbsolutePath());
        Mat mat = new Mat(src.size(), CvType.CV_8UC1);

        Imgproc.cvtColor(src, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(mat, mat, new Size(3, 3));
//        Imgproc.threshold(mat, mat, 100, 255, 0);
//        Imgproc.adaptiveThreshold(src, src, 255, Adapt, thresholdType, blockSize, C[, dst]	)


//        Imgproc.Canny(mat, mat, 100, 255, 5);
        Imgproc.Canny(mat, mat, 10, 100, 3);
//        List<MatOfPoint> contours = new ArrayList<>();
//        Mat hierarchy = new Mat();
//        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        HighGui.imshow("sd", mat);
        HighGui.waitKey(0);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println(contours.size());

        List<Moments> mu = new ArrayList<>(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            mu.add(Imgproc.moments(contours.get(i)));
        }

        System.out.println("\t Info: Area and Polygon Length \n");
        for (int i = 0; i < contours.size(); i++) {
            Mat hu = new Mat();
            Moments m = mu.get(i);
            Imgproc.HuMoments(m, hu);
//            System.out.println(hu.dump());
//            System.out.println();
            System.out.format("%.4f", Imgproc.matchShapes(contours.get(1), contours.get(i), Imgproc.CV_CONTOURS_MATCH_I3, 0.0));
            System.out.println();
//            System.out.println("Fit ellipse: " + Imgproc.fitEllipse(new MatOfPoint2f(contours.get(i).toArray())));
//            System.out.format(" * Polygon[%d] - Area (M_00) = %.2f - Area OpenCV: %.2f - Length: %.2f\n", i,
//                    mu.get(i).m00, Imgproc.contourArea(contours.get(i)),
//                    Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true));
//            Imgproc.drawContours(src, contours, i, new Scalar(45, 78, 45), -1);

//            int cX = (int) ((m.m10 / m.m00) * 0.1);
//            int cY = (int) ((m.m01 / m.m00) * 0.1);

            Imgproc.drawContours(src, contours, i, new Scalar(0, 0, 255), 2);


            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f curve = new MatOfPoint2f(contours.get(i).toArray());
            Imgproc.approxPolyDP(curve, approxCurve, Imgproc.arcLength(curve, true) * 0.015, true);

            for (Point point : approxCurve.toArray()) {
                System.out.println(point);
            }

            System.out.println(approxCurve.size());
//            System.out.println(contours.get(i).size());

            Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(i).toArray()));
            Imgproc.putText(src, "Hello World!", new Point(boundRect.x, boundRect.y - 10), Core.FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0, 0, 0), 1);

            HighGui.imshow("sd", src);
            HighGui.waitKey(0);
        }

//        System.out.println(Imgproc.matchShapes(contours.get(0), contours.get(1), Imgproc.CV_CONTOURS_MATCH_I1, 0.0));


//        HighGui.imshow("sd", src);
//        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}