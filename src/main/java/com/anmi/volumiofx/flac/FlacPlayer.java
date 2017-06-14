package com.anmi.volumiofx.flac;

import java.io.IOException;
import java.io.InputStream;

public class FlacPlayer {
    Decoder decoder = new Decoder();

    public void play(String fileName) throws IOException {
        decoder.decode(fileName);
    }

    public void play(InputStream file) throws IOException {
        decoder.decode(file);
    }

    public void stop() {
        decoder.stopPlay();
    }


    public boolean isPlaying() {
        return decoder.isPlaying();
    }

}
