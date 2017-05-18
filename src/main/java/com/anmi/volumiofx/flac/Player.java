package com.anmi.volumiofx.flac;

import java.io.IOException;
import java.io.InputStream;

public class Player extends Thread {

    FlacPlayer flacPlayer;

    @Override
    public void run() {
        flacPlayer = new FlacPlayer();
    }

    public void play(InputStream file) throws IOException {
        flacPlayer.decode(file);
    }
}
