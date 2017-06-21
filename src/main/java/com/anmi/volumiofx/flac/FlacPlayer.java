package com.anmi.volumiofx.flac;

import java.io.IOException;
import java.io.InputStream;

public class FlacPlayer {
    Decoder decoder = new Decoder();

    public void play(InputStream file) throws IOException {
        decoder.decode(file);
    }

    public void resume() throws IOException {
        decoder.resume();
    }

    public void stop() {
        decoder.stop();
    }

    public void stopAndDrain() {
        decoder.stopAndDrain();
    }


    public boolean isPlaying() {
        return decoder.isPlaying();
    }

}
