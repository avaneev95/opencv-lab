package com.avaneev.opencv.basic;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

/**
 * @author Andrey Vaneev
 * Creation date: 17.09.2018
 */
public class ImageProcessor {

    private Mat source;

    public static ImageProcessor createProcessor(Mat source) {
        return new ImageProcessor(source);
    }

    public static ImageProcessor createProcessor(File sourceFile) {
        Mat source = Imgcodecs.imread(sourceFile.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        return new ImageProcessor(source);
    }

    private ImageProcessor(Mat source) {
        this.source = source;
    }

    public ImageProcessor rotate(int angle) {
        this.rotate(source, source, angle);
        return this;
    }

    public ImageProcessor toGrayScale() {
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.cvtColor(source, source, Imgproc.COLOR_GRAY2BGRA);
        return this;
    }

    public ImageProcessor flipVertical() {
        Core.flip(source, source, 0);
        return this;
    }

    public ImageProcessor flipHorizontal() {
        Core.flip(source, source, 1);
        return this;
    }

    public ImageProcessor toBGRA() {
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2BGRA);
        return this;
    }

    public Mat rotateAndGet(int angle) {
        Mat dest = new Mat();
        this.rotate(source, dest, angle);
        return dest;
    }

    private void rotate(Mat src, Mat dest, int angle) {
        Point center = new Point(src.cols() / 2.0, src.rows() / 2.0);

        Rect bRect = new RotatedRect(center, src.size(), angle).boundingRect();

        Mat r = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        r.put(0, 2, r.get(0, 2)[0] + (bRect.width - src.cols()) * 0.5);
        r.put(1, 2, r.get(1, 2)[0] + (bRect.height - src.rows()) * 0.5);

        Imgproc.warpAffine(src, dest, r, bRect.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(255, 255, 255, 0));
    }

    public BufferedImage toBufferedImage() {
        if (source.channels() > 4) {
            throw new IllegalArgumentException("4 channels images not supported!");
        }
        BufferedImage image;
        int width = source.width(), height = source.height(), channels = source.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        source.get(0, 0, sourcePixels);

        if (source.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    public Image toImage() {
        return toImage(source);
    }

    public Image toImage(Mat source) {
        if (source.channels() == 1) {
            return SwingFXUtils.toFXImage(this.toBufferedImage(), null);
        }
        int width = source.width(), height = source.height(), channels = source.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        source.get(0, 0, sourcePixels);
        WritableImage value = new WritableImage(width, height);
        PixelWriter pw = value.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), sourcePixels, 0, width * channels);
        return value;
    }

    public void saveToFile(File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf("."));
        MatOfInt params = new MatOfInt();
        if (ext.equalsIgnoreCase(".png")) {
            params.fromArray(Imgcodecs.IMWRITE_PNG_COMPRESSION, 9);
        } else {
            params.fromArray(Imgcodecs.CV_IMWRITE_JPEG_QUALITY, 97);
        }
        Imgcodecs.imwrite(file.getAbsolutePath(), source, params);
    }

}
