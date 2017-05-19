package com.anmi.volumiofx.scene;

import com.anmi.volumiofx.flac.Player;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.Getter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.Principal;

@Getter
public class HBoxFileMenuFactory {
    private HBox hboxFileMenu;
    private ListView fileListView;
    Player player;
    private Thread thread;

    public HBoxFileMenuFactory(SmbFile smbFile) throws SmbException, UnknownHostException {
        fileListView = new ListView();
        fileListView.getItems().addAll(smbFile.listFiles());
        setListViewOnCLickEvent();
        hboxFileMenu = new HBox(fileListView);
        hboxFileMenu.setMinWidth(500);
    }

    private void setListViewOnCLickEvent() {
        fileListView.setOnMouseClicked(click -> {
            SmbFile selectedItem = (SmbFile) fileListView.getSelectionModel().getSelectedItem();
            try {
                if (selectedItem.isDirectory()) {
                    Principal principal = selectedItem.getPrincipal();
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", principal.getName(), "F0rt0$ka1983");

                    SmbFile parentDir = new SmbFile(selectedItem.getParent(), auth);
                    fileListView.getItems().setAll(selectedItem.listFiles());
                    fileListView.getItems().set(0, parentDir);
                }
                if (selectedItem.isFile()) {
                    System.out.println("start playing: " + selectedItem.getCanonicalPath());
                    if (player != null) {
                        player.stop();
                        selectedItem.getInputStream().close();
                    }
                    player = new Player(selectedItem.getInputStream());

                    if (thread != null ) {
                        thread.stop();
                    }
                    thread = new Thread(player);
                    thread.start();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

}
