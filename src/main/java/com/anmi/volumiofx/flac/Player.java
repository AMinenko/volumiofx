package com.anmi.volumiofx.flac;

import com.anmi.volumiofx.scene.VolumioSmbFile;
import org.eclipse.jetty.util.BlockingArrayQueue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class Player{
    private FlacPlayer flacPlayer = new FlacPlayer();
    private Thread thread;
    private InputStream inputStream;
    private Queue<VolumioSmbFile> playlist = new BlockingArrayQueue<>();

    public void play(InputStream inputStream) {
        this.inputStream=inputStream;
        flacPlayer.setFile(inputStream);
        thread = new Thread(flacPlayer);
        thread.start();
    }

    public void stop() throws IOException {
       if( thread != null && thread.isAlive()){
           flacPlayer.stop();
           this.inputStream.close();
       }
    }

}

