package com.anmi.volumiofx;

import com.anmi.volumiofx.scene.HBoxFileMenuFactory;
import javafx.application.Application;
import javafx.application.Platform;
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
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPD;
import org.bff.javampd.song.MPDSong;


import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@Slf4j
public class App extends Application {

    private TextArea message = new TextArea();
    private HBoxFileMenuFactory hBoxFileMenuFactory;
    private static MPD mpd;

    private Parent createSocketContent() throws URISyntaxException, IOException, DeploymentException {
        ClientSocket client = new ClientSocket(data ->
                Platform.runLater(() -> message.appendText(data)));
        client.socketIoConnect();

        message.setPrefHeight(500.);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String inputText = input.getText();
            try {
                client.sendMessage(inputText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(20, message, input);
        root.setPrefSize(800, 600);
        return root;
    }

    private Scene createFileMenuContent() throws MalformedURLException, SmbException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", "Andriy_Minenko", "F0rt0$ka1983");
        SmbFile smbFile = new SmbFile("smb://192.168.1.1/share/");
        HBox hboxFileMenu = new HBoxFileMenuFactory(smbFile).getHboxFileMenu();
        return new Scene(hboxFileMenu, hboxFileMenu.getMinWidth(), 120);
    }



    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
      //  pmdStart();
        // primaryStage.setScene(new Scene(createSocketContent()));
        primaryStage.setScene(createFileMenuContent());
        primaryStage.show();
    }

    private void pmdStart() throws MalformedURLException, UnknownHostException {
        MPD mpd = new MPD.Builder()
                .server("127.0.0.1")
                .build();

        final Player player = mpd.getPlayer();
        final Playlist playlist = mpd.getPlaylist();
        playlist.clearPlaylist();
        player.getStatus();
         final MPDSong s = new MPDSong("/home/anmi/Music/01. Hotel California.wma","");
        playlist.addSong(s);
        player.play();
    }


    void handle(Event event){

    }
    public static void main(String[] args) {
        launch(args);
    }
}
