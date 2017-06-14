package com.anmi.volumiofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.server.MPD;

@Slf4j
public class FlackApp extends Application {

    private TextArea textArea = new TextArea();
    private static MPD mpd;

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
