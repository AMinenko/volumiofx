package com.anmi.volumiofx.scene;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.Getter;

import java.net.MalformedURLException;

@Getter
public class HBoxFileMenuFactory {
    private HBox hboxFileMenu;
    private ListView fileListView;

    public HBoxFileMenuFactory(SmbFile smbFile) throws SmbException {
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
                    SmbFile parentDir = new SmbFile(selectedItem.getParent());
                    fileListView.getItems().setAll(selectedItem.listFiles());
                    fileListView.getItems().set(0,parentDir);
                }
                if (selectedItem.isFile()) {
                    System.out.println("now playing: " + selectedItem.getCanonicalPath());
                }
            } catch (SmbException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }
}
