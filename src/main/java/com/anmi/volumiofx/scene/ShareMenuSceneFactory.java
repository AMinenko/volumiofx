package com.anmi.volumiofx.scene;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class ShareMenuSceneFactory {
    HBox hbox;

    public ShareMenuSceneFactory(SmbFile smbFile) throws SmbException {
        ListView listView = new ListView();
        Arrays.stream(smbFile.list()).forEach(el -> listView.getItems().add(el));
        hbox = new HBox(listView);
    }

}
