package com.anmi.volumiofx;

import com.anmi.volumiofx.scene.HBoxFileMenuFactory;
import com.anmi.volumiofx.scene.JavaSocket;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.server.MPD;


import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@Slf4j
public class App extends Application {

    private TextArea textArea = new TextArea();
    private HBoxFileMenuFactory hBoxFileMenuFactory;
    private static MPD mpd;

    private Parent createSocketContent() throws URISyntaxException, IOException, DeploymentException {
        JavaSocket javaSocket = new JavaSocket(data ->
                textArea.appendText(data));

        textArea.setPrefHeight(500.);
        textArea.setWrapText(true);

        TextField input = new TextField();
        input.setOnAction(event -> {
            String inputText = input.getText();
            try {
                javaSocket.fireCommand(inputText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(20, textArea, input);
        root.setPrefSize(800, 600);
        return root;
    }

    private Scene createFileMenuContent() throws MalformedURLException, SmbException, UnknownHostException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", "Andriy_Minenko", "F0rt0$ka1983");
        SmbFile windowsShare = new SmbFile("smb://10.17.163.37/", auth);
        // SmbFile serverShare = new SmbFile("smb://192.168.1.1/share/");
        HBox hboxFileMenu = new HBoxFileMenuFactory(windowsShare).getHboxFileMenu();
        return new Scene(hboxFileMenu, hboxFileMenu.getMinWidth(), 120);
    }


    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {

        //Workable socket
        primaryStage.setScene(new Scene(createSocketContent()));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
