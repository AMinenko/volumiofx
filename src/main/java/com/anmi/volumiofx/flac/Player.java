package com.anmi.volumiofx.flac;

import javafx.concurrent.Task;

import java.io.InputStream;

public class Player extends Task{
    FlacPlayer flacPlayer;
    InputStream file;

    public Player(InputStream inputStream) {
        flacPlayer = new FlacPlayer();
        this.file = inputStream;
    }

    @Override
    protected Object call() throws Exception {
        flacPlayer.decode(file);
        return null;
    }

    public boolean isPlayng(){
        return flacPlayer.isPlaying();
    }

    public void stop() {
        flacPlayer.stopPlay();
    }
}
