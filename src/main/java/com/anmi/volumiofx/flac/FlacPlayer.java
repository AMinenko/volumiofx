package com.anmi.volumiofx.flac;

import com.anmi.volumiofx.scene.VolumioSmbFile;
import org.eclipse.jetty.util.BlockingArrayQueue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class FlacPlayer implements Runnable {
    private InputStream file;
    private volatile boolean running;

    Decoder decoder = new Decoder();

    public void play(InputStream file) throws IOException {
        decoder.decode(file);
    }

    public void stop() {
        running = false;
        decoder.stopPlay();
    }


    public boolean isPlaying() {
        return decoder.isPlaying();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                decoder.decode(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
}
