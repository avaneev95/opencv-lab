package com.avaneev.opencv.recognition;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.opencv.core.Core;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

/**
 * The main application class.
 * It loads OpenCV native lib, loads main view and creates a primary stage of the app.
 *
 * @author Andrey Vaneev
 * Creation date: 15.09.2018
 */
@Log
public class Main extends Application {

    static {
        // Load OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.setProperty("prism.lcdtext", "false");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OpenCV Lab");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.setResizable(true);
        primaryStage.setMinHeight(760);
        primaryStage.setMinWidth(1100);

        try {
            URL resource = getClass().getResource("/views/MainView.fxml");
            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(new StackPane(root));
            scene.getStylesheets().addAll(
                    getClass().getResource("/css/jfoenix-fonts.css").toExternalForm(),
                    getClass().getResource("/css/jfoenix-design.css").toExternalForm(),
                    getClass().getResource("/css/styles.css").toExternalForm()
            );
            primaryStage.setScene(scene);

        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }

        primaryStage.show();
    }
}
