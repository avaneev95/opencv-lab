package com.avaneev.opencv.basic;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.java.Log;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * @author Andrey Vaneev
 * Creation date: 16.09.2018
 */
@Log
public class MainController {

    @FXML
    public Label errorLabel;
    @FXML
    public VBox fileDropBox;
    @FXML
    private VBox fileLoadingBox;
    @FXML
    private JFXButton applyRotationButton;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXSlider angleSlider;
    @FXML
    private JFXTextField angleField;
    @FXML
    private BorderPane settingsPane;

    private ImageProcessor imageProcessor;
    private FileChooser fileChooser;
    private String filename;

    private static final String JPEG = "*.jpeg";
    private static final String JPG = "*.jpg";
    private static final String PNG = "*.png";

    @FXML
    private void initialize() {
        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File("./"));

        Platform.runLater(() -> {
            Stage stage = ((Stage) imageView.getScene().getWindow());
            stage.widthProperty().addListener((o, oldValue, newValue) -> fitImageViewWidth(newValue.doubleValue()));
            stage.heightProperty().addListener((o, oldValue, newValue) -> fitImageViewHeight(newValue.doubleValue()));
        });
    }

    @FXML
    private void chooseFile(ActionEvent event) {
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", JPEG, JPG, PNG),
                new FileChooser.ExtensionFilter("PNG Format", PNG),
                new FileChooser.ExtensionFilter("JPEG Format", JPEG, JPG)
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
    private void onGrayScaleAction() {
        imageProcessor.toGrayScale();
        this.updateImageView();
    }

    @FXML
    private void onVerticalFlipAction() {
        imageProcessor.flipVertical();
        this.updateImageView();
    }

    @FXML
    private void onHorizontalFlipAction() {
        imageProcessor.flipHorizontal();
        this.updateImageView();
    }

    @FXML
    private void applyRotation() {
        imageProcessor.rotate((int) angleSlider.getValue());
        angleSlider.setValue(0); // Will fire image reload in ImageView
    }

    @FXML
    private void onSaveAction(ActionEvent event) {
        fileChooser.setTitle("Save image");
        fileChooser.setInitialFileName(filename);
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Format", PNG),
                new FileChooser.ExtensionFilter("JPEG Format", JPEG, JPG)
        );
        File file = fileChooser.showSaveDialog(((JFXButton) event.getSource()).getScene().getWindow());
        if (file != null) {
            JFXDialog alert = new JFXDialog();
            CompletableFuture
                    .runAsync(() -> imageProcessor.saveToFile(file))
                    .thenRun(alert::close);
            alert.setOverlayClose(false);
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.getStyleClass().add("alert-dialog");
            layout.setHeading(new Label("Save image"));
            VBox vBox = new VBox(new JFXSpinner(), new Label("Saving " + file.getName() + "..."));
            vBox.setAlignment(Pos.CENTER);
            vBox.getStyleClass().add("file-loading-box");
            vBox.setPadding(new Insets(32, 0, 16, 0));
            layout.setBody(vBox);
            alert.setContent(layout);
            alert.show((StackPane) ((JFXButton) event.getTarget()).getScene().getRoot());
        }
    }

    @FXML
    private void reset() {
        imageProcessor = null;
        fileDropBox.setVisible(true);
        errorLabel.setVisible(false);
        imageView.setImage(null);
        imageView.getParent().setVisible(false);
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);
        angleSlider.setValue(0);
        settingsPane.setDisable(true);
    }

    private void initScene() {
        imageView.setImage(imageProcessor.toImage());
        this.resizeImage();

        angleField.setOnAction(event -> {
            this.setAngleInInput();
            angleField.getParent().requestFocus();
        });

        angleSlider.valueProperty().addListener((o, oldValue, newValue) -> {
            int angle = newValue.intValue();
            angleField.setText(String.valueOf(angle));
            applyRotationButton.setVisible(angle != 0);
            if (imageProcessor != null) {
                Platform.runLater(() -> {
                    imageView.setImage(imageProcessor.toImage(imageProcessor.rotateAndGet(angle)));
                    this.resizeImage();
                });
            }
        });

        fileLoadingBox.setVisible(false);
        imageView.getParent().setVisible(true);
        settingsPane.setDisable(false);
    }

    private void setAngleInInput() {
        if (angleField.validate() && !angleField.getText().isEmpty()) {
            angleSlider.setValue(Double.parseDouble(angleField.getText()));
        }
    }

    private void updateImageView() {
        if (angleSlider.getValue() != 0) {
            angleSlider.setValue(0);
        } else {
            Platform.runLater(() -> imageView.setImage(imageProcessor.toImage()));
        }
    }

    private void loadImage(File file) {
        fileDropBox.setVisible(false);
        fileLoadingBox.setVisible(true);
        filename = file.getName().substring(0, file.getName().lastIndexOf('.')) + "_modified";
        CompletableFuture
                .runAsync(() -> this.readImage(file))
                .thenRun(() -> Platform.runLater(this::initScene));
    }

    private void resizeImage() {
        Stage stage = ((Stage) imageView.getScene().getWindow());
        fitImageViewWidth(stage.getWidth());
        fitImageViewHeight(stage.getHeight());
    }


    private void fitImageViewWidth(double stageWidth) {
        if (imageView.getImage() == null) {
            return;
        }
        double w = stageWidth - 416; // Right sidebar width offset
        if (imageView.getImage().getRequestedWidth() > w) {
            imageView.setFitWidth(w);
        } else if (imageView.getFitWidth() != imageView.getImage().getRequestedWidth()) {
            imageView.setFitWidth(imageView.getImage().getRequestedWidth());
        }
    }

    private void fitImageViewHeight(double stageHeight) {
        if (imageView.getImage() == null) {
            return;
        }
        double h = stageHeight - 86; // Toolbar height offset
        if (imageView.getImage().getRequestedHeight() > h) {
            imageView.setFitHeight(h);
        } else if (imageView.getFitHeight() != imageView.getImage().getRequestedHeight()) {
            imageView.setFitHeight(imageView.getImage().getRequestedHeight());
        }
    }

    private void readImage(File file) {
        try {
            this.imageProcessor = ImageProcessor.createProcessor(file);
            this.imageProcessor.toBGRA();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
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
