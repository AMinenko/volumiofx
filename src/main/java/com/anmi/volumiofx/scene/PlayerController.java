package com.anmi.volumiofx.scene;

import com.anmi.volumiofx.flac.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class PlayerController {

    @FXML
    private ListView fileList;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button play;

    @FXML
    private Button stop;

    private NtlmPasswordAuthentication auth;

    @FXML
    private Button resume;

    private Player player = new Player();

    private Thread playerThread;


    @FXML
    public void initialize() {
        final SmbFile smbFile = createFileMenuContent();
        initButtons();
        setFilesToList(smbFile);
        setListViewOnCLickEvent();

    }

    private void initButtons() {
        setButtonBarOnClickEventListener();
    }

    private void setFilesToList(SmbFile smbFile) {
        try {
            fileList.getItems().addAll(createNames(smbFile.listFiles()));
        } catch (SmbException e) {
            e.printStackTrace();
        }
    }

    private SmbFile createFileMenuContent() {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", "Andriy_Minenko", "F0rt0$ka1715");
        try {
           // return new SmbFile("smb://127.0.0.1/", auth);
            return new SmbFile("smb://192.168.1.1/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setListViewOnCLickEvent() {
        fileList.setOnMouseClicked(click -> {
            VolumioSmbFile selectedItem = (VolumioSmbFile) fileList.getSelectionModel().getSelectedItem();
            try {
                SmbFile selectedSmbFile = new SmbFile(selectedItem.getUrl());
                if (selectedSmbFile.isDirectory()) {
                   /* For Win10
                   Principal principal = selectedSmbFile.getPrincipal();
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", principal.getName(), "F0rt0$ka1715");
                    */
                    fileList.getItems().setAll(createNames(selectedSmbFile.listFiles()));
                    fileList.getItems().add(0, VolumioSmbFile.ofSmbFile(selectedSmbFile));
                }
                if (selectedSmbFile.isFile()) {
                    //attempt of a playlist
                    Collection playlist = createNames(createPlayList(selectedSmbFile));

                    System.out.println("start playing: " + selectedSmbFile.getCanonicalPath());
                    player.setTrack(selectedSmbFile.getInputStream());
                    if (playerThread == null) {
                        playerThread = new Thread(player);
                        playerThread.start();
                    }

                   /* if (player != null) {
                        player.shutDown();
                        selectedItem.getInputStream().close();
                    }*/

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private Collection createNames(SmbFile[] smbFiles) {
        return Arrays.stream(smbFiles).map(VolumioSmbFile::ofSmbFile).collect(Collectors.toList());
    }


    private void setButtonBarOnClickEventListener() {
        stop.setOnMouseClicked(event -> player.stop());
        resume.setOnMouseClicked(event -> player.resume());
    }

    private void stopPlay() {
        System.out.println("Stop");
    }

    private SmbFile[] createPlayList(SmbFile selectedSmbFile) throws MalformedURLException, SmbException {
        SmbFile[] alldirFiles = new SmbFile(selectedSmbFile.getParent()).listFiles();
        return (SmbFile[]) Arrays.stream(alldirFiles)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SmbFile::getName))))
                .tailSet(selectedSmbFile).stream().toArray(SmbFile[]::new);
    }

}
