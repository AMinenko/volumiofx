package com.anmi.volumiofx.scene;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.Getter;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPD;
import org.bff.javampd.song.MPDSong;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.Principal;

@Getter
public class HBoxFileMenuFactory {
    private HBox hboxFileMenu;
    private ListView fileListView;
    private MPD mpd;

    public HBoxFileMenuFactory(SmbFile smbFile) throws SmbException, UnknownHostException {
        fileListView = new ListView();
        fileListView.getItems().addAll(smbFile.listFiles());
        setListViewOnCLickEvent();
        hboxFileMenu = new HBox(fileListView);
        hboxFileMenu.setMinWidth(500);

        mpd = new MPD.Builder()
                .server("127.0.0.1")
                .build();
    }

    private void setListViewOnCLickEvent() {
        fileListView.setOnMouseClicked(click -> {
            SmbFile selectedItem = (SmbFile) fileListView.getSelectionModel().getSelectedItem();
            try {
                if (selectedItem.isDirectory()) {
                    Principal principal = selectedItem.getPrincipal();
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", principal.getName(), "F0rt0$ka1983");

                    SmbFile parentDir = new SmbFile(selectedItem.getParent(),auth);
                    fileListView.getItems().setAll(selectedItem.listFiles());
                    fileListView.getItems().set(0,parentDir);
                }
                if (selectedItem.isFile()) {
                    System.out.println("now playing: " + selectedItem.getCanonicalPath());
                    playSong(selectedItem.getCanonicalPath());
                }
            } catch (SmbException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    private void playSong(String canonicalPath) throws MalformedURLException, UnknownHostException {


        final Player player = mpd.getPlayer();
        final Playlist playlist = mpd.getPlaylist();
        playlist.clearPlaylist();
        player.getStatus();
        final MPDSong s = new MPDSong(canonicalPath,canonicalPath.substring(canonicalPath.lastIndexOf("/"),canonicalPath.length()));
        playlist.addSong(s);
        player.play();
    }

}
