package com.anmi.volumiofx.flac;

import java.io.IOException;
import java.io.InputStream;

public class Player implements Runnable {
    private FlacPlayer flacPlayer = new FlacPlayer();
    private InputStream file;
    private volatile boolean running;

    public void setTrack(InputStream inputStream) {
        file = inputStream;
        if (flacPlayer.isPlaying()) {
            shutDown();
        }
        running = true;
    }

    public void resume(){
        try {
            flacPlayer.resume();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        //  running = false;
        flacPlayer.stop();
    }

    public void shutDown() {
        running = false;
    }

    @Override
    public void run() {
            try {
                flacPlayer.play(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }





    /*For Runnable
    @Override
    public Object call() {
        while (running) {
            try {
                flacPlayer.setTrack(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flacPlayer.shutDown();
        return running;
    }*/
}

