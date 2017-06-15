package com.anmi.volumiofx.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.Principal;
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
            fileList.getItems().addAll(smbFile.listFiles());
        } catch (SmbException e) {
            e.printStackTrace();
        }
    }

    private SmbFile createFileMenuContent() {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", "Andriy_Minenko", "F0rt0$ka1983");
        try {
            return new SmbFile("smb://127.0.0.1/", auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setListViewOnCLickEvent() {
        fileList.setOnMouseClicked(click -> {
            SmbFile selectedItem = (SmbFile) fileList.getSelectionModel().getSelectedItem();
            try {
                if (selectedItem.isDirectory()) {
                    Principal principal = selectedItem.getPrincipal();
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", principal.getName(), "F0rt0$ka1983");

                    SmbFile parentDir = new SmbFile(selectedItem.getParent(), auth);
                    fileList.getItems().setAll(createNames(selectedItem.listFiles(),auth));
                    fileList.getItems().set(0, parentDir);
                }
                if (selectedItem.isFile()) {
                    System.out.println("start playing: " + selectedItem.getCanonicalPath());
                   /* if (player != null) {
                        player.stop();
                        selectedItem.getInputStream().close();
                    }
                    player.play(selectedItem.getInputStream());*/

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private Collection createNames(SmbFile[] smbFiles, NtlmPasswordAuthentication auth) {
       return Arrays.stream(smbFiles).map(el -> {
            SmbFile smbFile = null;
            try {
                smbFile = new SmbFile(el.getURL().toString(), el.getName(), auth);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return smbFile;
        }).collect(Collectors.toList());
    }

    private void setButtonBarOnClickEventListener() {
        stop.setOnMouseClicked(event -> stopPlay());
    }

    private void stopPlay() {
        System.out.println("Stop");
    }

}
