package com.avaneev.opencv.recognition;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Andrey Vaneev
 * Creation date: 16.09.2018
 */
public class MainController {

    @FXML
    public Label errorLabel;
    @FXML
    public VBox fileDropBox;
    public JFXToggleButton showEdgesToggle;
    @FXML
    private VBox fileLoadingBox;
    @FXML
    private ImageView imageView;
    @FXML
    private BorderPane settingsPane;

    private FileChooser fileChooser;
    private Mat source;
    private Mat edges;

    @FXML
    private void initialize() {
        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File("./"));

        Platform.runLater(() -> this.loadImage(new File("./test_data/shapes.png")));
    }

    @FXML
    private void chooseFile(ActionEvent event) {
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpeg", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("PNG Format", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Format", "*.jpeg", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(((JFXButton) event.getSource()).getScene().getWindow());
        if (file != null) {
            this.loadImage(file);
        }
    }

    @FXML
    private void onFileOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            String path = file.toPath().toString();
            if (Stream.of(".jpeg", ".jpg", ".png").anyMatch(path::endsWith)) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        }
        event.consume();
    }

    @FXML
    private void onFileDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            loadImage(db.getFiles().get(0));
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void onSaveAction(ActionEvent event) {
//        fileChooser.setTitle("Save image");
//        fileChooser.setInitialFileName(filename);
//        fileChooser.getExtensionFilters().clear();
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("PNG Format", "*.png"),
//                new FileChooser.ExtensionFilter("JPEG Format", "*.jpeg", "*.jpg")
//        );
//        File file = fileChooser.showSaveDialog(((JFXButton) event.getSource()).getScene().getWindow());
//        if (file != null) {
//            JFXDialog alert = new JFXDialog();
//            CompletableFuture
//                    .runAsync(() -> imageProcessor.saveToFile(file))
//                    .thenRun(alert::close);
//            alert.setOverlayClose(false);
//            JFXDialogLayout layout = new JFXDialogLayout();
//            layout.getStyleClass().add("alert-dialog");
//            layout.setHeading(new Label("Save image"));
//            VBox vBox = new VBox(new JFXSpinner(), new Label("Saving " + file.getName() + "..."));
//            vBox.setAlignment(Pos.CENTER);
//            vBox.getStyleClass().add("file-loading-box");
//            vBox.setPadding(new Insets(32, 0, 16, 0));
//            layout.setBody(vBox);
//            alert.setContent(layout);
//            alert.show((StackPane) ((JFXButton) event.getTarget()).getScene().getRoot());
//        }
    }

    @FXML
    private void reset() {
        fileDropBox.setVisible(true);
        imageView.setImage(null);
        imageView.getParent().setVisible(false);
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);
        settingsPane.setDisable(true);
    }

    private void initScene() {

        imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) HighGui.toBufferedImage(source), null));
        this.resizeImage();

        Stage stage = ((Stage) imageView.getScene().getWindow());
        stage.widthProperty().addListener((o, oldValue, newValue) -> fitImageViewWidth(newValue.doubleValue()));
        stage.heightProperty().addListener((o, oldValue, newValue) -> fitImageViewHeight(newValue.doubleValue()));

        fileLoadingBox.setVisible(false);
        imageView.getParent().setVisible(true);
        settingsPane.setDisable(false);

        showEdgesToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Mat img = newValue ? edges : source;
            Platform.runLater(() ->imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) HighGui.toBufferedImage(img), null)));
        });



        edges = new Mat(source.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(source, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(edges, edges, new Size(3, 3));
        Imgproc.Canny(edges, edges, 10, 100, 3);

        ShapeDetector classifier = new ShapeDetector();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int i = 0; i < contours.size(); i++) {
            if (Math.abs(Imgproc.contourArea(contours.get(i))) < 100) {
                continue;
            }

            Shape shape = classifier.detect(contours.get(i));
            if (shape != null) {
                String shapeName = shape.name().toLowerCase();
                Imgproc.drawContours(source, contours, i, new Scalar(0, 0, 255), 2);
                Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(i).toArray()));
                Imgproc.putText(source, shapeName, new Point(boundRect.x, boundRect.y - 10), Core.FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0, 0, 0), 1);
            }
        }
    }

    private void loadImage(File file) {
        fileDropBox.setVisible(false);
        fileLoadingBox.setVisible(true);
        CompletableFuture
                .runAsync(() -> this.readImage(file))
                .thenRun(() -> Platform.runLater(this::initScene))
                .exceptionally(t -> {
                    t.printStackTrace();
                    return null;
                });
    }

    private void resizeImage() {
        Stage stage = ((Stage) imageView.getScene().getWindow());
        fitImageViewWidth(stage.getWidth());
        fitImageViewHeight(stage.getHeight());
    }

    private void fitImageViewWidth(double stageWidth) {
        double w = stageWidth - 416; // Right sidebar width offset
        if (imageView.getImage().getRequestedWidth() > w) {
            imageView.setFitWidth(w);
        } else if (imageView.getFitWidth() != imageView.getImage().getRequestedWidth()) {
            imageView.setFitWidth(imageView.getImage().getRequestedWidth());
        }
    }

    private void fitImageViewHeight(double stageHeight) {
        double h = stageHeight - 86; // Toolbar height offset
        if (imageView.getImage().getRequestedHeight() > h) {
            imageView.setFitHeight(h);
        } else if (imageView.getFitHeight() != imageView.getImage().getRequestedHeight()) {
            imageView.setFitHeight(imageView.getImage().getRequestedHeight());
        }
    }

    private void readImage(File file) {
        try {
            source = Imgcodecs.imread(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            this.showFileError(String.format("Can't read file \"%s\"! Try another one!", file.getName()));
        }
    }

    private void showFileError(String text) {
        fileLoadingBox.setVisible(false);
        fileDropBox.setVisible(true);
        errorLabel.setText(text);
        errorLabel.getParent().setVisible(true);
    }
}
