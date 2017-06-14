package com.anmi.volumiofx.flac;

import java.io.IOException;
import java.io.InputStream;

public class Player implements Runnable {
    private FlacPlayer flacPlayer = new FlacPlayer();
    private InputStream file;
    private volatile boolean running;
    private Thread thread = new Thread(this);;

    public void play(InputStream inputStream) {
        file = inputStream;
       /* should be replaced by a loop
       if (thread != null) {
            thread.stop();
        }*/
        if (running) {
            stop();
        }

        thread.start();
        running = true;

    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                flacPlayer.play(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flacPlayer.stop();
    }

    /*For Runnable
    @Override
    public Object call() {
        while (running) {
            try {
                flacPlayer.play(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flacPlayer.stop();
        return running;
    }*/
}

